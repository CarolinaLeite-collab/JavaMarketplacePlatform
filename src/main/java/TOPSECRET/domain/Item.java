package TOPSECRET.domain;

public class Item {

    private final Publication publication;
    private final Condition condition;
    private DirectSale directSale;
    private Auction auction;

    public Item(Publication publication, Condition condition) {
        this.publication = publication;
        this.condition = condition;
    }

    public Publication getPublication() {
        return publication;
    }
    public Condition getCondition() {
        return condition;
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
        if (auction.getItem() != this) {
            throw new IllegalArgumentException("This Auction does not belong to this Item.");
        }
        this.auction = auction;
    }
}
