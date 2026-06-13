package MITELOVERS.domain.shoppingcart;

import MITELOVERS.ddd.DomainEntity;
import MITELOVERS.domain.valueobject.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class ShoppingCartLine implements DomainEntity<ShoppingCartLineId> {

    private ShoppingCartLineId _shoppingCartLineId;
    private DirectSaleId _directSaleId;
    private ItemId _itemId;
    private UserId _sellerId;
    private Price _priceAtAddition;
    private LocalDateTime _addedAt;

    public ShoppingCartLine(DirectSaleId directSaleId,
                            ItemId itemId,
                            UserId sellerId,
                            Price priceAtAddition) {

        _shoppingCartLineId = new ShoppingCartLineId();
        _directSaleId = Objects.requireNonNull(directSaleId, "directSaleId cannot be null!");
        _itemId = Objects.requireNonNull(itemId, "itemId cannot be null!");
        _sellerId = Objects.requireNonNull(sellerId, "sellerId cannot be null!");
        _priceAtAddition = Objects.requireNonNull(priceAtAddition, "priceAtAddition cannot be null!");
        _addedAt = LocalDateTime.now();

    }

    //Re-hydration
    public ShoppingCartLine(ShoppingCartLineId shoppingCartLineId,
                            DirectSaleId directSaleId,
                            ItemId itemId,
                            UserId sellerId,
                            Price priceAtAddition,
                            LocalDateTime addetAt) {

        this(directSaleId, itemId, sellerId, priceAtAddition);
        _shoppingCartLineId = Objects.requireNonNull(shoppingCartLineId, "ShoppingCartLineId cannot be null!");
        _addedAt = Objects.requireNonNull(addetAt, "Time of addition cannot be null!");

    }

    public DirectSaleId getDirectSaleId() {
        return _directSaleId;
    }
    public ItemId getItemId() {
        return _itemId;
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
        return Objects.equals(_shoppingCartLineId, that._shoppingCartLineId) && Objects.equals(_directSaleId, that._directSaleId) && Objects.equals(_itemId, that._itemId) && Objects.equals(_sellerId, that._sellerId) && Objects.equals(_priceAtAddition, that._priceAtAddition) && Objects.equals(_addedAt, that._addedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_shoppingCartLineId, _directSaleId, _itemId, _sellerId, _priceAtAddition, _addedAt);
    }

}
