package MITELOVERS.domain.auction;

import MITELOVERS.ddd.AggregateRoot;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.UserId;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a time-bounded selling mechanism where one or more {@link ItemId}s are listed for sale via competitive bidding.
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
    private final List<ItemId> _itemsId;
    private final Price _startingPrice;
    private final Price _reservePrice;
    private final Price _outrightPrice;
    private final ZonedDateTime _auctionStartDate;
    private final ZonedDateTime _auctionEndDate;
    private UserId _buyer;
    private Price _finalPrice;
    private final BidFactory _bidFactory;
    private List<Bid> _bids;
    private UserId _seller;

    Auction(List<ItemId> itemsId, Price startingPrice, Price reservePrice, Price outrightPrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate, UserId seller) {
        _startingPrice = startingPrice;
        _auctionId = new AuctionId();
        _bidFactory = new BidFactory();
        _bids = new ArrayList<>();
        _seller = seller;

        if (isOutrightPriceValid(outrightPrice)) {
            _outrightPrice = outrightPrice;
        } else {
            throw new IllegalArgumentException("Invalid outright price");
        }

        if (isReservePriceValid(reservePrice)) {
            _reservePrice = reservePrice;
        } else {
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

        if (itemsId == null || itemsId.isEmpty()) {
            throw new IllegalArgumentException("Items cannot be null or empty");
        }

        _itemsId = itemsId;
    }

    Auction(List<ItemId> itemsId, Price startingPrice, Price reservePrice, ZonedDateTime auctionStartDate, ZonedDateTime auctionEndDate, UserId seller) {
        this(itemsId, startingPrice, reservePrice, null, auctionStartDate, auctionEndDate, seller);
    }

    Auction(AuctionId auctionId,
            List<ItemId> itemsId,
            Price startingPrice,
            Price reservePrice,
            Price outrightPrice,
            ZonedDateTime auctionStartDate,
            ZonedDateTime auctionEndDate,
            UserId userId,
            Price finalPrice,
            List<Bid> bids,
            UserId seller) {

        if (auctionId == null) {
            throw new IllegalArgumentException("AuctionId cannot be null");
        }

        if (itemsId == null || itemsId.isEmpty()) {
            throw new IllegalArgumentException("Items cannot be null or empty");
        }

        if (startingPrice == null) {
            throw new IllegalArgumentException("Starting price cannot be null");
        }

        if (reservePrice == null) {
            throw new IllegalArgumentException("Reserve price cannot be null");
        }

        if (auctionStartDate == null) {
            throw new IllegalArgumentException("Auction start date cannot be null");
        }

        if (auctionEndDate == null) {
            throw new IllegalArgumentException("Auction end date cannot be null");
        }

        _auctionId = auctionId;
        _itemsId = new ArrayList<>(itemsId);
        _startingPrice = startingPrice;
        _reservePrice = reservePrice;
        _outrightPrice = outrightPrice;
        _auctionStartDate = auctionStartDate;
        _auctionEndDate = auctionEndDate;
        _buyer = userId;
        _finalPrice = finalPrice;
        _bids = (bids == null) ? new ArrayList<>() : new ArrayList<>(bids);
        _bidFactory = new BidFactory();
        _seller = seller;
    }

    @Override
    public AuctionId identity() {
        return _auctionId;
    }

    @Override
    public boolean sameAs(Object object) {
        if (object instanceof Auction) {
            Auction other = (Auction) object;

            if (Objects.equals(_itemsId, other._itemsId) &&
                    Objects.equals(_startingPrice, other._startingPrice) &&
                    Objects.equals(_reservePrice, other._reservePrice) &&
                    Objects.equals(_outrightPrice, other._outrightPrice) &&
                    Objects.equals(_auctionStartDate, other._auctionStartDate) &&
                    Objects.equals(_auctionEndDate, other._auctionEndDate) &&
                    Objects.equals(_seller, other._seller)
            )
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Auction other)) return false;
        return _auctionId.equals(other._auctionId);
    }

    public List<ItemId> getItemsId() {
        return _itemsId;
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

    public UserId getUserId() {
        return _buyer;
    }

    public UserId getSeller() {
        return _seller;
    }

    public Price getFinalPrice() {
        return _finalPrice;
    }

    public Price getReservePrice() {
        return _reservePrice;
    }

    public Instant getAuctionStartDate() {
        return  _auctionStartDate.toInstant();
    }

    public Instant getAuctionEndDate() {
        return  _auctionEndDate.toInstant();
    }

    public void finalizeAuction() {
        if (_bids.isEmpty()) {
            _buyer = null;
            _finalPrice = null;
            return;
        }

        Bid highestBid = getHighestBid();

        if (isReserveMet(highestBid.getOfferPrice())) {
            _buyer = highestBid.getUserId();
            _finalPrice = highestBid.getOfferPrice();
        } else {
            _buyer = null;
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
        return reservePrice.getValue() >= _startingPrice.getValue();
    }

    private boolean isReserveMet(Price price) {
        return price.isGreaterOrEqualThan(_reservePrice);
    }

    private boolean isAuctionEndDateValid(ZonedDateTime auctionEndDate) {
        return auctionEndDate.isAfter(_auctionStartDate);
    }

    public Bid placeBid(UserId userId, Price offerPrice) {
        ZonedDateTime now = ZonedDateTime.now();

        if (now.isBefore(_auctionStartDate) || now.isAfter(_auctionEndDate)) {
            throw new IllegalStateException("Auction not active");
        }

        if (offerPrice == null) {
            throw new IllegalArgumentException("Offer price must not be null");
        }

        if (!offerPrice.getCurrency().equals(_startingPrice.getCurrency())) {
            throw new IllegalArgumentException("Bid currency must match auction currency");
        }

        Price minimumAcceptedPrice = _bids.isEmpty()
                ? _startingPrice
                : getHighestBid().getOfferPrice();

        if (offerPrice.getValue() <= minimumAcceptedPrice.getValue()) {
            throw new IllegalArgumentException(
                    _bids.isEmpty()
                            ? "Bid must be higher than starting price"
                            : "Bid must be higher than current highest bid"
            );
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
