package TOPSECRET.domain;

import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;

import java.time.ZonedDateTime;

/**
 * Represents a time-bounded selling mechanism where a {@link Item} is sold via competitive bidding.
 * <p>
 * An auction is active only within its configured time window: {@code auctionStartDate}
 * to {@code auctionEndDate}.
 * Bids placed during the active period determine the final sale price and winning buyer.
 * </p>
 *
 * <h2>Seller-defined parameters</h2>
 * <ul>
 *   <li><b>startingPrice</b>: minimum price required for the first valid bid.</li>
 *   <li><b>outrightPrice</b> (optional): a "buy now" price that allows immediate purchase without waiting
 *       for the auction to end.</li>
 *   <li><b>auctionStartDate</b>: date/time when the auction becomes active.</li>
 *   <li><b>auctionEndDate</b>: date/time when the auction closes.</li>
 * </ul>
 *
 * <h2>Business rules</h2>
 * <ul>
 *   <li>Items cannot be simultaneously on direct sale (e.g., shopping cart) and on auction.</li>
 * </ul>
 */

public class Auction {

    private final Item _item;
    private final Price _startingPrice;
    private final Price _reservePrice;
    private final Price _outrightPrice;
    private final ZonedDateTime _auctionStartDate;
    private final ZonedDateTime _auctionEndDate;
    private final MemoBidRepo _bids;
    private User _buyer;
    private Price _finalPrice;


    Auction(Item item, Price startingPrice, Price reservePrice, Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        _item = item;
        _startingPrice = startingPrice;
        _bids = new MemoBidRepo( new BidFactory());


        if (isOutrightPriceValid(outrightPrice)) {
            _outrightPrice = outrightPrice;
        } else {
            throw new IllegalArgumentException("Invalid outright price");
        }

        if (isReservePriceValid(reservePrice)) {
            _reservePrice = reservePrice;
        }  else {
            throw new IllegalArgumentException("Invalid reserve price");
        }

        if (isAuctionStartDateValid(auctionStartDate)) {
            _auctionStartDate = auctionStartDate;
        } else {
            throw new IllegalArgumentException("Invalid start date");
        }

        if (isAuctionEndDateValid(auctionEndDate)) {
            _auctionEndDate = auctionEndDate;
        } else {
            throw new IllegalArgumentException("Invalid end date");
        }

        _item.setAuction(this);
    }

    Auction(Item item, Price startingPrice, Price reservePrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        this (item, startingPrice, reservePrice, null, auctionStartDate, auctionEndDate);
    }

    public Item getItem() {
        return _item;
    }

    public MemoBidRepo getBids() {
        return _bids;
    }

    public Price getStartingPrice() {
        return _startingPrice;
    }

    public Price getOutrightPrice() {
        return _outrightPrice;
    }


    public void acceptBid(Bid bid) {
        ZonedDateTime now = ZonedDateTime.now();
        if (now.isAfter(_auctionStartDate) && now.isBefore(_auctionEndDate) && bid.getOfferPrice().getValue() > _startingPrice.getValue()) {
            _bids.addBid(bid);
            if (_outrightPrice != null && bid.getOfferPrice().getValue() >= _outrightPrice.getValue()) {
                finalizeAuction();
            }
        } else {
            throw new IllegalArgumentException("Invalid Bid");
        }
    }

    public void finalizeAuction() {
        Bid highestBid = _bids.getHighestBid();

        if (isReserveMet(highestBid.getOfferPrice())) {
            _buyer = highestBid.getBidder();
            _finalPrice = highestBid.getOfferPrice();
        } else {
            _buyer = null;
            _finalPrice = null;
        }
    }

    private boolean isAuctionStartDateValid(ZonedDateTime auctionStartDate) {
        boolean result = false;
        ZonedDateTime now = ZonedDateTime.now();
        if (now.isBefore(auctionStartDate)) {
            result = true;
        }
        return result;
    }

    private boolean isOutrightPriceValid(Price outrightPrice) {
        if (outrightPrice == null) return true;
        return outrightPrice.getValue() > _startingPrice.getValue();
    }

    private boolean isReservePriceValid(Price reservePrice) {
        boolean result = false;
        if (reservePrice.getValue() >= _startingPrice.getValue()) {
            result = true;
        }
        return result;
    }

    private boolean isReserveMet (Price price) {
        return price.isGreaterOrEqualThan(_reservePrice);
    }

    private boolean isAuctionEndDateValid(ZonedDateTime auctionEndDate) {
        boolean result = false;
        if (auctionEndDate.isAfter(_auctionStartDate)) {
            result = true;
        }
        return result;
    }

    public boolean isByGenre( GenreId genreId) {
        return _item.isByGenre(genreId);
    }

    public boolean isByAuthor(AuthorId authorId) {

        return _item.isByAuthor(authorId);

    }

    public boolean isByPublication(Publication publication) {

        return _item.isByPublication(publication);

    }

    public boolean isByPublishingCompany(PublishingCompany publisher) {

        return _item.isByPublishingCompany(publisher);

    }
}
