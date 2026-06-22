package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.repository.ISaleRepo;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleFactory;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.sale.SaleLineFactory;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
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
                       SaleLineFactory saleLineFactory,
                       ShoppingCartService shoppingCartService,
                       PaymentService paymentService,
                       DirectSaleService directSaleService,
                       ItemService itemService,
                       SaleFactory saleFactory,
                       SaleLineFactory saleLineFactory1) {
        _saleRepo = saleRepo;
        _shoppingCartService = shoppingCartService;
        _paymentService = paymentService;
        _directSaleService = directSaleService;
        _itemService = itemService;
        _saleFactory = saleFactory;
        _saleLineFactory = saleLineFactory1;
    }

    @Transactional
    public List<Sale> findUserSales(User user) {

        List<Sale> userSales = new ArrayList<>();
        UserId userId = user.identity();

        return _saleRepo.findByUserId(userId);

    }

    @Transactional
    public Sale findSaleById(SaleId saleId) {

        return _saleRepo.ofIdentity(saleId)
                .orElseThrow(() -> new NoSuchElementException("Sale does not exist!"));


    }

    @Transactional
    public SaleLine getSaleLineById(SaleId saleId, SaleLineId saleLineId) {

        Sale sale = findSaleById(saleId);

        return sale.get_saleLines().stream()
                .filter(saleLine -> saleLine.identity().equals(saleLineId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("SaleLine not found: " + saleLineId));

    }

    @Transactional
    public Sale createSaleFromCart(ShoppingCartId cartId, String email) {

        // MUDAR METODO PARA ACEITAR CARTID
        ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId.toString());

        if (cart.getCartLines().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart!");
        }

        List<SaleLine> saleLines = new ArrayList<>();

        for (ShoppingCartLine shoppingCartLine : cart.getCartLines()) {

            SaleLine newSaleLine = _saleLineFactory.createSaleLine(
                    shoppingCartLine.getSellerId(),
                    shoppingCartLine.getPriceAtAddition(),
                    shoppingCartLine.getDirectSaleId()
            );

            saleLines.add(newSaleLine);

        }

        Sale newSale = _saleFactory.createSale(
                cart.getBuyerId(),
                saleLines
        );

        // Fake payment
        boolean isPaymentSuccessful = _paymentService.isPaymentSuccessful(newSale.get_totalAmount());

        if (isPaymentSuccessful) {

            for (SaleLine saleLine : newSale.get_saleLines()) {

                DirectSaleId directSaleId = saleLine.get_directSaleId();
                DirectSale directSale = _directSaleService.getDirectSaleById(directSaleId.toString());

                for (ItemId itemId : directSale.getItemsId()) {

                    _itemService.markItemAsSold(itemId.toString());

                }

                _directSaleService.markDirectSaleAsCompleted(directSaleId.toString());

            }

            newSale.markSaleAsCompleted();
            _shoppingCartService.clearShoppingCartLines(cart.identity().toString());
            _saleRepo.save(newSale);

        } else {

            newSale.markSaleAsCancelled();
            _saleRepo.save(newSale);

        }

        return newSale;

    }
}
