package TOPSECRET.domain;

import java.time.Period;
import java.util.Objects;

public class DirectSale {

    private final Item item;
    private final Price price;
    private final Period timeLimit; // optional

    public DirectSale(Item item, Price price, Period timeLimit) {

        requiresItemAndPrice(item, price);
        timeLimitMustBeValid(timeLimit);

        this.item = item;
        this.price = price;
        this.timeLimit = timeLimit;  // may be null = unlimited duration
    }

    public Item getItem() { return item; }
    public Price getPrice() { return price; }
    public Period getTimeLimit() { return timeLimit; }

    private static void requiresItemAndPrice(Item item, Price price) {
        if (item == null) {
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

    public boolean isByAuthor (Author author) {

        if (item.getPublication().getAuthor().equals(author)) {

            return true;

        }

        return false;

    }
}
