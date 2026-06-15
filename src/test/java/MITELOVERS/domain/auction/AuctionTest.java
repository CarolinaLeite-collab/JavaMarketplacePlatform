package MITELOVERS.domain.auction;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionTest {
    private List<ItemId> _itemsId;
    private ItemId _itemIdDouble;
    private Price _startingPriceDouble;
    private Price _reservePriceDouble;
    private Price _outrightPriceDouble;
    private Price _offerPriceDouble;
    private ZonedDateTime _auctionStart;
    private ZonedDateTime _auctionEnd;
    private ZonedDateTime _start;
    private ZonedDateTime _end;

    @BeforeEach
    void setUp() {
        _itemIdDouble = mock(ItemId.class);
        _itemsId = new ArrayList<>();
        _itemsId.add(_itemIdDouble);
        _startingPriceDouble = mock(Price.class);
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_startingPriceDouble.getCurrency()).thenReturn(Currency.EUR);
        _reservePriceDouble = mock(Price.class);
        when(_reservePriceDouble.getValue()).thenReturn(50.0);
        when(_reservePriceDouble.getCurrency()).thenReturn(Currency.EUR);
        _outrightPriceDouble = mock(Price.class);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        when(_outrightPriceDouble.getCurrency()).thenReturn(Currency.EUR);
        _offerPriceDouble = mock(Price.class);
        _auctionStart = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
        _auctionEnd = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
        _start = ZonedDateTime.now().plusMinutes(1);
        _end = _start.plusHours(1);
    }

    @Test
    void shouldCreateAuctionWithoutOutrightPrice() {
        // Act
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd);
    }

    @Test
    void shouldCreateAuctionWithOutrightPrice() {
        // Act
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);
    }

    @Test
    void shouldCreateAuctionWhenReservePriceEqualsStartingPrice() {
        // Arrange
        when(_reservePriceDouble.getValue()).thenReturn(10.0);

        // Act
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd);

        // Assert
        assertNotNull(auction);
    }

    @Test
    void shouldCreateAuctionWhenReservePriceHigherStartingPrice() {
        // Arrange
        when(_reservePriceDouble.getValue()).thenReturn(50.0);

        // Act
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd);

        // Assert
        assertNotNull(auction);
    }

    @Test
    void shouldThrowExceptionWhenReservePriceIsLowerThanStartingPrice() {
        // Arrange
        when(_reservePriceDouble.getValue()).thenReturn(5.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        // Arrange
        ZonedDateTime endBefore_start = ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _auctionStart, endBefore_start));
    }

    @Test
    void constructorShouldThrowWhenStartDateIsInvalid() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, null, _auctionEnd));
    }

    @Test
    void createAuctionThrowsExceptionWhenItemListIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Auction(null, _startingPriceDouble, _reservePriceDouble, null, _start, _end));
    }

    @Test
    void createAuctionThrowsExceptionWhenItemListIsEmpty() {
        // Arrange
        List<ItemId> emptyItems = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> new Auction(emptyItems, _startingPriceDouble, _reservePriceDouble, null, _start, _end));
    }

    @Test
    void identityShouldReturnAuctionId() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Act
        AuctionId result = auction.identity();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof AuctionId);
    }

    @Test
    void sameAsShouldReturnTrueWhenAllFieldsAreEqual() {
        // Arrange
        Auction auction1 = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);
        Auction auction2 = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);

        assertTrue(auction1.sameAs(auction2));
    }

    @Test
    void sameAsShouldReturnFalseWhenStartingPriceIsDifferent() {
        // Arrange
        Price otherPrice = mock(Price.class);

        Auction auction1 = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);
        Auction auction2 = new Auction(_itemsId, otherPrice, _reservePriceDouble, _start, _end);

        assertFalse(auction1.sameAs(auction2));
    }

    @Test
    void equalsShouldReturnTrueWhenSameInstance() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Act & Assert
        assertTrue(auction.equals(auction));
    }

    @Test
    void equalsShouldReturnFalseWhenIdsAreDifferent() {
        // Arrange
        Auction auction1 = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);
        Auction auction2 = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Act
        boolean result = auction1.equals(auction2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNull() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Assert
        assertFalse(auction.sameAs(null));
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsDifferentType() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Assert
        assertFalse(auction.equals("not an auction"));
    }

    @Test
    void getShouldReturnItemIdList() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd);

        // Act
        List<ItemId> result = auction.getItemsId();

        // Assert
        assertEquals(_itemsId, result);
    }

    @Test
    void getShouldReturnStartingPrice() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd);

        // Act
        Price result = auction.getStartingPrice();

        // Assert
        assertEquals(_startingPriceDouble, result);
    }

    @Test
    void getShouldReturnOutrightPriceWhenDefined() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        // Act
        Price result = auction.getOutrightPrice();

        // Assert
        assertEquals(_outrightPriceDouble, result);
    }

    @Test
    void getOutrightPriceShouldReturnNullOutrightPriceWhenNotDefined() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, null, _auctionStart, _auctionEnd);

        // Act
        Price result = auction.getOutrightPrice();

        // Assert
        assertNull(result);
    }

    @Test
    void constructorShouldThrowWhenOutrightPriceIsNotGreaterThanStartingPrice() {
        // Arrange
        when(_outrightPriceDouble.getValue()).thenReturn(10.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd));
    }

    @Test
    void placeBidShouldThrowWhenBidCurrencyIsDifferentFromAuctionCurrency() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        // SUT
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, start, end);

        UserId user = mock(UserId.class);

        when(_offerPriceDouble.getValue()).thenReturn(20.0);
        when(_offerPriceDouble.getCurrency()).thenReturn(Currency.USD);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> auction.placeBid(user, _offerPriceDouble));
    }

    @Test
    void placeBidShouldSucceedWhenBidCurrencyIsSameAsAuctionCurrency() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        // SUT
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, start, end);

        UserId user = mock(UserId.class);

        when(_offerPriceDouble.getValue()).thenReturn(20.0);
        when(_offerPriceDouble.getCurrency()).thenReturn(Currency.EUR);

        // Act
        Bid bid = auction.placeBid(user, _offerPriceDouble);

        // Assert
        assertNotNull(bid);
        assertEquals(1, auction.getBids().size());
        assertEquals(bid, auction.getBids().get(0));
    }

    @Test
    void getBidsReturnsNonNullEvenWhenEmpty() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusHours(1);

        // SUT
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        // Assert
        assertNotNull(auction.getBids());
        assertThrows(IllegalStateException.class, auction::getHighestBid);
    }

    @Test
    void getBidsShouldReturnImmutableList() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        UserId user = mock(UserId.class);
        Price price = mock(Price.class);
        when(price.getValue()).thenReturn(20.0);
        when(price.getCurrency()).thenReturn(Currency.EUR);

        auction.placeBid(user, price);

        // Act
        List<Bid> result = auction.getBids();

        // Assert
        assertEquals(1, result.size());
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Bid.class)));
    }

    @Test
    void getHighestBidShouldReturnHighestBid() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        UserId user = mock(UserId.class);
        Price p1 = mock(Price.class);
        Price p2 = mock(Price.class);
        when(p1.getValue()).thenReturn(20.0);
        when(p1.getCurrency()).thenReturn(Currency.EUR);
        when(p2.getValue()).thenReturn(50.0);
        when(p2.getCurrency()).thenReturn(Currency.EUR);


        auction.placeBid(user, p1);
        auction.placeBid(user, p2);

        // Act
        Bid result = auction.getHighestBid();

        // Assert
        assertEquals(50.0, result.getOfferPrice().getValue());
    }

    @Test
    void getHighestBidShouldThrowWhenNoBids() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        // Act & Assert
        assertThrows(IllegalStateException.class, auction::getHighestBid);
    }

    @Test
    void placeBidShouldThrowWhenAuctionNotActive() {
        // Arrange
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd);

        UserId user = mock(UserId.class);
        Price price = mock(Price.class);
        when(price.getValue()).thenReturn(20.0);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> auction.placeBid(user, price));
    }

    @Test
    void placeBidShouldThrowWhenPriceIsTooLow() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        UserId user = mock(UserId.class);
        Price price = mock(Price.class);
        when(price.getValue()).thenReturn(5.0);
        when(price.getCurrency()).thenReturn(Currency.EUR);


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> auction.placeBid(user, price));
    }

    @Test
    void placeBidShouldAddBidWhenValid() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        UserId user = mock(UserId.class);
        Price price = mock(Price.class);
        when(price.getValue()).thenReturn(20.0);
        when(price.getCurrency()).thenReturn(Currency.EUR);


        // Act
        Bid bid = auction.placeBid(user, price);

        // Assert
        assertNotNull(bid);
        assertEquals(1, auction.getBids().size());
    }

    @Test
    void finalizeAuctionShouldNotThrowWhenNoBids() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        // Act & Assert
        assertDoesNotThrow(auction::finalizeAuction);
    }

    @Test
    void finalizeAuctionShouldUseHighestBidWhenBidsExist() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);

        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        UserId user = mock(UserId.class);
        Price low = mock(Price.class);
        Price high = mock(Price.class);
        when(low.getValue()).thenReturn(20.0);
        when(low.getCurrency()).thenReturn(Currency.EUR);
        when(high.getValue()).thenReturn(60.0);
        when(high.getCurrency()).thenReturn(Currency.EUR);
        when(high.isGreaterOrEqualThan(_reservePriceDouble)).thenReturn(true);

        auction.placeBid(user, low);
        auction.placeBid(user, high);

        // Act
        auction.finalizeAuction();

        // Assert
        Bid highest = auction.getHighestBid();
        assertEquals(60.0, highest.getOfferPrice().getValue());
    }

    @Test
    void finalizeAuctionShouldSetWinnerWhenReserveIsMet() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(5);
        Auction auction = new Auction(_itemsId, _startingPriceDouble, _reservePriceDouble, start, end);

        UserId user = mock(UserId.class);
        Price bidPrice = mock(Price.class);
        when(bidPrice.getValue()).thenReturn(100.0);
        when(bidPrice.getCurrency()).thenReturn(Currency.EUR);
        when(bidPrice.isGreaterOrEqualThan(_reservePriceDouble)).thenReturn(true);


        auction.placeBid(user, bidPrice);

        // Act
        auction.finalizeAuction();

        // Assert
        assertEquals(user, auction.getUserId());
        assertEquals(bidPrice, auction.getFinalPrice());
    }
}
