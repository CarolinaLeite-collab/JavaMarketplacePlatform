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

    private final ShoppingCartLineId _shoppingCartLineId;
    private final DirectSaleId _directSaleId;
    private final UserId _sellerId;
    private final Price _priceAtAddition;
    private final LocalDateTime _addedAt;

    // rehydration / primary
    ShoppingCartLine(ShoppingCartLineId shoppingCartLineId,
                     DirectSaleId directSaleId,
                     UserId sellerId,
                     Price priceAtAddition,
                     LocalDateTime addedAt) {

        _shoppingCartLineId = Objects.requireNonNull(shoppingCartLineId, "ShoppingCartLineId cannot be null!");
        _directSaleId = Objects.requireNonNull(directSaleId, "directSaleId cannot be null!");
        _sellerId = Objects.requireNonNull(sellerId, "sellerId cannot be null!");
        _priceAtAddition = Objects.requireNonNull(priceAtAddition, "priceAtAddition cannot be null!");
        _addedAt = Objects.requireNonNull(addedAt, "Time of addition cannot be null!");
    }

    // creation
    ShoppingCartLine(DirectSaleId directSaleId, UserId sellerId, Price priceAtAddition) {
        this(new ShoppingCartLineId(), directSaleId, sellerId, priceAtAddition, LocalDateTime.now());
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

        if (object instanceof ShoppingCartLine) {
            ShoppingCartLine oShoppingCartLine = (ShoppingCartLine) object;

            if ((Objects.equals(_sellerId, oShoppingCartLine._sellerId))
                    && (Objects.equals(_directSaleId, oShoppingCartLine._directSaleId))
                    && (Objects.equals(_priceAtAddition, oShoppingCartLine._priceAtAddition))
                    && (Objects.equals(_addedAt, oShoppingCartLine._addedAt))
            )

                return true;
        }

        return false;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartLine that = (ShoppingCartLine) o;
        return _shoppingCartLineId.equals(that._shoppingCartLineId);
    }

    @Override
    public int hashCode() {
        return _shoppingCartLineId.hashCode();
    }

}
