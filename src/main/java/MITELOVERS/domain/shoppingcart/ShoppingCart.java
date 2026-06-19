package MITELOVERS.domain.shoppingcart;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Represents a buyer's shopping cart, containing a collection of {@link ShoppingCartLine}s
 * and the total amount across all lines.
 * <p>
 * Enforces that all cart lines share the same currency, and that the total amount
 * is {@code null} when the cart is empty and non-null otherwise.
 * </p>
 */

public class ShoppingCart implements AggregateRoot<ShoppingCartId> {

    private final ShoppingCartId _cartId;
    private final UserId _buyerId;
    private Price _totalAmount;
    private List<ShoppingCartLine> _cartLines;

    // rehydration / primary
    ShoppingCart(ShoppingCartId cartId,
                 UserId buyerId,
                 Price totalAmount,
                 List<ShoppingCartLine> cartLines) {

        _cartId = Objects.requireNonNull(cartId, "cartId cannot be null!");
        _buyerId = Objects.requireNonNull(buyerId, "buyerId cannot be null!");
        _cartLines = (cartLines == null) ? new ArrayList<>() : new ArrayList<>(cartLines);

        if (_cartLines.isEmpty() && totalAmount != null) {
            throw new IllegalArgumentException("TotalAmount must be null when cart has no items!");
        }

        if (!_cartLines.isEmpty() && totalAmount == null) {
            throw new IllegalArgumentException("TotalAmount cannot be null when cart has items!");
        }

        if (!allCartLinesShareSameCurrency(_cartLines)) {
            throw new IllegalStateException("CartLines must all have the same currency!");
        }

        recalculateTotalAmount();
    }

    // creation
    ShoppingCart(UserId buyerId) {
        this(new ShoppingCartId(), buyerId, null, new ArrayList<>());
    }

    public UserId getBuyerId() { return _buyerId; }
    public Price getTotalAmount() { return _totalAmount; }
    public List<ShoppingCartLine> getCartLines() { return List.copyOf(_cartLines); }

    public void clearShoppingCart() {
        _cartLines.clear();
        _totalAmount = null;
    }

    public void addCartLine(ShoppingCartLine shoppingCartLine) {

        if (shoppingCartLine == null) {
            throw new IllegalArgumentException("Shopping Cart Line cannot be null!");
        }

        if (!isSameCurrencyAsExisting(shoppingCartLine)) {
            throw new IllegalArgumentException("Cannot mix currencies in a shopping cart!");
        }

        for(ShoppingCartLine cartLine : _cartLines) {

            if (cartLine.getDirectSaleId().equals(shoppingCartLine.getDirectSaleId())) {
                throw new IllegalArgumentException("This DirectSale is already present in the cart!");
            }

        }

        _cartLines.add(shoppingCartLine);
        recalculateTotalAmount();
    }

    public void removeCartLine(ShoppingCartLineId cartLineId) {

        boolean isRemoved = _cartLines.removeIf(
                cartLine -> cartLine.identity().equals(cartLineId)
        );

        if (!isRemoved) {
            throw new NoSuchElementException("ShoppingCartLine not found in cart: " + cartLineId);
        }

        recalculateTotalAmount();
    }

    private boolean allCartLinesShareSameCurrency(List<ShoppingCartLine> cartLines) {

        if (cartLines.isEmpty()) {
            return true;
        }

        Currency currencyToCompare = cartLines.get(0).getPriceAtAddition().getCurrency();

        for (ShoppingCartLine cartLine : cartLines) {
            if (!cartLine.getPriceAtAddition().getCurrency().equals(currencyToCompare)) {
                return false;
            }
        }

        return true;
    }

    private boolean isSameCurrencyAsExisting(ShoppingCartLine cartLine) {

        if (_cartLines.isEmpty()) {
            return true;
        }

        Currency existingCurrency = _cartLines.get(0).getPriceAtAddition().getCurrency();

        return cartLine.getPriceAtAddition().getCurrency().equals(existingCurrency);
    }

    private void recalculateTotalAmount() {

        if (_cartLines.isEmpty()) {
            _totalAmount = null;
            return;
        }

        Currency currency = _cartLines.get(0).getPriceAtAddition().getCurrency();
        double sum = _cartLines.stream()
                .mapToDouble(line -> line.getPriceAtAddition().getValue())
                .sum();

        _totalAmount = new Price(sum, currency);
    }

    @Override
    public ShoppingCartId identity() {
        return _cartId;
    }

    @Override
    public boolean sameAs(Object object) {

        if (object instanceof ShoppingCart) {
            ShoppingCart oShoppingCart = (ShoppingCart) object;

            if ((Objects.equals(_buyerId, oShoppingCart._buyerId))
                    && (Objects.equals(_totalAmount, oShoppingCart._totalAmount))
                    && (Objects.equals(_cartLines, oShoppingCart._cartLines))
            )

                return true;

        }

        return false;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return _cartId.equals(that._cartId);
    }

    @Override
    public int hashCode() {
        return _cartId.hashCode();
    }
}