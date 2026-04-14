package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.valueobject.PublishingCompanyId;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.List;

public interface IAuctionRepo extends IRepository<AuctionId, Auction> {

    Auction addAuction(List<Item> item, Price startingPrice, Price reservePrice, Price outrightPrice,
                       ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    Auction addAuction(List<Item> item, Price startingPrice, Price reservePrice,
                       ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate);

    List<Item> getAuctionItemsByGenreId(GenreId genreId);

    List<Item> getAuctionItemsByAuthorId(AuthorId authorId);

    List<Item> getAuctionItemsByPublicationId(Publication publication);

    List<Item> getAuctionItemsByPublishingCompanyId(PublishingCompanyId publisherId);
}