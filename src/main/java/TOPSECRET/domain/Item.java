package TOPSECRET.domain;

import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.valueobject.*;

/**
 * <h3>Item represents a publication that has been listed for sale.</h3>
 * <p>
 * An {@code Item} wraps a {@link Edition} and captures its sale context,
 * including its {@link Condition} and the type of sale it belongs to .
 * Each item can only be part of either a DirectSale or an Auction, but never both.
 * </p>
 *
 * <p>
 * This class enforces mutual exclusivity between direct sales and auctions
 * to maintain domain consistency. Attempting to assign both types of sale will
 * result in an {@link IllegalStateException}.
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
