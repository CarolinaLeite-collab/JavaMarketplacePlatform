package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.repository.ISaleRepo;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleFactory;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.sale.SaleLineFactory;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SaleService {

    private final ISaleRepo _saleRepo;
    private final ShoppingCartService _shoppingCartService;
    private final PaymentService _paymentService;
    private final DirectSaleService _directSaleService;
    private final ItemService _itemService;
    private final SaleFactory _saleFactory;
    private final SaleLineFactory _saleLineFactory;

    public SaleService(ISaleRepo saleRepo,
                       ShoppingCartService shoppingCartService,
                       PaymentService paymentService,
                       DirectSaleService directSaleService,
                       ItemService itemService,
                       SaleFactory saleFactory,
                       SaleLineFactory saleLineFactory) {
        _saleRepo = saleRepo;
        _shoppingCartService = shoppingCartService;
        _paymentService = paymentService;
        _directSaleService = directSaleService;
        _itemService = itemService;
        _saleFactory = saleFactory;
        _saleLineFactory = saleLineFactory;
    }

    @Transactional(readOnly = true)
    public List<Sale> findUserSales(UserId userId) {

        return _saleRepo.findByUserId(userId);

    }

    @Transactional(readOnly = true)
    public Sale findSaleById(SaleId saleId) {

        return _saleRepo.ofIdentity(saleId)
                .orElseThrow(() -> new NoSuchElementException("Sale does not exist!"));


    }

    @Transactional(readOnly = true)
    public SaleLine getSaleLineById(SaleId saleId, SaleLineId saleLineId) {

        Sale sale = findSaleById(saleId);

        return sale.get_saleLines().stream()
                .filter(saleLine -> saleLine.identity().equals(saleLineId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("SaleLine not found: " + saleLineId));

    }

    @Transactional
    public Sale createSaleFromCart(ShoppingCartId cartId) {

        ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);

        if (cart.getCartLines().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart!");
        }

        List<SaleLine> saleLines = createSaleLines(cart);
        Sale newSale = _saleFactory.createSale(cart.getBuyerId(), saleLines);

        if (_paymentService.isPaymentSuccessful(newSale.get_totalAmount())) {
            completeSale(newSale, cartId);
        } else {
            cancelSale(newSale);
        }

        return newSale;
    }

    private List<SaleLine> createSaleLines(ShoppingCart cart) {

        List<SaleLine> saleLines = new ArrayList<>();

        for (ShoppingCartLine line : cart.getCartLines()) {

            saleLines.add(_saleLineFactory.createSaleLine(
                    line.getSellerId(),
                    line.getPriceAtAddition(),
                    line.getDirectSaleId()
            ));

        }

        return saleLines;
    }

    private void completeSale(Sale sale, ShoppingCartId cartId) {

        completeSaleLines(sale.get_saleLines());
        sale.markSaleAsCompleted();

        _shoppingCartService.clearShoppingCartLines(cartId);
        _saleRepo.save(sale);

    }

    private void completeSaleLines(List<SaleLine> saleLines) {

        for (SaleLine saleLine : saleLines) {

            DirectSaleId directSaleId = saleLine.get_directSaleId();
            DirectSale directSale = _directSaleService.getDirectSaleById(directSaleId);

            for (ItemId itemId : directSale.getItemsId()) {

                _itemService.markItemAsSold(itemId);

            }

            _directSaleService.markDirectSaleAsCompleted(directSaleId);

        }
    }

    private void cancelSale(Sale sale) {

        sale.markSaleAsCancelled();
        _saleRepo.save(sale);

    }
}
