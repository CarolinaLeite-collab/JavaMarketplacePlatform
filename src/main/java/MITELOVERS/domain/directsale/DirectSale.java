package MITELOVERS.domain.directsale;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

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
    private final Duration _timeLimit; // optional
    private DirectSaleId _directSaleId;
    private Instant _creationDate;

    DirectSale(List<ItemId> itemsId, Price price, Duration timeLimit) {

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
        _creationDate = Instant.now();

    }

    // rehydration
    DirectSale(DirectSaleId directSaleId, List<ItemId> itemsId, Price price, Duration timeLimit,Instant creationDate) {
        this(itemsId, price, timeLimit);
        _directSaleId = Objects.requireNonNull(directSaleId, "DirectSaleId cannot be null");
        _creationDate = creationDate;

    }

    public List<ItemId> getItemsId() { return _itemsId; }
    public Price getPrice() { return _price; }
    public Duration getTimeLimit() { return _timeLimit; }
    public Instant getCreationDate(){ return _creationDate;}

    private static void requiresItemAndPrice(List<ItemId> itemsId, Price price) {
        if (itemsId == null) {
            throw new IllegalArgumentException("ItemId is required for a direct sale");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price is required for a direct sale");
        }
    }

    private static void timeLimitMustBeValid(Duration timeLimit) {
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