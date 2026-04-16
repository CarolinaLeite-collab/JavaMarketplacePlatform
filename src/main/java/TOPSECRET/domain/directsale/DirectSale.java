package TOPSECRET.domain.directsale;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.valueobject.DirectSaleId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;

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

    private final List<ItemId> _itemsId;
    private final Price _price;
    private final Period _timeLimit; // optional
    private DirectSaleId _directSaleId;

    DirectSale(List<ItemId> itemsId, Price price, Period timeLimit) {

        requiresItemAndPrice(itemsId, price);
        timeLimitMustBeValid(timeLimit);

        for (ItemId itemId : itemsId) {
            if (itemId == null) {
                throw new IllegalArgumentException("Items cannot contain null elements.");
            }
        }

        _itemsId = itemsId;
        _price = price;
        _timeLimit = timeLimit;// may be null = unlimited duration
        _directSaleId = new DirectSaleId();

    }

    public List<ItemId> getItemsId() { return _itemsId; }
    public Price getPrice() { return _price; }
    public Period getTimeLimit() { return _timeLimit; }

    private static void requiresItemAndPrice(List<ItemId> itemsId, Price price) {
        if (itemsId == null) {
            throw new IllegalArgumentException("ItemId is required for a direct sale");
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

            if (this._itemsId.equals(other._itemsId) &&
                    this._price.equals(other._price) &&
                    this._timeLimit.equals(other._timeLimit)
            )
                return true;
        }
        return false;
    }
}
