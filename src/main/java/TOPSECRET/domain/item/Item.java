package TOPSECRET.domain.item;

import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.*;

/**
 * <h3>Item represents a unit of a publication available in the system.</h3>
 *
 * <p>
 * An {@code Item} is identified by an {@link EditionId} and describes a specific
 * instance of a publication through its {@link Condition} and {@link Description}.
 * It also maintains its current {@link SaleStatus}, representing its lifecycle
 * within the selling process.
 * </p>
 *
 * <p>
 * An item can transition between different sale states (e.g. not on sale, on auction,
 * on direct sale, or sold), but it cannot be simultaneously in multiple sale states.
 * Invalid transitions will result in an {@link IllegalStateException}.
 * </p>
 */

public class Item implements AggregateRoot<ItemId> {

    private final Condition _condition;
    private final EditionId _editionId;
    private final Description _description;
    private final ItemId _itemId;
    private SaleStatus _saleStatus;


    Item(EditionId editionId, Condition condition, Description description) {
        _editionId = editionId;
        _condition = condition;
        _description = description;

        _itemId = new ItemId();
        _saleStatus = SaleStatus.NotOnSale;
    }

    @Override
    public ItemId identity() {
        return _itemId;
    }

    @Override
    public boolean sameAs(Object object) {

        return equals(object);
    }

    public void markAsAuction() {
        ensureNotOnSale();
        _saleStatus = SaleStatus.OnAuction;
    }

    public void markAsDirectSale() {
        ensureNotOnSale();
        _saleStatus = SaleStatus.OnDirectSale;
    }

    public void markAsSold() {
        if (_saleStatus == SaleStatus.NotOnSale) {
            throw new IllegalStateException("Item is not on sale.");
        }

        _saleStatus = SaleStatus.Sold;
    }

    private void ensureNotOnSale() {
        if (_saleStatus != SaleStatus.NotOnSale) {
            throw new IllegalStateException("Item is already on sale.");
        }
    }

    public EditionId get_editionId() {
        return _editionId;
    }

    public Condition get_condition() {
        return _condition;
    }

    public SaleStatus get_saleStatus() {
        return _saleStatus;
    }

    public Description get_description() {
            return _description;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item other)) return false;
        return _itemId.equals(other._itemId);
    }

    @Override
    public int hashCode() {
        return _itemId.hashCode();
    }
}
