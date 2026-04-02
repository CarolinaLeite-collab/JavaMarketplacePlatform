package TOPSECRET.domain;

import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAuctionRepo {

    Auction createAuction(Item item, Price startingPrice, Price outrightPrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    List<Item> getAuctionItemsByGenre(Genre genre);

    List<Item> getAuctionItemsByAuthor(Author author);

    List<Item> getAuctionItemsByPublication(Publication publication);

    List<Item> getAuctionItemsByPublishingCompany(PublishingCompany publisher);
}
