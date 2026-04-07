package TOPSECRET.domain;

import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;

/**
 * Represents a direct sale of an {@link Item} with a specified {@link Price} and optional time limit.
 * <p>
 * Ensures that both the item and price are provided and that the time limit, if specified, is not negative.
 * Provides methods to retrieve the item, price, time limit, and to check if the sale is by a specific {@link Author}.
 * </p>
 */

public class DirectSale {

    private final Item _item;
    private final Price _price;
    private final Period _timeLimit; // optional

    DirectSale(Item item, Price price, Period timeLimit) {

        requiresItemAndPrice(item, price);
        timeLimitMustBeValid(timeLimit);

        _item = item;
        _price = price;
        _timeLimit = timeLimit;  // may be null = unlimited duration
    }

    public Item getItem() { return _item; }
    public Price getPrice() { return _price; }
    public Period getTimeLimit() { return _timeLimit; }

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

    public boolean isByAuthor (AuthorId authorId) {

        return _item.isByAuthor(authorId);

    }

    public boolean isByPublication (Publication publication) {

        return _item.isByPublication(publication);
    }

    public boolean isByPublisher (PublishingCompany publisher) {

        return _item.isByPublishingCompany(publisher);
    }

    public boolean isByGenre(GenreId genreId) {

        return _item.isByGenre(genreId);

    }

}
