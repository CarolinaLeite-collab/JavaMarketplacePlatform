package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.auction.AuctionFactory;
import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.valueobject.PublishingCompanyId;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Repository class responsible for managing auctions within the system.
 * <p>
 * The {@code AuctionRepo} class provides methods to create new auctions
 * and to retrieve auction items that match a given genre.
 * Internally, it maintains an in-memory list of ongoing auctions.
 * </p>
 */

public class MemoAuctionRepo implements IAuctionRepo {

    private final Map<AuctionId, Auction> DATA;
    private final AuctionFactory _auctionFactory;

    public MemoAuctionRepo() {
        this(new AuctionFactory());
    }
    MemoAuctionRepo(AuctionFactory auctionFactory) {
        DATA = new HashMap<AuctionId, Auction>();
        _auctionFactory = auctionFactory;
    }

    @Override
    public Auction save(Auction auction) {
        DATA.put(auction.identity(), auction);
        return auction;
    }

    @Override
    public Iterable<Auction> findAll() {
        return DATA.values();
    }

    @Override
    public Optional<Auction> ofIdentity(AuctionId id) {
        if(!containsOfIdentity(id)) {

            return Optional.empty();

        } else {

            return Optional.of(DATA.get(id));

        }
    }

    @Override
    public boolean containsOfIdentity(AuctionId id) {
        return DATA.containsKey(id);
    }

    @Override
    public Auction addAuction(List<Item> itemsOnAuction, Price startingPrice, Price reservePrice,
                              Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

        Auction auction = _auctionFactory.createAuction(itemsOnAuction, startingPrice, reservePrice,
                outrightPrice, auctionStartDate, auctionEndDate);

        return save(auction);
    }

    @Override
    public Auction addAuction(List<Item> itemsOnAuction, Price startingPrice, Price reservePrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        return addAuction(itemsOnAuction, startingPrice, reservePrice, null, auctionStartDate, auctionEndDate);
    }

    @Override
    public List<Item> getAuctionItemsByGenreId(GenreId genreId) {

        List<Item> listOfAuctionItemsByGenre = new ArrayList<>();

        for (Auction auction : findAll()) {
            if (auction.isByGenreId(genreId)) {
                listOfAuctionItemsByGenre.addAll(auction.getItems());
            }
        }
        return new ArrayList<>(listOfAuctionItemsByGenre);
    }

    @Override
    public List<Item> getAuctionItemsByAuthorId(AuthorId authorId) {

        List<Item> listOfAuctionItemsByAuthor = new ArrayList<>();

        for (Auction auction : findAll()) {
            if (auction.isByAuthorId(authorId)) {
                listOfAuctionItemsByAuthor.addAll(auction.getItems());
            }
        }
        return new ArrayList<>(listOfAuctionItemsByAuthor);
    }

    @Override
    public List<Item> getAuctionItemsByPublicationId(Publication publication) {

        List<Item> listOfAuctionItemsByPublication = new ArrayList<>();

        for (Auction auction : findAll()) {
            if(auction.isByPublication(publication)) {
                listOfAuctionItemsByPublication.addAll(auction.getItems());
            }
        }
        return new ArrayList<>(listOfAuctionItemsByPublication);
    }

    @Override
    public List<Item> getAuctionItemsByPublishingCompanyId(PublishingCompanyId publishingCompanyId) {

        List<Item> listOfAuctionItemsByPublisher = new ArrayList<>();

        for (Auction auction : findAll()) {
            if (auction.isByPublishingCompany(publishingCompanyId)){
                listOfAuctionItemsByPublisher.addAll(auction.getItems());
            }
        }
        return new ArrayList<>(listOfAuctionItemsByPublisher);
    }
}
