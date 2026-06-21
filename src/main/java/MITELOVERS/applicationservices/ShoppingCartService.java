package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.repository.IShoppingCartRepo;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.shoppingcart.ShoppingCartLineFactory;
import MITELOVERS.domain.valueobject.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ShoppingCartService {

    private final IShoppingCartRepo _shoppingCartRepo;
    private final DirectSaleService _directSaleService;
    private final ShoppingCartLineFactory _shoppingCartLineFactory;

    @Transactional
    public ShoppingCart findCartByCartId(String cartId) {

        ShoppingCartId shoppingCartId = new ShoppingCartId(cartId);

        return _shoppingCartRepo.ofIdentity(shoppingCartId)
                .orElseThrow(() -> new NoSuchElementException("ShoppingCart not found!"));

    }

    @Transactional
    public ShoppingCart findCartByUserId(String email) {

        UserId userId = new UserId(new Email(email));

        return _shoppingCartRepo.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("ShoppingCart not found for user: " + userId));

    }

    @Transactional
    public ShoppingCart clearShoppingCartLines(String cartId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);

        shoppingCart.clearShoppingCart();

        _shoppingCartRepo.save(shoppingCart);

        return shoppingCart;

    }

    @Transactional
    public ShoppingCartLine findCartLineByLineCartId(String cartId, String cartLineId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);
        ShoppingCartLineId shoppingCartLineId = new ShoppingCartLineId(cartLineId);

        return shoppingCart.getCartLines().stream()
                .filter(shoppingCartLine -> shoppingCartLine.identity().equals(shoppingCartLineId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("ShoppingCartLine not found: " + cartLineId));


    }

    @Transactional
    public ShoppingCartLine addCartLineToCart(String cartId, String directSaleId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);
        DirectSale directSale = _directSaleService.getDirectSaleById(directSaleId);

        ShoppingCartLine newCartLine = _shoppingCartLineFactory.createNewShoppingCartLine(
                directSale.identity(),
                directSale.getSellerId(),
                directSale.getPrice()
        );

        shoppingCart.addCartLine(newCartLine);

        _shoppingCartRepo.save(shoppingCart);

        return newCartLine;

    }

    @Transactional
    public ShoppingCart deleteCartLineByLineCartId(String cartId, String cartLineId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);
        ShoppingCartLine shoppingCartLine = findCartLineByLineCartId(cartId, cartLineId);
        shoppingCart.removeCartLine(shoppingCartLine.identity());

        return _shoppingCartRepo.save(shoppingCart);

    }



}
