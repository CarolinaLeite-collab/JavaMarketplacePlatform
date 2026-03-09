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
    private Publication _publication;
    private Item _item;
    private Price _startingPrice;
    private Price _outrightPrice; // optional (nullable)
    private User _buyer;
    private BidRepo _bidRepo;


    //Isolated
    private Publication _publicationDouble;
    private Item _itemDouble;
    private Price _startingPriceDouble;
    private Price _outrightPriceDouble; // optional (nullable)
    private User _buyerDouble;
    private BidRepo _bidRepoDouble;
    private ZonedDateTime _auctionStart1;
    private ZonedDateTime _auctionEnd1;

    @BeforeEach
    void setUp() {
        _buyer = new User(new Name("Joaquim"), new Email("test@isep.com"));

        _publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .genre(new Genre("action"))
                .build();
        _item = new Item(_publication, Condition.GOOD);
        _startingPrice = new Price(10.0, Currency.EUR);
        _outrightPrice = new Price(50.0, Currency.EUR);
        Country country = new Country("Portugal");
        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", country, "6969-200", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix, "919999999");
        User ze = new User(new Name("Ze"), address, new Email("reader@email.com"), phoneNumber1);
        User antonio = new User(new Name("Antonio"), address, new Email("ar@email.com"), phoneNumber1);
        Bid bid1 = new Bid(ze, new Price(100.0, Currency.EUR));
        Bid bid2 = new Bid(antonio, new Price(102.0, Currency.EUR));
        BidRepo bids = new BidRepo(new BidFactory());
        bids.addBid(bid1);
        bids.addBid(bid2);
        _bidRepo = bids;

        // Isolated setup

        _publicationDouble = mock(Publication.class);
        _itemDouble = mock(Item.class);
        _startingPriceDouble = mock(Price.class);
        _outrightPriceDouble = mock(Price.class);
        _buyerDouble = mock(User.class);
        _bidRepoDouble = mock(BidRepo.class);
        _auctionStart1 = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));
        _auctionEnd1 = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("Europe/Lisbon"));

        when(_outrightPriceDouble.getValue()).thenReturn(100.0);
        when(_startingPriceDouble.getValue()).thenReturn(10.0);

    }

    //test a sucessful auction
    @Test
    void constructorBuildsAuctionWithoutOutrightPrice() {
        // Arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");
        // Act
        Auction auction = new Auction(_item, _startingPrice, auctionStart, auctionEnd);
        // Assert
        assertNotNull(auction);
        assertEquals(_item, auction.getItem());
        assertNotNull(_item.getAuction());
        assertSame(auction, _item.getAuction());
    }


    @Test
    void constructorBuildsAuctionWithOutrightPrice() {
        // Arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");
        // Act
        Auction auction = new Auction(_item, _startingPrice, _outrightPrice, auctionStart, auctionEnd);
        // Assert
        assertNotNull(auction);
        assertEquals(_item, auction.getItem());
        assertNotNull(_item.getAuction());
        assertSame(auction, _item.getAuction());
    }

    //test a unsucessful auction
    //Exception date related
    @Test
    void throwsExceptionEndDateBeforeStartDate() {
        // Arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2026-02-01T00:00:00+00:00[Europe/Lisbon]");
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, auctionStart, auctionEnd));
        // Assert
        assertEquals("Invalid end date", exception.getMessage());
    }

    @Test
    void throwsExceptionStartDateinvalid() {
        // Arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2025-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, auctionStart, auctionEnd));
        // Assert
        assertEquals("Invalid start date", exception.getMessage());
    }

    @Test
    void acceptBid_throwsexceptionInvalidBidTimeAuctionNotActive() {
        // Arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime now = ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Bid bid3 = new Bid(_buyer, new Price(105.0, Currency.EUR));
        Auction auction = new Auction(_item, _startingPrice, auctionStart, auctionEnd);

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> auction.acceptBid(bid3)
        );
        // Assert
        assertEquals("Invalid Bid", ex.getMessage());
    }
    @Test
    void test_is_by_genre_should_return_true_when_genre_matches() {

        // Arrange
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Genre _genre= new Genre("action");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        // Act
        boolean result = auction.isByGenre(_genre);

        // Assert
        assertTrue(result);

    }
    @Test
    void test_is_by_genre_should_return_true_when_genre_matches_case_insensitive() {

        // Arrange
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Genre _genre= new Genre("ActioN");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        // Act
        boolean result = auction.isByGenre(_genre);

        // Assert
        assertTrue(result);

    }

    @Test
    void test_is_by_genre_should_return_false_when_genre_does_not_match() {

        // Arrange
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Genre _genre= new Genre("Horror");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        // Act
        boolean result = auction.isByGenre(_genre);

        // Assert
        assertFalse(result);

    }

    @Test
    void constructorThrowsWhenOutrightPriceIsNotGreaterThanStartingPrice() {
        // Arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, new Price(10.0, Currency.EUR), auctionStart, auctionEnd));

        // Assert
        assertEquals("Invalid outright price", ex.getMessage());
    }

    @Test
    void constructorWithOutrightThrowsWhenStartDateInvalid() {
        // Arrange
        ZonedDateTime auctionStart = ZonedDateTime.now().minusDays(1);
        ZonedDateTime auctionEnd = ZonedDateTime.now().plusDays(1);

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, _outrightPrice, auctionStart, auctionEnd));

        // Assert
        assertEquals("Invalid start date", ex.getMessage());
    }

    @Test
    void constructorWithOutrightThrowsWhenEndDateBeforeStartDate() {
        // Arrange
        ZonedDateTime auctionStart = ZonedDateTime.now().plusDays(2);
        ZonedDateTime auctionEnd = ZonedDateTime.now().plusDays(1);

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, _outrightPrice, auctionStart, auctionEnd));

        // Assert
        assertEquals("Invalid end date", ex.getMessage());
    }

    @Test
    void test_is_by_author_should_return_true_when_author_matches() {
        // Arrange
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Author author = _item.getPublication().getAuthor();

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        // Act
        boolean result = auction.isByAuthor(author);

        // Assert
        assertTrue(result);
    }

    @Test
    void test_is_by_author_should_return_true_when_author_matches_case_insensitive() {
        // Arrange
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Author author = new Author(_item.getPublication().getAuthor().getName().toUpperCase());

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        // Act
        boolean result = auction.isByAuthor(author);

        // Assert
        assertTrue(result);
    }

    @Test
    void test_is_by_author_should_return_false_when_author_does_not_match() {
        // Arrange
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Author differentAuthor = new Author("Different");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        // Act
        boolean result = auction.isByAuthor(differentAuthor);

        // Assert
        assertFalse(result);
    }

    @Test
    void getBidsReturnsNonNullEvenWhenEmpty() {
        // Arrange
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusHours(1);

        Auction auction = new Auction(_item, _startingPrice, start, end);

        // Act
        BidRepo bidRepo = auction.getBids();

        // Assert
        assertNotNull(bidRepo);
        assertThrows(IllegalStateException.class, () -> bidRepo.getHighestBid());
    }

    @Test
    void shouldReturnTrueForMatchingPublication() {
        // Arrange
        Auction auction = new Auction(_item, _startingPrice, _outrightPrice, _auctionStart1, _auctionEnd1);

        // Act
        boolean result = auction.isByPublication(_publication);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForDifferentPublication() {
        // Arrange
        Publication otherPublication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("838894522X"))
                .year(Year.of(2020))
                .title(new Title("Louis I. Khan: The idea of order"))
                .author(new Author("Klaus-Peter Gast"))
                .publisher(new PublishingCompany("Birkhauser"))
                .build();

        Auction auction = new Auction(_item, _startingPrice, _outrightPrice, _auctionStart1, _auctionEnd1);

        // Act
        boolean result = auction.isByPublication(otherPublication);

        // Assert
        assertFalse(result);
    }
    @Test
    void should_return_true_when_publisher_matches_and_be_case_insensitive() {
        // Arrange
        Publication publication1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("PeNgUin"))
                .genre(new Genre("action"))
                .build();
        Item item1 = new Item(publication1, Condition.GOOD);

        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        PublishingCompany publisher1 = _item.getPublication().getPublisher();
        PublishingCompany publisher2 = item1.getPublication().getPublisher();

        Auction auction1 = new Auction(_item, _startingPrice, _startDate, _endDate);
        Auction auction2 = new Auction(_item, _outrightPrice, _startDate, _endDate);

        // Act
        boolean result1 = auction1.isByPublisher(publisher1);
        boolean result2 = auction2.isByPublisher(publisher2);

        // Assert
        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    void should_return_false_when_publisher_does_not_match() {
        // Arrange
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        PublishingCompany publisher3 = new PublishingCompany("Porto Editora");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        // Act
        boolean result = auction.isByPublisher(publisher3);

        // Assert
        assertFalse(result);
    }

    @Test
    void acceptBidAddsBidWhenAuctionIsActiveAndPriceIsAboveStartingPrice() throws Exception {
        // Arrange
        ZonedDateTime startFuture = ZonedDateTime.now().plusDays(1);
        ZonedDateTime endFuture = startFuture.plusDays(1);
        Auction auction = new Auction(_item, _startingPrice, startFuture, endFuture);

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        Bid bid = new Bid(_buyer, new Price(25.0, Currency.EUR));

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
        Auction auction = new Auction(_item, _startingPrice, startFuture, endFuture);

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        User bidder1 = new User(new Name("Ana"), new Email("ana@example.com"));
        User bidder2 = new User(new Name("Bruno"), new Email("bruno@example.com"));
        Bid lower = new Bid(bidder1, new Price(20.0, Currency.EUR));
        Bid higher = new Bid(bidder2, new Price(30.0, Currency.EUR));

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
        Auction auction = new Auction(_item, _startingPrice, startFuture, endFuture);

        ZonedDateTime now = ZonedDateTime.now();
        setPrivateField(auction, "_auctionStartDate", now.minusMinutes(5));
        setPrivateField(auction, "_auctionEndDate", now.plusMinutes(5));

        BidRepo bids = mock(BidRepo.class);
        setPrivateField(auction, "_bids", bids);

        Bid bid = new Bid(_buyer, new Price(25.0, Currency.EUR));

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
        Auction auction = new Auction(_item, _startingPrice, startFuture, endFuture);

        User bidder = new User(new Name("Carla"), new Email("carla@example.com"));
        Bid highestBid = new Bid(bidder, new Price(40.0, Currency.EUR));

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

        // SUT
        Auction auction = new Auction(_itemDouble,_startingPriceDouble,_outrightPriceDouble, _auctionStart1, _auctionEnd1);

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
        Auction auction = new Auction(_itemDouble,_startingPriceDouble,_outrightPriceDouble, _auctionStart1, _auctionEnd1);

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
        Auction auction = new Auction(_itemDouble,_startingPriceDouble,_outrightPriceDouble, _auctionStart1, _auctionEnd1);

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
        Auction auction = new Auction(_itemDouble,_startingPriceDouble,_outrightPriceDouble, _auctionStart1, _auctionEnd1);

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
        Auction auction = new Auction(_itemDouble,_startingPriceDouble,_outrightPriceDouble, _auctionStart1, _auctionEnd1);

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
        Auction auction = new Auction(_itemDouble,_startingPriceDouble,_outrightPriceDouble, _auctionStart1, _auctionEnd1);

        //Act
        auction.isByGenre(_genre);

        //Assert
        verify(_itemDouble, times(1)).isByGenre(_genre);
    }
}
