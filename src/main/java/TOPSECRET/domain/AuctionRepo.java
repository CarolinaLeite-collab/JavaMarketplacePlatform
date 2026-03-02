package TOPSECRET.domain;
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

public class AuctionRepo {

    private List<Auction> itemsOnAuction = new ArrayList<>();

    /**
     * Creates a new auction for a specific item.
     *
     * @param item             The item to be auctioned.
     * @param startingPrice    The initial price at which the auction begins.
     * @param auctionStartDate The start date and time of the auction.
     * @param auctionEndDate   The end date and time of the auction.
     * @return The newly created {@link Auction} instance.
     */

    public Auction createAuction(Item item, Price startingPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {

        Auction auction = new Auction(item, startingPrice, auctionStartDate, auctionEndDate);

        itemsOnAuction.add(auction);

        return auction;
    }

    /**
     * Retrieves a list of items currently on auction that belong to a specific genre.
     * <p>
     * This method creates and returns a copy of the list to preserve encapsulation
     * and prevent external modifications to the internal repository state.
     * </p>
     *
     * @param genre The genre by which to filter auction items.
     * @return A new list containing items that match the given genre.
     */

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
    /**
     * Retrieves a list of items currently on auction for a specific author.
     * Returns a copy to preserve encapsulation.
     */
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

    /**
     * Retrieves a list of items currently on auction that belong to a specific publisher.
     * <p>
     * This method creates and returns a copy of the list to preserve encapsulation
     * and prevent external modifications to the internal repository state.
     * </p>
     * @param publisher The publisher by which to filter auction items.
     * @return A new list containing items that match the given publisher.
     */

    public List<Item> getAuctionItemsByPublisher(PublishingCompany publisher) {

        List<Item> listOfAuctionItemsByPublisher = new ArrayList<>();

        for (Auction auction : itemsOnAuction) {
            if (auction.isByPublisher(publisher)){
                listOfAuctionItemsByPublisher.add(auction.getItem());
            }
        }

        return new ArrayList<>(listOfAuctionItemsByPublisher);
    }

}
