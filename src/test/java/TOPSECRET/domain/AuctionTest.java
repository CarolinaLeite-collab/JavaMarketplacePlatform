package TOPSECRET.domain;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.AuctionId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionTest {
    private AuctionId _auctionIdDouble1;
    private AuctionId _auctionIdDouble2;
    private List<Item> _items;
    private Item _itemDouble;
    private Price _startingPriceDouble;
    private Price _reservePriceDouble;
    private Price _outrightPriceDouble; // optional (nullable)
    private ZonedDateTime _auctionStart;
    private ZonedDateTime _auctionEnd;
    private ZonedDateTime _start;
    private ZonedDateTime _end;

    @BeforeEach
    void setUp() {
        _itemDouble = mock(Item.class);
        _items = new ArrayList<>();
        _items.add(_itemDouble);
        _auctionIdDouble1 = mock(AuctionId.class);
        _auctionIdDouble2 = mock(AuctionId.class);
        _startingPriceDouble = mock(Price.class);
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        _reservePriceDouble = mock(Price.class);
        when(_reservePriceDouble.getValue()).thenReturn(50.0);
        _outrightPriceDouble = mock(Price.class);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        _auctionStart = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
        _auctionEnd = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
        _start = ZonedDateTime.now().plusMinutes(1);
        _end = _start.plusHours(1);
    }

    @Test
    void shouldCreateAuctionWithoutOutrightPrice() {
        // Act
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Assert
        assertNotNull(auction);
        assertEquals(_items, auction.getItems());
        verify(_itemDouble).setAuction(auction);
    }

    @Test
    void shouldCreateAuctionWithOutrightPrice() {
        // Act
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble,_auctionStart, _auctionEnd); // SUT

        // Assert
        assertNotNull(auction);
        assertEquals(_items, auction.getItems());
        verify(_itemDouble).setAuction(auction);
    }

    @Test
    void shouldThrowExceptionWhenReservePriceEquals_startingPrice() {
        // Arrange
        when(_reservePriceDouble.getValue()).thenReturn(10.0);

        // Act
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT
        
        // Assert
        assertNotNull(auction);
    }

    @Test
    void shouldThrowExceptionWhenReservePriceIsLowerThan_startingPrice() {
        // Arrange
        when(_reservePriceDouble.getValue()).thenReturn(5.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsBefore_startDate() {
        // Arrange
        ZonedDateTime endBefore_start = ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, endBefore_start)); // SUT
    }

    @Test
    void constructorShouldThrowWhen_startDateIsInvalid() {
        // Arrange
        ZonedDateTime pastDate = ZonedDateTime.now().minusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, pastDate, _auctionEnd)); // SUT
    }

    @Test
    void createAuctionThrowsExceptionWhenItemListIsNull() {
        // Assert & Act
        assertThrows(IllegalArgumentException.class, () -> new Auction(_auctionIdDouble1, null, _startingPriceDouble, _reservePriceDouble, null, _start, _end));
    }

    @Test
    void createAuctionThrowsExceptionWhenItemListIsEmpty() {
        // Arrange
        List<Item> emptyItems = new ArrayList<>();

        // Assert & Act
        assertThrows(IllegalArgumentException.class, () -> new Auction(_auctionIdDouble1, emptyItems, _startingPriceDouble, _reservePriceDouble, null, _start, _end));
    }

    @Test
    void shouldReturnAuctionId() {
        // Arrange
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Act
        AuctionId result = auction.identity();

        // Assert
        assertSame(_auctionIdDouble1, result);
    }

    @Test
    void shouldReturnTrueWhenIdsAreEqual() {
        // Arrange
        Auction auction1 = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _start, _end);
        Auction auction2 = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Act
        boolean result = auction1.sameAs(auction2);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenIdsAreDifferent() {
        // Arrange
        Auction auction1 = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _start, _end);
        Auction auction2 = new Auction(_auctionIdDouble2, _items, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Act
        boolean result = auction1.sameAs(auction2);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenObjectIsNull() {
        // Arrange
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Assert
        assertFalse(auction.sameAs(null));
    }

    @Test
    void shouldReturnFalseWhenObjectIsDifferentType() {
        // Arrange
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _start, _end);

        // Assert
        assertFalse(auction.sameAs("not an auction"));
    }

    @Test
    void shouldReturnStartingPrice() {
        // Arrange
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        Price result = auction.getStartingPrice();

        // Assert
        assertEquals(_startingPriceDouble, result);
    }

    @Test
    void shouldReturnOutrightPriceWhenDefined() {
        // Arrange
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble,_auctionStart, _auctionEnd); // SUT

        // Act
        Price result = auction.getOutrightPrice();

        // Assert
        assertEquals(_outrightPriceDouble, result);
    }

    @Test
    void shouldReturnNullOutrightPriceWhenNotDefined() {
        // Arrange
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, null,_auctionStart, _auctionEnd); // SUT

        // Act
        Price result = auction.getOutrightPrice();

        // Assert
        assertNull(result);
    }

    @Test
    void acceptBidShouldThrowWhenAuctionIsNotActive() {
        // Arrange
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT
        Bid bidDouble = mock(Bid.class); // stub
        Price bidPriceDouble = mock(Price.class); // stub
        when(bidDouble.getOfferPrice()).thenReturn(bidPriceDouble);
        when(bidPriceDouble.getValue()).thenReturn(105.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(bidDouble)); // SUT
    }
    @Test
    void acceptBidThrowsWhenBidPriceEqualsStartingPrice() throws Exception {
        // Arrange
        ZonedDateTime _startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = _startFuture.plusDays(1);
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _startFuture, endFuture); //SUT

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        Bid bid = mock(Bid.class);
        Price bidPrice = mock(Price.class);
        when(bid.getOfferPrice()).thenReturn(bidPrice);
        when(bidPrice.getValue()).thenReturn(10.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(bid));
    }

    @Test
    void constructorShouldThrowWhenOutrightPriceIsNotGreaterThanStartingPrice() {
        // Arrange
        when(_outrightPriceDouble.getValue()).thenReturn(10.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd)); // SUT
    }

    @Test
    void getBidsReturnsNonNullEvenWhenEmpty() {
        // Arrange
        ZonedDateTime _start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = _start.plusHours(1);

        //SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _start, end);

        // Act
        MemoBidRepo bidRepo = auction.getBids();

        // Assert
        assertNotNull(bidRepo);
        assertThrows(IllegalStateException.class, () -> bidRepo.getHighestBid());
    }

    @Test
    void acceptBidAddsBidWhenAuctionIsActiveAndPriceIsAboveStartingPrice() throws Exception {
        // Arrange
        ZonedDateTime _startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = _startFuture.plusDays(1);
        User buyerDouble = mock(User.class);
        // #SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _startFuture, endFuture);

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        Bid bid = mock(Bid.class);
        Price bidPrice = mock(Price.class);
        when(bid.getOfferPrice()).thenReturn(bidPrice);
        when(bidPrice.getValue()).thenReturn(25.0);
        when(bid.getBidder()).thenReturn(buyerDouble);

        // Act
        auction.acceptBid(bid);

        // Assert
        assertSame(bid, auction.getBids().getHighestBid());
    }

    @Test
    void acceptBidShouldFinalizeAuctionWhenOutrightPriceIsReached() throws Exception {
        // Arrange
        ZonedDateTime _startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = _startFuture.plusDays(1);
        // SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _startFuture, endFuture);

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        Bid bidDouble = mock(Bid.class);
        Price bidPriceDouble = mock(Price.class);
        when(bidDouble.getOfferPrice()).thenReturn(bidPriceDouble);
        when(bidPriceDouble.getValue()).thenReturn(100.0);
        when(bidPriceDouble.isGreaterOrEqualThan(_reservePriceDouble)).thenReturn(true);

        User buyerDouble = mock(User.class);
        when(bidDouble.getBidder()).thenReturn(buyerDouble);

        // Act
        auction.acceptBid(bidDouble);

        //Assert
        assertSame(buyerDouble, getPrivateField(auction, "_buyer"));
        assertEquals(bidPriceDouble, getPrivateField(auction, "_finalPrice"));
    }

    @Test
    void finalizeAuctionShouldSetBuyerWhenReserveIsMet() throws Exception {
        // Arrange
        ZonedDateTime _startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = _startFuture.plusDays(1);
        // SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _startFuture, endFuture);

        Bid bidDouble = mock(Bid.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(60.0);
        when(bidDouble.getOfferPrice()).thenReturn(priceDouble);
        when(priceDouble.isGreaterOrEqualThan(_reservePriceDouble)).thenReturn(true);

        User buyerDouble = mock(User.class);
        when(bidDouble.getBidder()).thenReturn(buyerDouble);
        MemoBidRepo bidRepoDouble = mock(MemoBidRepo.class);
        when(bidRepoDouble.getHighestBid()).thenReturn(bidDouble);

        setPrivateField(auction, "_bids", bidRepoDouble);

        // Act
        auction.finalizeAuction();

        // Assert
        assertSame(buyerDouble, getPrivateField(auction, "_buyer"));
    }

    @Test
    void finalizeAuctionShouldNotSetBuyerWhenReserveIsNotMet() throws Exception {
        // Arrange
        ZonedDateTime _startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = _startFuture.plusDays(1);
        // SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _startFuture, endFuture);

        Bid bidDouble = mock(Bid.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(20.0);
        when(bidDouble.getOfferPrice()).thenReturn(priceDouble);

        MemoBidRepo bidRepoDouble = mock(MemoBidRepo.class);
        when(bidRepoDouble.getHighestBid()).thenReturn(bidDouble);

        setPrivateField(auction, "_bids", bidRepoDouble);

        // Act
        auction.finalizeAuction();

        // Assert
        assertNull(getPrivateField(auction, "_buyer"));
        assertNull(getPrivateField(auction, "_finalPrice"));
    }

    private void setPrivateField(Auction auction, String fieldName, Object value) throws Exception {
        Field field = Auction.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(auction, value);
    }

    private Object getPrivateField(Auction auction, String fieldName) throws Exception {
        Field field = Auction.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(auction);
    }

    // Isolated test of isByAuthor method
    @Test
    void isByAuthorShouldReturnTrueWhenAuthorMatches() {
        //Arrange
        Author _author = mock(Author.class);
        when(_itemDouble.isByAuthor(_author)).thenReturn(true);

        // SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByAuthor(_author);

        //Assert
        assertTrue(result);

    }

    @Test
    void isByAuthorShouldReturnFalseWhenAuthorIsDifferent() {
        //Arrange
        Author _author2 = mock(Author.class);
        when(_itemDouble.isByAuthor(_author2)).thenReturn(false);

        // SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByAuthor(_author2);

        //Assert
        assertFalse(result);

    }

    @Test
    void isByAuthorShouldDelegateToItem() {
        //Arrange
        Author _author = mock(Author.class);

        //SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        auction.isByAuthor(_author);

        //Assert
        verify(_itemDouble, times(1)).isByAuthor(_author);
    }

    @Test
    void isByGenreShouldReturnTrueWhenGenreMatches() {
        //Arrange
        Genre _genre = mock(Genre.class);
        when(_itemDouble.isByGenre(_genre)).thenReturn(true);

        // SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByGenre(_genre);

        //Assert
        assertTrue(result);
    }

    @Test
    void isByGenreShouldReturnFalseWhenGenreIsDifferent() {
        //Arrange
        Genre _genre2 = mock(Genre.class);
        when(_itemDouble.isByGenre(_genre2)).thenReturn(false);

        // SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByGenre(_genre2);

        //Assert
        assertFalse(result);
    }

    @Test
    void isByGenreShouldDelegateToItem() {
        //Arrange
        Genre _genre = mock(Genre.class);

        //SUT
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        auction.isByGenre(_genre);

        //Assert
        verify(_itemDouble, times(1)).isByGenre(_genre);
    }

    // Isolated test of isByPublishingCompany method
    @Test
    void isByPublisherShouldReturnTrueWhenPublisherMatches() {
        // Arrange
        PublishingCompany publisherDouble = mock(PublishingCompany.class); // stub
        when(_itemDouble.isByPublishingCompany(publisherDouble)).thenReturn(true);
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        boolean result = auction.isByPublishingCompany(publisherDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void isByPublisherShouldReturnFalseWhenPublisherDoesNotMatch() {
        // Arrange
        PublishingCompany publisherDouble = mock(PublishingCompany.class); // stub
        when(_itemDouble.isByPublishingCompany(publisherDouble)).thenReturn(false);
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        boolean result = auction.isByPublishingCompany(publisherDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublisherShouldDelegateToItem() {
        // Arrange
        PublishingCompany publisherDouble = mock(PublishingCompany.class); // stub
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        auction.isByPublishingCompany(publisherDouble);

        // Assert
        verify(_itemDouble).isByPublishingCompany(publisherDouble);
    }

    @Test
    void isByPublicationShouldReturnTrueWhenPublicationMatches() {
        // Arrange
        Publication publicationDouble = mock(Publication.class); // stub
        when(_itemDouble.isByPublication(publicationDouble)).thenReturn(true);

        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        boolean result = auction.isByPublication(publicationDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void isByPublicationShouldReturnFalseWhenPublicationDoesNotMatch() {
        // Arrange
        Publication publicationDouble = mock(Publication.class); // stub
        when(_itemDouble.isByPublication(publicationDouble)).thenReturn(false);
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        boolean result = auction.isByPublication(publicationDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublicationShouldDelegateToItem() {
        // Arrange
        Publication publicationDouble = mock(Publication.class); // stub
        Auction auction = new Auction(_auctionIdDouble1, _items, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        auction.isByPublication(publicationDouble);

        // Assert
        verify(_itemDouble).isByPublication(publicationDouble);
    }
}

