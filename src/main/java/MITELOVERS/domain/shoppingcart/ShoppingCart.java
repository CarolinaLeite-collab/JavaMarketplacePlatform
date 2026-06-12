package MITELOVERS.domain.shoppingcart;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements AggregateRoot<ShoppingCartId> {

    private ShoppingCartId _cartId;
    private UserId _buyerId;
    private Price _totalAmount;
    private List<ShoppingCartLine> _cartItems;

    public ShoppingCart(UserId buyerId) {

        _cartId = new ShoppingCartId();
        _buyerId = buyerId;
        _totalAmount = null;
        _cartItems = new ArrayList<>();

    }

    public ShoppingCart(ShoppingCartId cartId,
                        UserId buyerId,
                        Price totalAmount,
                        List<ShoppingCartLine> cartItems) {

        _cartId = cartId;
        _buyerId = buyerId;
        _totalAmount = totalAmount;
        _cartItems = cartItems;

    }

    @Override
    public ShoppingCartId identity() {
        return _cartId;
    }

    @Override
    public boolean sameAs(Object object) {
        return equals(object);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return _cartId.equals(that._cartId) && _buyerId.equals(that._buyerId);
    }

    @Override
    public int hashCode() {
        return _cartId.hashCode();
    }
}
