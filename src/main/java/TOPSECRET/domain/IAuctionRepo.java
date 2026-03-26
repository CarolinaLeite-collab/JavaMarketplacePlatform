package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAuctionRepo {

    public Auction createAuction(Item item, Price startingPrice, Price outrightPrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    public List<Item> getAuctionItemsByGenre(Genre genre);

    public List<Item> getAuctionItemsByAuthor(Author author);

    public List<Item> getAuctionItemsByPublication(Publication publication);

    public List<Item> getAuctionItemsByPublishingCompany(PublishingCompany publisher);
}
