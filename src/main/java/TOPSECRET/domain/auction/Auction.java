package TOPSECRET.domain.auction;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a time-bounded selling mechanism where one or more {@link Item}s is sold via competitive bidding.
 * <p>
 * An auction is active only within its configured time window: {@code auctionStartDate}
 * to {@code auctionEndDate}.
 * Bids placed during the active period determine the final sale price and winning buyer.
 * </p>
 *
 * <h2>Seller-defined parameters</h2>
 * <ul>
 *   <li><b>startingPrice</b>: minimum price required for the first valid bid.</li>
 *   <li><b>reservePrice</b>: minimum acceptable price for the item to be sold; bids below this
 *       price do not result in a sale.</li>
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

public class Auction implements AggregateRoot<AuctionId> {

    private final AuctionId _auctionId;
    private final List<Item> _items;
    private final Price _startingPrice;
    private final Price _reservePrice;
    private final Price _outrightPrice;
    private final ZonedDateTime _auctionStartDate;
    private final ZonedDateTime _auctionEndDate;
    private UserId _userId;
    private Price _finalPrice;
    private final BidFactory _bidFactory;
    private List<Bid> _bids;



    Auction(List<Item> items, Price startingPrice, Price reservePrice, Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        _startingPrice = startingPrice;
        _auctionId = new AuctionId();
        _bidFactory = new BidFactory();
        _bids = new ArrayList<>();

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

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items cannot be null or empty");
        }

        for (Item item : items) {
            if (item.get_saleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException("Item is already on sale.");
            }
        }
            _items = items;

        for (Item item : _items) {
            item.markAsAuction();
        }
    }

    Auction(List<Item> item, Price startingPrice, Price reservePrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate) {
        this (item, startingPrice, reservePrice, null, auctionStartDate, auctionEndDate);
    }

    @Override
    public AuctionId identity() {
        return _auctionId;
    }

    @Override
    public boolean sameAs(Object object) {
        if (!(object instanceof Auction other)) return false;
        return _auctionId.equals(other._auctionId);
    }

    public List <Item> getItems() {
        return _items;
    }

    public List<Bid> getBids() {
        return List.copyOf(_bids);
    }

    public Price getStartingPrice() {
        return _startingPrice;
    }

    public Price getOutrightPrice() {
        return _outrightPrice;
    }



    public void finalizeAuction() {
        if (_bids.isEmpty()) {
            _userId = null;
            _finalPrice = null;
            return;
        }

        Bid highestBid = getHighestBid();

        if (isReserveMet(highestBid.getOfferPrice())) {
            _userId = highestBid.getUserId();
            _finalPrice = highestBid.getOfferPrice();
        } else {
            _userId = null;
            _finalPrice = null;
        }
    }

    private boolean isAuctionStartDateValid(ZonedDateTime auctionStartDate) {
        return auctionStartDate != null;
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

    private boolean isReserveMet(Price price) {
        return price.isGreaterOrEqualThan(_reservePrice);
    }

    private boolean isAuctionEndDateValid(ZonedDateTime auctionEndDate) {
        boolean result = false;
        if (auctionEndDate.isAfter(_auctionStartDate)) {
            result = true;
        }
        return result;
    }

    public Bid placeBid (UserId userId, Price offerPrice){
        ZonedDateTime now =  ZonedDateTime.now();

        if (now.isBefore(_auctionStartDate) || now.isAfter(_auctionEndDate)) {
            throw new IllegalStateException("Auction not active");
        }

        if (offerPrice.getValue() <= _startingPrice.getValue()) {
            throw new IllegalArgumentException("Bid must be higher than starting price");
        }

        Bid bid = Objects.requireNonNull(
                _bidFactory.createBid(userId, offerPrice), "Bid must not be null"
        );

        _bids.add(bid);

        if (_outrightPrice != null &&
                offerPrice.getValue() >= _outrightPrice.getValue()) {
            finalizeAuction();
        }

        return bid;
    }

    public Bid getHighestBid() {

        if (_bids.isEmpty()) {
            throw new IllegalStateException("No bids available");
        }

        Bid highestBid = _bids.get(0);

        for (int i = 1; i < _bids.size(); i++) {
            Bid anotherBid = _bids.get(i);
            if (anotherBid.getOfferPrice().getValue() > highestBid.getOfferPrice().getValue()) {
                highestBid = anotherBid;
            }
        }
        return highestBid;
    }
}
