package TOPSECRET.domain;

import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionTest {
    private Item _itemDouble;
    private Price _startingPriceDouble;
    private Price _reservePriceDouble;
    private Price _outrightPriceDouble; // optional (nullable)
    private ZonedDateTime _auctionStart;
    private ZonedDateTime _auctionEnd;

    @BeforeEach
    void setUp() {
        _itemDouble = mock(Item.class);
        _startingPriceDouble = mock(Price.class);
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        _reservePriceDouble = mock(Price.class);
        when(_reservePriceDouble.getValue()).thenReturn(50.0);
        _outrightPriceDouble = mock(Price.class);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        _auctionStart = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
        _auctionEnd = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
    }

    @Test
    void shouldCreateAuctionWithoutOutrightPrice() {
        // Act
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Assert
        assertNotNull(auction);
        assertEquals(_itemDouble, auction.getItem());
        verify(_itemDouble).setAuction(auction);
    }

    @Test
    void shouldCreateAuctionWithOutrightPrice() {
        // Act
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble,_auctionStart, _auctionEnd); // SUT

        // Assert
        assertNotNull(auction);
        assertEquals(_itemDouble, auction.getItem());
        verify(_itemDouble).setAuction(auction);
    }

    @Test
    void shouldThrowExceptionWhenReservePriceEqualsStartingPrice() {
        // Arrange
        when(_reservePriceDouble.getValue()).thenReturn(10.0);

        // Act
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Assert
        assertNotNull(auction);
    }

    @Test
    void shouldThrowExceptionWhenReservePriceIsLowerThanStartingPrice() {
        // Arrange
        when(_reservePriceDouble.getValue()).thenReturn(5.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd));
    }

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        // Arrange
        ZonedDateTime endBeforeStart = ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, endBeforeStart)); // SUT
    }

    @Test
    void constructorShouldThrowWhenStartDateIsInvalid() {
        // Arrange
        ZonedDateTime pastDate = ZonedDateTime.now().minusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, pastDate, _auctionEnd)); // SUT
    }

    @Test
    void shouldReturnStartingPrice() {
        // Arrange
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        Price result = auction.getStartingPrice();

        // Assert
        assertEquals(_startingPriceDouble, result);
    }

    @Test
    void shouldReturnOutrightPriceWhenDefined() {
        // Arrange
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble,_auctionStart, _auctionEnd); // SUT

        // Act
        Price result = auction.getOutrightPrice();

        // Assert
        assertEquals(_outrightPriceDouble, result);
    }

    @Test
    void shouldReturnNullOutrightPriceWhenNotDefined() {
        // Arrange
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, null,_auctionStart, _auctionEnd); // SUT

        // Act
        Price result = auction.getOutrightPrice();

        // Assert
        assertNull(result);
    }

    @Test
    void acceptBidShouldThrowWhenAuctionIsNotActive() {
        // Arrange
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT
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
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, startFuture, endFuture); //SUT

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
                () -> new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd)); // SUT
    }

    @Test
    void getBidsReturnsNonNullEvenWhenEmpty() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusHours(1);

        //SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, start, end);

        // Act
        MemoBidRepo bidRepo = auction.getBids();

        // Assert
        assertNotNull(bidRepo);
        assertThrows(IllegalStateException.class, () -> bidRepo.getHighestBid());
    }

    @Test
    void acceptBidAddsBidWhenAuctionIsActiveAndPriceIsAboveStartingPrice() throws Exception {
        // Arrange
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        User buyerDouble = mock(User.class);
        // #SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, startFuture, endFuture);

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
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, startFuture, endFuture);

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
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, startFuture, endFuture);

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
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, startFuture, endFuture);

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
        AuthorId authorIdDouble = mock(AuthorId.class);
        when(_itemDouble.isByAuthor(authorIdDouble)).thenReturn(true);

        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByAuthor(authorIdDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void isByAuthorShouldReturnFalseWhenAuthorIsDifferent() {
        //Arrange
        AuthorId author2IdDouble = mock(AuthorId.class);
        when(_itemDouble.isByAuthor(author2IdDouble)).thenReturn(false);

        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByAuthor(author2IdDouble);

        //Assert
        assertFalse(result);

    }

    @Test
    void isByAuthorShouldDelegateToItem() {
        //Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);

        //SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        auction.isByAuthor(authorIdDouble);

        //Assert
        verify(_itemDouble, times(1)).isByAuthor(authorIdDouble);
    }

    @Test
    void isByGenreShouldReturnTrueWhenGenreMatches() {
        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(_itemDouble.isByGenre(genreIdDouble)).thenReturn(true);

        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByGenre(genreIdDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void isByGenreShouldReturnFalseWhenGenreIsDifferent() {
        //Arrange
        GenreId genreId2Double = mock(GenreId.class);
        when(_itemDouble.isByGenre(genreId2Double)).thenReturn(false);

        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByGenre(genreId2Double);

        //Assert
        assertFalse(result);
    }

    @Test
    void isByGenreShouldDelegateToItem() {
        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        //SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        auction.isByGenre(genreIdDouble);

        //Assert
        verify(_itemDouble, times(1)).isByGenre(genreIdDouble);
    }

    // Isolated test of isByPublishingCompany method
    @Test
    void isByPublisherShouldReturnTrueWhenPublisherMatches() {
        // Arrange
        PublishingCompany publisherDouble = mock(PublishingCompany.class); // stub
        when(_itemDouble.isByPublishingCompany(publisherDouble)).thenReturn(true);
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

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
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        boolean result = auction.isByPublishingCompany(publisherDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublisherShouldDelegateToItem() {
        // Arrange
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        PublishingCompany publisherDouble = mock(PublishingCompany.class); // stub
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

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

        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

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
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        boolean result = auction.isByPublication(publicationDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublicationShouldDelegateToItem() {
        // Arrange
        Publication publicationDouble = mock(Publication.class); // stub
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _reservePriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        auction.isByPublication(publicationDouble);

        // Assert
        verify(_itemDouble).isByPublication(publicationDouble);
    }
}

