package MITELOVERS.domain.shoppingcart;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingCart implements AggregateRoot<ShoppingCartId> {

    private ShoppingCartId _cartId;
    private UserId _buyerId;
    private Price _totalAmount;
    private List<ShoppingCartLine> _cartItems;

    public ShoppingCart(UserId buyerId) {

        _cartId = new ShoppingCartId();
        _buyerId = Objects.requireNonNull(buyerId, "buyerId cannot be null!");
        _totalAmount = null;
        _cartItems = new ArrayList<>();

    }

    public ShoppingCart(ShoppingCartId cartId,
                        UserId buyerId,
                        Price totalAmount,
                        List<ShoppingCartLine> cartItems) {

        _cartId = Objects.requireNonNull(cartId, "cartId cannot be null!");
        _buyerId = Objects.requireNonNull(buyerId, "buyerId cannot be null!");
        _cartItems = (cartItems == null) ? new ArrayList<>() : new ArrayList<>(cartItems);

        if (_cartItems.isEmpty() && totalAmount != null) {
            throw new IllegalArgumentException("TotalAmount must be null when cart has no items!");
        }

        if (!_cartItems.isEmpty() && totalAmount == null) {
            throw new IllegalArgumentException("TotalAmount cannot be null when cart has items!");
        }

        _totalAmount = totalAmount;

    }

    public UserId getBuyerId() { return _buyerId; }
    public Price getTotalAmount() { return _totalAmount; }
    public List<ShoppingCartLine> getCartItems() { return List.copyOf(_cartItems); }

    public void clearShoppingCart() {
        _cartItems.clear();
        _totalAmount = null;
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
