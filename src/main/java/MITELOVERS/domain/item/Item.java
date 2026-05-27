package MITELOVERS.domain.item;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.*;

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
    private Name _name;
    private Picture _picture;

    Item(ItemId itemId, EditionId editionId, Condition condition, Description description, SaleStatus saleStatus, Name name, Picture picture) {
        _itemId = itemId;
        _editionId = editionId;
        _condition = condition;
        _description = description;
        _saleStatus = saleStatus;
        _name = name;
        _picture = picture;
    }

    Item(EditionId editionId, Condition condition, Description description, Name name, Picture picture) {
        this(new ItemId(), editionId, condition, description, SaleStatus.NotOnSale, name, picture);
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

    public EditionId getEditionId() {
        return _editionId;
    }

    public Condition getCondition() {
        return _condition;
    }

    public SaleStatus getSaleStatus() {
        return _saleStatus;
    }

    public Description getDescription() {
            return _description;
        }

    public Name getName() {
        return _name;
    }

    public Picture getPicture() { return  _picture; }

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
