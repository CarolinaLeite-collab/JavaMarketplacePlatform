package TOPSECRET.domain.directsale;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.*;

import java.time.Period;
import java.util.List;

/**
 * Represents a direct sale of an {@link Item} with a specified {@link Price} and optional time limit.
 * <p>
 * Ensures that both the item and price are provided and that the time limit, if specified, is not negative.
 * Provides methods to retrieve the item, price, time limit, and to check if the sale is by a specific {@link Author}.
 * </p>
 */

public class DirectSale implements AggregateRoot<DirectSaleId> {

    private final List<Item> _items;
    private final Price _price;
    private final Period _timeLimit; // optional
    private DirectSaleId _directSaleId;

    DirectSale(List<Item> items, Price price, Period timeLimit) {

        requiresItemAndPrice(items, price);
        timeLimitMustBeValid(timeLimit);

        for (Item item : items) {
            if (item == null) {
                throw new IllegalArgumentException("Items cannot contain null elements.");
            }
            if (item.get_saleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException("Item is already on sale.");
            }
        }

        _items = items;
        _price = price;
        _timeLimit = timeLimit;// may be null = unlimited duration
        _directSaleId = new DirectSaleId();

        for (Item item : _items) {
            item.markAsDirectSale();
        }
    }

    public List<Item> getItems() { return _items; }
    public Price getPrice() { return _price; }
    public Period getTimeLimit() { return _timeLimit; }

    private static void requiresItemAndPrice(List<Item> items, Price price) {
        if (items == null) {
            throw new IllegalArgumentException("Item is required for a direct sale");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price is required for a direct sale");
        }
    }
    private static void timeLimitMustBeValid(Period timeLimit) {
        if (timeLimit != null && timeLimit.isNegative()) {
            throw new IllegalArgumentException("Time limit cannot be negative");
        }
    }

    @Override
    public DirectSaleId identity() {
        return _directSaleId;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DirectSale other)) return false;
        return _directSaleId.equals(other._directSaleId);
    }

    @Override
    public boolean sameAs(Object object) {
        if (object instanceof DirectSale) {
            DirectSale other = (DirectSale) object;

            if (this._items.equals(other._items) &&
                    this._price.equals(other._price) &&
                    this._timeLimit.equals(other._timeLimit)
            )
                return true;
        }
        return false;
    }
}
