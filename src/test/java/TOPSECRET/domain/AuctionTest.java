package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionTest {
    private Item _itemDouble;
    private Price _startingPriceDouble;
    private Price _outrightPriceDouble; // optional (nullable)
    private User _buyerDouble;
    private ZonedDateTime _auctionStart;
    private ZonedDateTime _auctionEnd;

    @BeforeEach
    void setUp() {
        _buyerDouble = mock(User.class);
        _itemDouble = mock(Item.class);
        _startingPriceDouble = mock(Price.class);
        _outrightPriceDouble = mock(Price.class);
        _auctionStart = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
        _auctionEnd = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
            }

    @Test
    void constructorShouldBuildAuctionWithoutOutrightPrice() {
        // Act
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _auctionStart, _auctionEnd); // SUT

        // Assert
        assertNotNull(auction);
        assertEquals(_itemDouble, auction.getItem());
        verify(_itemDouble).setAuction(auction);
    }

    @Test
    void constructorShouldBuildAuctionWithOutrightPrice() {
        // Arrange
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);

        // Act
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

        // Assert
        assertNotNull(auction);
        assertEquals(_itemDouble, auction.getItem());
        verify(_itemDouble).setAuction(auction);
    }


    @Test
    void constructorShouldThrowWhenEndDateIsBeforeStartDate() {
        // Arrange
        ZonedDateTime endBeforeStart = ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemDouble, _startingPriceDouble, _auctionStart, endBeforeStart)); // SUT
    }

    @Test
    void constructorShouldThrowWhenStartDateIsInvalid() {
        // Arrange
        ZonedDateTime pastDate = ZonedDateTime.now().minusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemDouble, _startingPriceDouble, pastDate, _auctionEnd)); // SUT
    }

    @Test
    void acceptBidShouldThrowWhenAuctionIsNotActive() {
        // Arrange
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _auctionStart, _auctionEnd); // SUT
        User buyerDouble = mock(User.class); // stub
        Bid bidDouble = mock(Bid.class); // stub
        Price bidPriceDouble = mock(Price.class); // stub
        when(bidDouble.getOfferPrice()).thenReturn(bidPriceDouble);
        when(bidPriceDouble.getValue()).thenReturn(105.0);
        when(_startingPriceDouble.getValue()).thenReturn(10.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> auction.acceptBid(bidDouble)); // SUT
    }
    @Test
    void acceptBidThrowsWhenBidPriceEqualsStartingPrice() throws Exception {
        // Arrange
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, startFuture, endFuture);

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
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(10.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd)); // SUT
    }

    @Test
    void constructorWithOutrightShouldThrowWhenStartDateIsInvalid() {
        // Arrange
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        ZonedDateTime pastDate = ZonedDateTime.now().minusDays(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, pastDate, _auctionEnd)); // SUT
    }

    @Test
    void constructorWithOutrightShouldThrowWhenEndDateIsBeforeStartDate() {
        // Arrange
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        ZonedDateTime endBeforeStart = ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, endBeforeStart)); // SUT
    }
    @Test
    void getBidsReturnsNonNullEvenWhenEmpty() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusHours(1);

        Auction auction = new Auction(_itemDouble, _startingPriceDouble, start, end);

        // Act
        BidRepo bidRepo = auction.getBids();

        // Assert
        assertNotNull(bidRepo);
        assertThrows(IllegalStateException.class, () -> bidRepo.getHighestBid());
    }

    @Test
    void acceptBidAddsBidWhenAuctionIsActiveAndPriceIsAboveStartingPrice() throws Exception {
        // Arrange
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        // #SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, startFuture, endFuture);

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        Bid bid = mock(Bid.class);
        Price bidPrice = mock(Price.class);
        when(bid.getOfferPrice()).thenReturn(bidPrice);
        when(bidPrice.getValue()).thenReturn(25.0);
        when(bid.getBidder()).thenReturn(_buyerDouble);

        // Act
        auction.acceptBid(bid);

        // Assert
        assertSame(bid, auction.getBids().getHighestBid());
    }


    @Test
    void finalizeAuctionSetsBuyerAndFinalPriceToHighestBid() throws Exception {
        // Arrange
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        // #SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, startFuture, endFuture);

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        Bid lower = mock(Bid.class);
        Price lowerPrice = mock(Price.class);
        when(lower.getOfferPrice()).thenReturn(lowerPrice);
        when(lowerPrice.getValue()).thenReturn(20.0);
        Bid higher = mock(Bid.class);
        Price higherPrice = mock(Price.class);
        when(higher.getOfferPrice()).thenReturn(higherPrice);
        when(higherPrice.getValue()).thenReturn(30.0);
        User bidder2 = mock(User.class);
        when(higher.getBidder()).thenReturn(bidder2);

        // Act
        auction.acceptBid(lower);
        auction.acceptBid(higher);
        auction.finalizeAuction();

        // Assert
        assertSame(bidder2, getPrivateField(auction, "_buyer"));
        assertEquals(higher.getOfferPrice(), getPrivateField(auction, "_finalPrice"));
    }


    @Test
    void acceptBidDelegatesToBidsCollection() throws Exception {
        // Arrange
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, startFuture, endFuture);

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        BidRepo bids = mock(BidRepo.class);
        setPrivateField(auction, "_bids", bids);

        Bid bid = mock(Bid.class);
        Price bidPrice = mock(Price.class);
        when(bid.getOfferPrice()).thenReturn(bidPrice);
        when(bidPrice.getValue()).thenReturn(25.0);

        // Act
        auction.acceptBid(bid);

        // Assert
        verify(bids).addBid(bid);
    }

    @Test
    void finalizeAuctionUsesHighestBidFromBidsCollection() throws Exception {
        // Arrange
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, startFuture, endFuture);

        User bidder = mock(User.class);
        Bid highestBid = mock(Bid.class);
        Price highestPrice = mock(Price.class);
        when(highestBid.getBidder()).thenReturn(bidder);
        when(highestBid.getOfferPrice()).thenReturn(highestPrice);

        BidRepo bids = mock(BidRepo.class);
        when(bids.getHighestBid()).thenReturn(highestBid);
        setPrivateField(auction, "_bids", bids);

        // Act
        auction.finalizeAuction();

        // Assert
        verify(bids).getHighestBid();
        assertSame(bidder, getPrivateField(auction, "_buyer"));
        assertEquals(highestBid.getOfferPrice(), getPrivateField(auction, "_finalPrice"));
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
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);

        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

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
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);

        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByAuthor(_author2);

        //Assert
        assertFalse(result);

    }

    @Test
    void isByAuthorShouldDelegateToItem() {
        //Arrange
        Author _author = mock(Author.class);
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);

        //SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

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
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);

        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

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
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);

        // SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

        //Act
        boolean result = auction.isByGenre(_genre2);

        //Assert
        assertFalse(result);
    }

    @Test
    void isByGenreShouldDelegateToItem() {
        //Arrange
        Genre _genre = mock(Genre.class);
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);

        //SUT
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd);

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
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

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
        when(_startingPriceDouble.getValue()).thenReturn(10.0);
        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

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
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _outrightPriceDouble, _auctionStart, _auctionEnd); // SUT

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

        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _auctionStart, _auctionEnd); // SUT

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
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        boolean result = auction.isByPublication(publicationDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByPublicationShouldDelegateToItem() {
        // Arrange
        Publication publicationDouble = mock(Publication.class); // stub
        Auction auction = new Auction(_itemDouble, _startingPriceDouble, _auctionStart, _auctionEnd); // SUT

        // Act
        auction.isByPublication(publicationDouble);

        // Assert
        verify(_itemDouble).isByPublication(publicationDouble);
    }
}

