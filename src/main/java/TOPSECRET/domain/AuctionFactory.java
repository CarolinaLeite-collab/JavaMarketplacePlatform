package TOPSECRET.domain;

import java.time.ZonedDateTime;

/**
 * Factory responsible for creating {@link Auction} instances.
 * <p>
 * This class encapsulates the instantiation logic of {@code Auction},
 * centralizing object creation and isolating clients from constructor
 * details. Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */
public class AuctionFactory {

    /**
     * Creates a new auction without an outright price.
     *
     * @param item             item to auction
     * @param startingPrice    minimum acceptable bid price
     * @param auctionStartDate start date/time of the auction
     * @param auctionEndDate   end date/time of the auction
     * @return created {@link Auction}
     * @throws InstantiationException when auction creation fails
     */
    public Auction create(Item item, Price startingPrice, ZonedDateTime auctionStartDate,
                          ZonedDateTime auctionEndDate) throws InstantiationException {
        try {
            return new Auction(item, startingPrice, auctionStartDate, auctionEndDate);
        } catch (final Exception e) {
            throw new InstantiationException("Unable to instantiate Auction: " + e.getMessage());
        }
    }

    /**
     * Creates a new auction with an optional outright price.
     *
     * @param item             item to auction
     * @param startingPrice    minimum acceptable bid price
     * @param outrightPrice    buy-now price (must be greater than starting price)
     * @param auctionStartDate start date/time of the auction
     * @param auctionEndDate   end date/time of the auction
     * @return created {@link Auction}
     * @throws InstantiationException when auction creation fails
     */
    public Auction create(Item item, Price startingPrice, Price outrightPrice,
                          ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate)
            throws InstantiationException {
        try {
            return new Auction(item, startingPrice, outrightPrice, auctionStartDate, auctionEndDate);
        } catch (final Exception e) {
            throw new InstantiationException("Unable to instantiate Auction: " + e.getMessage());
        }
    }
}
