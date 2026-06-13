package MITELOVERS.domain.shoppingcart;

import MITELOVERS.ddd.DomainEntity;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single line item within a {@link ShoppingCart}, referencing a
 * {@link DirectSale} along with the seller and the
 * price at the time it was added to the cart.
 */

public class ShoppingCartLine implements DomainEntity<ShoppingCartLineId> {

    private ShoppingCartLineId _shoppingCartLineId;
    private DirectSaleId _directSaleId;
    private UserId _sellerId;
    private Price _priceAtAddition;
    private LocalDateTime _addedAt;

    public ShoppingCartLine(DirectSaleId directSaleId,
                            UserId sellerId,
                            Price priceAtAddition) {

        _shoppingCartLineId = new ShoppingCartLineId();
        _directSaleId = Objects.requireNonNull(directSaleId, "directSaleId cannot be null!");
        _sellerId = Objects.requireNonNull(sellerId, "sellerId cannot be null!");
        _priceAtAddition = Objects.requireNonNull(priceAtAddition, "priceAtAddition cannot be null!");
        _addedAt = LocalDateTime.now();

    }

    //Re-hydration
    public ShoppingCartLine(ShoppingCartLineId shoppingCartLineId,
                            DirectSaleId directSaleId,
                            UserId sellerId,
                            Price priceAtAddition,
                            LocalDateTime addetAt) {

        this(directSaleId, sellerId, priceAtAddition);
        _shoppingCartLineId = Objects.requireNonNull(shoppingCartLineId, "ShoppingCartLineId cannot be null!");
        _addedAt = Objects.requireNonNull(addetAt, "Time of addition cannot be null!");

    }

    public DirectSaleId getDirectSaleId() {
        return _directSaleId;
    }
    public UserId getSellerId() {
        return _sellerId;
    }
    public Price getPriceAtAddition() {
        return _priceAtAddition;
    }
    public LocalDateTime getAddedAt() {
        return _addedAt;
    }

    @Override
    public ShoppingCartLineId identity() {
        return _shoppingCartLineId;
    }

    @Override
    public boolean sameAs(Object object) {
        return equals(object);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartLine that = (ShoppingCartLine) o;
        return Objects.equals(_shoppingCartLineId, that._shoppingCartLineId) && Objects.equals(_directSaleId, that._directSaleId) && Objects.equals(_sellerId, that._sellerId) && Objects.equals(_priceAtAddition, that._priceAtAddition) && Objects.equals(_addedAt, that._addedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_shoppingCartLineId, _directSaleId, _sellerId, _priceAtAddition, _addedAt);
    }

}
