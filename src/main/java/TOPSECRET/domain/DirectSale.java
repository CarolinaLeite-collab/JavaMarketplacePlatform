package TOPSECRET.domain;

import java.time.Period;

/**
 * Represents a direct sale of an {@link Item} with a specified {@link Price} and optional time limit.
 * <p>
 * Ensures that both the item and price are provided and that the time limit, if specified, is not negative.
 * Provides methods to retrieve the item, price, time limit, and to check if the sale is by a specific {@link Author}.
 * </p>
 */

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

    public boolean isByPublication (Publication publication) {

        if (item.getPublication().equals(publication)) {
            return true;
        }
        return false;
    }

    public boolean isByPublisher (PublishingCompany publisher) {

        if (item.getPublication().getPublisher().equals(publisher)) {

            return true;
        }
        return false;
    }
}
