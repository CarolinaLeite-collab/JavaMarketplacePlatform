package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAuctionRepo {

    Auction addAuction(AuctionId auctionId, List<Item> item, Price startingPrice, Price reservePrice, Price outrightPrice,
                       ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    Auction addAuction(AuctionId auctionId, List<Item> item, Price startingPrice, Price reservePrice,
                       ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    List<Item> getAuctionItemsByGenre(GenreId genreId);

    List<Item> getAuctionItemsByAuthor(AuthorId authorId);

    List<Item> getAuctionItemsByPublication(Publication publication);

    List<Item> getAuctionItemsByPublishingCompany(PublishingCompany publisher);
}
