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
}

