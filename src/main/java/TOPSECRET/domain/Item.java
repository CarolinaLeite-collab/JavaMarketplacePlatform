package TOPSECRET.domain;

import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.GenreId;

import java.util.Objects;

/**
 * <h3>Item represents a publication that has been listed for sale.</h3>
 * <p>
 * An {@code Item} wraps a {@link Publication} and captures its sale context,
 * including its {@link Condition} and the type of sale it belongs to .
 * Each item can only be part of either a {@link DirectSale} or an {@link Auction}, but never both.
 * </p>
 *
 * <p>
 * This class enforces mutual exclusivity between direct sales and auctions
 * to maintain domain consistency. Attempting to assign both types of sale will
 * result in an {@link IllegalStateException}.
 * </p>
 */

public class Item {

    private final Publication _publication;
    private final Condition _condition;
    private DirectSale directSale;
    private Auction auction;

    Item(Publication publication, Condition condition) {
        _publication = publication;
        _condition = condition;
    }

    public Publication get_publication() {
        return _publication;
    }

    public Condition get_condition() {
        return _condition;
    }

    public void setDirectSale(DirectSale directSale) {
        if (this.auction != null) {
            throw new IllegalStateException("Item is already in an auction.");
        }
        if (directSale.getItem() != this) {
            throw new IllegalArgumentException("This DirectSale does not belong to this Item.");
        }
        this.directSale = directSale;
    }

    public void setAuction(Auction auction) {
        if (this.directSale != null) {
            throw new IllegalStateException("Item is already in a direct sale.");
        }
        if (!auction.getItems().contains(this)) {
            throw new IllegalArgumentException("This Auction does not belong to this Item.");
        }
        this.auction = auction;
    }

    public DirectSale getDirectSale() {
        return directSale;
    }

    public Auction getAuction() {
        return auction;

    }

    public boolean isByAuthor(AuthorId authorId) {

        return _publication.isByAuthor(authorId);

    }

    public boolean isByGenre(GenreId genreId) {

        return _publication.isByGenre(genreId);

    }

    /** @deprecated filtering by publishing company moves to Edition — remove when Item references Edition */
    @Deprecated
    public boolean isByPublishingCompany( PublishingCompany publisher) {

        return false;
    }

    public boolean isByPublication(Publication publication) {

        return publication.equals(this._publication);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Item other)) return false;

        return Objects.equals(_publication, other._publication)
                && _condition == other._condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_publication, _condition);
    }
}
