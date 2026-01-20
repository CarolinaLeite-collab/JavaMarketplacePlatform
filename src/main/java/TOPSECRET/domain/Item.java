package TOPSECRET.domain;

public class Item {

    private Publication publication;
    private Condition condition;
    private DirectSale directSale;
    private Auction auction;

    public Item(Publication publication, Condition condition) {
        this.publication = publication;
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setDirectSale(DirectSale directSale) {
        if (this.auction != null) {
            throw new IllegalStateException("Item is already in an auction.");
        }
        this.directSale = directSale;
    }

    public void setAuction(Auction auction) {
        if (this.directSale != null) {
            throw new IllegalStateException("Item is already in a direct sale.");
        }
        this.auction = auction;
    }
}
