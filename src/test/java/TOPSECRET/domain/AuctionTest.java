package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {
    private Publication _publication;
    private Item _item;
    private Price _startingPrice;
    private Price _outrightPrice; // optional (nullable)
    private User _buyer;
    private Bids _bids;

    @BeforeEach
    void setUp() {
        _buyer = new User(new Name("Joaquim"), new Email("test@isep.com"));

        _publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .genre(new Genre("action"))
                .build();
        _item = new Item(_publication, Condition.GOOD);
        _startingPrice = new Price(10.0, Currency.EUR);
        _outrightPrice = new Price(50.0, Currency.EUR);

        Address address = new Address("Rua de S. Tomé", "Porto", Address.BuildingType.HOUSE, "Porto", "Porto", Address.Country.PORTUGAL, "6969-200", null);
        PhonePrefix prefix = new PhonePrefix("+351");
        Phone phoneNumber1 = new Phone(prefix, "919999999");
        User ze = new User(new Name("Ze"), address, new Email("reader@email.com"), phoneNumber1);
        User antonio = new User(new Name("Antonio"), address, new Email("ar@email.com"), phoneNumber1);
        Bid bid1 = new Bid(ze, new Price(100.0, Currency.EUR));
        Bid bid2 = new Bid(antonio, new Price(102.0, Currency.EUR));
        Bids bids = new Bids();
        bids.addBid(bid1);
        bids.addBid(bid2);
        _bids = bids;
    }

    //test a sucessful auction
    @Test
    void constructorBuildsAuctionWithoutOutrightPrice() {
        //arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");
        //act
        Auction auction = new Auction(_item, _startingPrice, auctionStart, auctionEnd);
        //assert
        assertNotNull(auction);
        assertEquals(_item, auction.getItem());
    }


    @Test
    void constructorBuildsAuctionWithOutrightPrice() {
        //arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");
        //act
        Auction auction = new Auction(_item, _startingPrice, _outrightPrice, auctionStart, auctionEnd);
        //assert
        assertNotNull(auction);
        assertEquals(_item, auction.getItem());
    }

    //test a unsucessful auction
    //Exception date related
    @Test
    void throwsExceptionEndDateBeforeStartDate() {
        //arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2026-02-01T00:00:00+00:00[Europe/Lisbon]");
        //act+assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, auctionStart, auctionEnd));
        assertEquals("Invalid end date", exception.getMessage());
    }

    @Test
    void throwsExceptionStartDateinvalid() {
        //arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2025-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");
        //act+assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, auctionStart, auctionEnd));
        assertEquals("Invalid start date", exception.getMessage());
    }

    @Test
    void acceptBid_throwsexceptionInvalidBidTimeAuctionNotActive() {
        //arrange
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime now = ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Bid bid3 = new Bid(_buyer, new Price(105.0, Currency.EUR));
        Auction auction = new Auction(_item, _startingPrice, auctionStart, auctionEnd);

        //act+assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> auction.acceptBid(bid3)
        );
        assertEquals("Invalid Bid", ex.getMessage());
    }
    @Test
    void test_is_by_genre_should_return_true_when_genre_matches() {

        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Genre _genre= new Genre("action");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);


        assertTrue(auction.isByGenre(_genre));

    }
    @Test
    void test_is_by_genre_should_return_true_when_genre_matches_case_insensitive() {

        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Genre _genre= new Genre("ActioN");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);


        assertTrue(auction.isByGenre(_genre));

    }

    @Test
    void test_is_by_genre_should_return_false_when_genre_does_not_match() {

        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Genre _genre= new Genre("Horror");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        assertFalse(auction.isByGenre(_genre));

    }

    @Test
    void constructorThrowsWhenOutrightPriceIsNotGreaterThanStartingPrice() {
        ZonedDateTime auctionStart = ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime auctionEnd = ZonedDateTime.parse("2027-02-01T00:00:00+00:00[Europe/Lisbon]");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, new Price(10.0, Currency.EUR), auctionStart, auctionEnd));

        assertEquals("Invalid outright price", ex.getMessage());
    }

    @Test
    void constructorWithOutrightThrowsWhenStartDateInvalid() {
        ZonedDateTime auctionStart = ZonedDateTime.now().minusDays(1);
        ZonedDateTime auctionEnd = ZonedDateTime.now().plusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, _outrightPrice, auctionStart, auctionEnd));

        assertEquals("Invalid start date", ex.getMessage());
    }

    @Test
    void constructorWithOutrightThrowsWhenEndDateBeforeStartDate() {
        ZonedDateTime auctionStart = ZonedDateTime.now().plusDays(2);
        ZonedDateTime auctionEnd = ZonedDateTime.now().plusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Auction(_item, _startingPrice, _outrightPrice, auctionStart, auctionEnd));

        assertEquals("Invalid end date", ex.getMessage());
    }

    @Test
    void test_is_by_author_should_return_true_when_author_matches() {
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Author author = _item.getPublication().getAuthor();

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        assertTrue(auction.isByAuthor(author));
    }

    @Test
    void test_is_by_author_should_return_true_when_author_matches_case_insensitive() {
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Author author = new Author(_item.getPublication().getAuthor().getName().toUpperCase());

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        assertTrue(auction.isByAuthor(author));
    }

    @Test
    void test_is_by_author_should_return_false_when_author_does_not_match() {
        ZonedDateTime _startDate =
                ZonedDateTime.parse("2027-01-01T00:00:00+00:00[Europe/Lisbon]");
        ZonedDateTime _endDate =
                ZonedDateTime.parse("2027-01-02T00:00:00+00:00[Europe/Lisbon]");
        Author differentAuthor = new Author("Different");

        Auction auction = new Auction(_item, _startingPrice, _startDate, _endDate);

        assertFalse(auction.isByAuthor(differentAuthor));
    }

    @Test
    void getBidsReturnsNonNullEvenWhenEmpty() {
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusHours(1);

        Auction auction = new Auction(_item, _startingPrice, start, end);

        assertNotNull(auction.getBids());
        assertThrows(IllegalStateException.class, () -> auction.getBids().getHighestBid());
    }

    @Test
    void acceptBidAddsBidWhenAuctionIsActive() {
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusMinutes(1);
        Bid bid = new Bid(_buyer, new Price(20.0, Currency.EUR));

        Auction auction = new Auction(_item, _startingPrice, start, end);

        setAuctionWindow(auction, ZonedDateTime.now().minusSeconds(1), ZonedDateTime.now().plusSeconds(1));
        auction.acceptBid(bid);

        assertSame(bid, auction.getBids().getHighestBid());
    }

    @Test
    void acceptBidThrowsWhenOfferNotHigherThanStartingPrice() {
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusMinutes(1);
        Bid bid = new Bid(_buyer, new Price(_startingPrice.getValue(), Currency.EUR));

        Auction auction = new Auction(_item, _startingPrice, start, end);

        setAuctionWindow(auction, ZonedDateTime.now().minusSeconds(1), ZonedDateTime.now().plusSeconds(1));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> auction.acceptBid(bid));
        assertEquals("Invalid Bid", ex.getMessage());
    }

    @Test
    void acceptBidThrowsWhenAuctionAlreadyEnded() {
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusMinutes(1);
        Bid bid = new Bid(_buyer, new Price(20.0, Currency.EUR));

        Auction auction = new Auction(_item, _startingPrice, start, end);

        setAuctionWindow(auction, ZonedDateTime.now().minusSeconds(2), ZonedDateTime.now().minusSeconds(1));
        assertThrows(IllegalArgumentException.class, () -> auction.acceptBid(bid));
    }

    @Test
    void finalizeAuctionSetsBuyerAndFinalPriceFromHighestBid() {
        ZonedDateTime start = ZonedDateTime.now().plusMinutes(1);
        ZonedDateTime end = start.plusMinutes(1);
        Auction auction = new Auction(_item, _startingPrice, start, end);

        User higher = new User(new Name("Higher"), new Email("high@example.com"));
        Bid lowBid = new Bid(_buyer, new Price(20.0, Currency.EUR));
        Bid highBid = new Bid(higher, new Price(30.0, Currency.EUR));

        Bids bids = new Bids();
        bids.addBid(lowBid);
        bids.addBid(highBid);
        setAuctionBids(auction, bids);

        auction.finalizeAuction();

        assertSame(higher, readPrivateField(auction, "_buyer", User.class));
        assertEquals(highBid.getOfferPrice(), readPrivateField(auction, "_finalPrice", Price.class));
    }

    private void setAuctionWindow(Auction auction, ZonedDateTime start, ZonedDateTime end) {
        try {
            var startField = Auction.class.getDeclaredField("_auctionStartDate");
            startField.setAccessible(true);
            startField.set(auction, start);

            var endField = Auction.class.getDeclaredField("_auctionEndDate");
            endField.setAccessible(true);
            endField.set(auction, end);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to adjust auction window for test: " + e.getMessage());
        }
    }

    private void setAuctionBids(Auction auction, Bids bids) {
        try {
            var bidsField = Auction.class.getDeclaredField("_bids");
            bidsField.setAccessible(true);
            bidsField.set(auction, bids);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject bids for test: " + e.getMessage());
        }
    }

    private <T> T readPrivateField(Auction auction, String fieldName, Class<T> type) {
        try {
            var field = Auction.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(auction);
            return type.cast(value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to read field '" + fieldName + "' for test: " + e.getMessage());
            return null; // unreachable
        }
    }
}
