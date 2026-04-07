package TOPSECRET.domain;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing auctions within the system.
 * <p>
 * The {@code AuctionRepo} class provides methods to create new auctions
 * and to retrieve auction items that match a given genre.
 * Internally, it maintains an in-memory list of ongoing auctions.
 * </p>
 */

public class MemoAuctionRepo implements IAuctionRepo {

    private final List<Auction> _itemsOnAuction;
    private final AuctionFactory _auctionFactory;

    public MemoAuctionRepo() {
        this(new AuctionFactory());
    }


    MemoAuctionRepo(AuctionFactory auctionFactory) {
        _itemsOnAuction = new ArrayList<>();
        _auctionFactory = auctionFactory;
    }

    @Override
    public Auction createAuction(AuctionId auctionId, List<Item> item, Price startingPrice, Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

            Auction auction = _auctionFactory.createAuction(auctionId, item, startingPrice, outrightPrice,  auctionStartDate, auctionEndDate);
            _itemsOnAuction.add(auction);
            return auction;
    }

    @Override
    public List<Item> getAuctionItemsByGenre(Genre genre) {

        List<Item> listOfAuctionItemsByGenre = new ArrayList<>();

        for (Auction auction : _itemsOnAuction) {
            if (auction.isByGenre(genre)) {
                listOfAuctionItemsByGenre.addAll(auction.getItems());
            }
        }
        List<Item> copyOfListOfAuctionItemsByGenre =
                new ArrayList<>(listOfAuctionItemsByGenre);

        return copyOfListOfAuctionItemsByGenre;
    }

    @Override
    public List<Item> getAuctionItemsByAuthor(Author author) {
        List<Item> listOfAuctionItemsByAuthor = new ArrayList<>();
        for (Auction auction : _itemsOnAuction) {
            if (auction.isByAuthor(author)) {
                listOfAuctionItemsByAuthor.addAll(auction.getItems());
            }
        }
        return new ArrayList<>(listOfAuctionItemsByAuthor);
    }

    @Override
    public List<Item> getAuctionItemsByPublication(Publication publication) {

        List<Item> listOfAuctionItemsByPublication = new ArrayList<>();

        for (Auction auction : _itemsOnAuction) {
            if(auction.isByPublication(publication)) {
                listOfAuctionItemsByPublication.addAll(auction.getItems());
            }
        }
        List<Item> copyOfListOfAuctionItemsByPublication =
                new ArrayList<>(listOfAuctionItemsByPublication);

        return copyOfListOfAuctionItemsByPublication;
    }

    @Override
    public List<Item> getAuctionItemsByPublishingCompany(PublishingCompany publisher) {

        List<Item> listOfAuctionItemsByPublisher = new ArrayList<>();

        for (Auction auction : _itemsOnAuction) {
            if (auction.isByPublishingCompany(publisher)){
                listOfAuctionItemsByPublisher.addAll(auction.getItems());
            }
        }

        return new ArrayList<>(listOfAuctionItemsByPublisher);
    }

}
