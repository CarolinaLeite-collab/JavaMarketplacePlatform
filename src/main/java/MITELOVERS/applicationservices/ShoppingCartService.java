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

/**
 * Application service responsible for ShoppingCart-related operations.
 * Supports querying carts by cart ID or user ID, managing cart lines
 * (adding, finding, and removing), and clearing all lines after checkout.
 * Enforces the constraint that a user cannot add their own direct sale to their cart.
 * All operations are transactional.
 */

@Service
@AllArgsConstructor
public class ShoppingCartService {

    private final IShoppingCartRepo _shoppingCartRepo;
    private final DirectSaleService _directSaleService;
    private final ShoppingCartLineFactory _shoppingCartLineFactory;

    @Transactional(readOnly = true)
    public ShoppingCart findCartByCartId(ShoppingCartId cartId) {

        return _shoppingCartRepo.ofIdentity(cartId)
                .orElseThrow(() -> new NoSuchElementException("ShoppingCart not found!"));

    }

    @Transactional(readOnly = true)
    public ShoppingCart findCartByUserId(UserId userId) {

        return _shoppingCartRepo.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("ShoppingCart not found for user: " + userId));

    }

    @Transactional
    public ShoppingCart clearShoppingCartLines(ShoppingCartId cartId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);

        shoppingCart.clearShoppingCart();

        _shoppingCartRepo.save(shoppingCart);

        return shoppingCart;

    }

    @Transactional(readOnly = true)
    public ShoppingCartLine findCartLineByLineCartId(ShoppingCartId cartId, ShoppingCartLineId cartLineId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);

        return shoppingCart.getCartLines().stream()
                .filter(shoppingCartLine -> shoppingCartLine.identity().equals(cartLineId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("ShoppingCartLine not found: " + cartLineId));


    }

    @Transactional
    public ShoppingCartLine addCartLineToCart(ShoppingCartId cartId, DirectSaleId directSaleId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);
        DirectSale directSale = _directSaleService.getDirectSaleById(directSaleId);

        if (shoppingCart.getBuyerId().equals(directSale.getSellerId())) {

            throw new IllegalStateException("Cannot buy own direct sale!");

        }

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
    public ShoppingCart deleteCartLineByLineCartId(ShoppingCartId cartId, ShoppingCartLineId cartLineId) {

        ShoppingCart shoppingCart = findCartByCartId(cartId);
        ShoppingCartLine shoppingCartLine = findCartLineByLineCartId(cartId, cartLineId);
        shoppingCart.removeCartLine(shoppingCartLine.identity());

        return _shoppingCartRepo.save(shoppingCart);

    }



}
