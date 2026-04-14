package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;
import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.publication.Publication;

/**
 * Represents the unique identifier of an {@link TOPSECRET.domain.item.Item}.
 *
 * <p>
 * An {@code ItemId} is a value object that encapsulates a {@link SKU},
 * ensuring that each Item in the system has a unique and immutable identity.
 * </p>
 *
 * <p>
 * The identity is generated upon creation and is based on a unique SKU value.
 * Two {@code ItemId} instances are considered equal if their underlying
 * {@link SKU} values are equal.
 * </p>
 *
 * <p>
 * This class follows value object semantics:
 * <ul>
 *   <li>it is immutable</li>
 *   <li>equality is based on its internal state ({@link SKU})</li>
 *   <li>it provides consistent {@code equals} and {@code hashCode} implementations</li>
 * </ul>
 * </p>
 */
public final class ItemId implements DomainId {

    private final SKU _sku;
    private Auction _auction;
    private DirectSale _directSale;
    private Publication _publication;

    public ItemId() {

        _sku = new SKU();
    }

    public SKU getSku() { return _sku; }

    public void setAuction(Auction auction) { _auction = auction; }

    public Auction getAuction() { return _auction; }

    public void setDirectSale(DirectSale directSale) { _directSale = directSale; }

    public DirectSale getDirectSale() { return _directSale; }

    public void setPublication(Publication publication) { _publication = publication; }

    public Publication get_publication() { return _publication; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemId other)) return false;
        return _sku.equals(other._sku);
    }

    @Override
    public String toString() {
        return _sku.toString();
    }

    @Override
    public int hashCode() {
        return _sku.hashCode();
    }
}
