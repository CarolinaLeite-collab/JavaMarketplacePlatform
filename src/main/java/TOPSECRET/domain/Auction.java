package TOPSECRET.domain;

import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.Author.Author;
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

    private Item _item;
    private Price _startingPrice;
    private Price _outrightPrice;
    private Price _finalPrice;
    private ZonedDateTime _auctionStartDate;
    private ZonedDateTime _auctionEndDate;
    private User _buyer;
    private MemoBidRepo _bids;
    private BidFactory _bidFactory;


    Auction(Item item, Price startingPrice, Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        _item = item;
        _startingPrice = startingPrice;
        _bids = new MemoBidRepo( new BidFactory());


        if (isOutrightPriceValid(outrightPrice)) {
            _outrightPrice = outrightPrice;
        } else {
            throw new IllegalArgumentException("Invalid outright price");
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

    Auction(Item item, Price startingPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        _item = item;
        _startingPrice = startingPrice;
        _outrightPrice = null;
        _bids = new MemoBidRepo(new BidFactory());

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

    public Item getItem() {
        return _item;
    }

    public MemoBidRepo getBids() {
        return _bids;
    }


    public void acceptBid(Bid bid) {
        ZonedDateTime now = ZonedDateTime.now();
        if (now.isAfter(_auctionStartDate) && now.isBefore(_auctionEndDate) && bid.getOfferPrice().getValue() > _startingPrice.getValue()) {
            _bids.addBid(bid);
        } else {
            throw new IllegalArgumentException("Invalid Bid");
        }
    }

    public void finalizeAuction() {
        Bid highestBid = _bids.getHighestBid();
        _buyer = highestBid.getBidder();
        _finalPrice = highestBid.getOfferPrice();
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
        boolean result = false;
        if (outrightPrice.getValue() > _startingPrice.getValue()) {
            result = true;
        }
        return result;
    }

    private boolean isAuctionEndDateValid(ZonedDateTime auctionEndDate) {
        boolean result = false;
        if (auctionEndDate.isAfter(_auctionStartDate)) {
            result = true;
        }
        return result;
    }

    public boolean isByGenre( Genre genre) {
        return _item.isByGenre(genre);
    }

    public boolean isByAuthor(Author author) {

        return _item.isByAuthor(author);

    }

    public boolean isByPublication(Publication publication) {

        return _item.isByPublication(publication);

    }

    public boolean isByPublishingCompany(PublishingCompany publisher) {

        return _item.isByPublishingCompany(publisher);

    }
}
