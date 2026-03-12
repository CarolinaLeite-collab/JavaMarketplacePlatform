package TOPSECRET.domain;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Repository class responsible for managing auctions within the system.
 * <p>
 * The {@code AuctionRepo} class provides methods to create new auctions
 * and to retrieve auction items that match a given genre.
 * Internally, it maintains an in-memory list of ongoing auctions.
 * </p>
 */

public class AuctionRepo {

    private final List<Auction> itemsOnAuction;
    private final AuctionFactory auctionFactory;

    public AuctionRepo() {
        this(new AuctionFactory());
    }


    AuctionRepo(AuctionFactory auctionFactory) {
        this.itemsOnAuction = new ArrayList<>();
        this.auctionFactory = Objects.requireNonNull(auctionFactory, "auctionFactory must not be null");
    }

    public Auction createAuction(Item item, Price startingPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

        try {
            Auction auction = auctionFactory.createAuction(item, startingPrice, auctionStartDate, auctionEndDate);
            itemsOnAuction.add(auction);
            return auction;
        } catch (InstantiationException ex) {
            if (ex.getCause() instanceof IllegalStateException) {
                throw (IllegalStateException) ex.getCause();
            }
            throw new IllegalArgumentException("Unable to create auction: " + ex.getMessage(), ex);
        }
    }

    public Auction createAuction(Item item, Price startingPrice, Price outrightPrice,
                                 ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        try {
            Auction auction = auctionFactory.createAuction(item, startingPrice, outrightPrice, auctionStartDate, auctionEndDate);
            itemsOnAuction.add(auction);
            return auction;
        } catch (InstantiationException ex) {
            if (ex.getCause() instanceof IllegalStateException) {
                throw (IllegalStateException) ex.getCause();
            }
            throw new IllegalArgumentException("Unable to create auction: " + ex.getMessage(), ex);
        }
    }

    public List<Item> getAuctionItemsByGenre(Genre genre) {


        List<Item> listOfAuctionItemsByGenre = new ArrayList<>();

        for (Auction auction : itemsOnAuction) {


            if (auction.isByGenre(genre)) {
                listOfAuctionItemsByGenre.add(auction.getItem());
            }
        }
        List<Item> copyOfListOfAuctionItemsByGenre =
                new ArrayList<>(listOfAuctionItemsByGenre);

        return copyOfListOfAuctionItemsByGenre;
    }

    public List<Item> getAuctionItemsByAuthor(Author author) {
        List<Item> listOfAuctionItemsByAuthor = new ArrayList<>();
        for (Auction auction : itemsOnAuction) {
            if (auction.isByAuthor(author)) {
                listOfAuctionItemsByAuthor.add(auction.getItem());
            }
        }
        return new ArrayList<>(listOfAuctionItemsByAuthor);
    }

    public List<Item> getAuctionItemsByPublication(Publication publication) {

        List<Item> listOfAuctionItemsByPublication = new ArrayList<>();

        for (Auction auction : itemsOnAuction) {
            if(auction.isByPublication(publication)) {
                listOfAuctionItemsByPublication.add(auction.getItem());
            }
        }
        List<Item> copyOfListOfAuctionItemsByPublication =
                new ArrayList<>(listOfAuctionItemsByPublication);

        return copyOfListOfAuctionItemsByPublication;
    }

    public List<Item> getAuctionItemsByPublishingCompany(PublishingCompany publisher) {

        List<Item> listOfAuctionItemsByPublisher = new ArrayList<>();

        for (Auction auction : itemsOnAuction) {
            if (auction.isByPublishingCompany(publisher)){
                listOfAuctionItemsByPublisher.add(auction.getItem());
            }
        }

        return new ArrayList<>(listOfAuctionItemsByPublisher);
    }

}
