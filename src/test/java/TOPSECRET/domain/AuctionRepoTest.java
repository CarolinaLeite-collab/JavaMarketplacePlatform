package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuctionRepoTest {

    private PublicationType bookType;
    private Year publicationYear;
    private String defaultAuthorName;
    private String defaultPublisherName;

    private AuctionRepo _repo;
    private Publication _pub1;
    private Publication _pub2;
    private Item _item1;
    private Item _item2;
    private Item _item3;
    private Price _startingPrice;
    private ZonedDateTime _start;
    private ZonedDateTime _end;

    @BeforeEach
    void setUp() {
        _repo = new AuctionRepo();

        _pub1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("1111111111"))
                .year(Year.of(2020))
                .title(new Title("Architectonica Percepta"))
                .author(new Author("Paulo Providência"))
                .publisher(new Publisher("Park Books"))
                .build();

        _pub2 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("838894522X"))
                .year(Year.of(1980))
                .title(new Title("Louis I. Khan: The idea of order"))
                .author(new Author("Klaus-Peter Gast"))
                .publisher(new Publisher("Birkhauser"))
                .build();

        _item1 = new Item(_pub1, Condition.GOOD);
        _item2 = new Item(_pub1, Condition.FAIR);
        _item3 = new Item(_pub2, Condition.LIKE_NEW);

        _startingPrice = new Price(10.0, Currency.EUR);

        _start = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        _end = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("UTC"));

        bookType = new PublicationType("BOOK");
        publicationYear = Year.of(2019);
        defaultAuthorName = "Someone";
        defaultPublisherName = "SomePub";
    }

    private Publication publication(Genre genre, String isbn, String title) {
        return publication(genre, isbn, title, defaultAuthorName, defaultPublisherName);
    }

    private Publication publication(Genre genre, String isbn, String title, String authorName, String publisherName) {
        return Publication.builder()
                .type(bookType)
                .identifier(new ISBN(isbn))
                .year(publicationYear)
                .title(new Title(title))
                .author(new Author(authorName))
                .publisher(new Publisher(publisherName))
                .genre(genre)
                .build();
    }

    private Item itemFor(Genre genre, String isbn, String title) {
        return new Item(publication(genre, isbn, title), Condition.GOOD);
    }

    private Item itemFor(Genre genre, String isbn, String title, String authorName, String publisherName) {
        return new Item(publication(genre, isbn, title, authorName, publisherName), Condition.GOOD);
    }

    private ZonedDateTime futureStart() {
        return ZonedDateTime.now().plusDays(1);
    }

    private ZonedDateTime futureEnd() {
        return ZonedDateTime.now().plusDays(2);
    }

    @Test
    void createAuctionAddsAuctionAndCanBeRetrievedByGenre() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Item item = itemFor(action, "9789896710453", "Sample");

        ZonedDateTime start = futureStart();
        ZonedDateTime end = futureEnd();

        Auction auction = repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);
        assertNotNull(auction);

        List<Item> results = repo.getAuctionItemsByGenre(action);
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void getAuctionItemsByGenreReturnsCopyOfList() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Item item = itemFor(action, "9789896710453", "Sample");

        ZonedDateTime start = futureStart();
        ZonedDateTime end = futureEnd();

        repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> first = repo.getAuctionItemsByGenre(action);
        // modify returned list
        first.clear();

        // original should remain present
        List<Item> second = repo.getAuctionItemsByGenre(action);
        assertEquals(1, second.size());
    }

    @Test
    void createAuctionThrowsWhenStartDateIsNotInFuture() {
        AuctionRepo repo = new AuctionRepo();

        Genre g = new Genre("X");
        Item item = itemFor(g, "9789896710453", "Sample");

        ZonedDateTime start = ZonedDateTime.now().minusDays(1);
        ZonedDateTime end = futureStart();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> repo.createAuction(item, new Price(5.0, Currency.EUR), start, end));
        assertEquals("Invalid start date", ex.getMessage());
    }

    @Test
    void createAuctionThrowsWhenEndDateBeforeStartDate() {
        AuctionRepo repo = new AuctionRepo();

        Genre g = new Genre("Y");
        Item item = itemFor(g, "9789896710453", "Sample");

        ZonedDateTime start = futureEnd();
        ZonedDateTime end = futureStart();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> repo.createAuction(item, new Price(5.0, Currency.EUR), start, end));
        assertEquals("Invalid end date", ex.getMessage());
    }

    @Test
    void getAuctionItemsByGenreFiltersCorrectlyAndIsCaseInsensitive() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Genre romance = new Genre("Romance");

        Item itemA = itemFor(action, "0306406152", "A", "A", "P");
        Item itemB = itemFor(romance, "9789896710453", "B", "B", "P");

        ZonedDateTime start = futureStart();
        ZonedDateTime end = futureEnd();

        repo.createAuction(itemA, new Price(1.0, Currency.EUR), start, end);
        repo.createAuction(itemB, new Price(1.0, Currency.EUR), start, end);

        List<Item> actionResult = repo.getAuctionItemsByGenre(new Genre("ACTION"));
        assertEquals(1, actionResult.size());
        assertSame(itemA, actionResult.get(0));

        List<Item> romanceResult = repo.getAuctionItemsByGenre(romance);
        assertEquals(1, romanceResult.size());
        assertSame(itemB, romanceResult.get(0));
    }

    @Test
    void getAuctionItemsByGenreWithNullGenreReturnsEmptyList() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Item item = itemFor(action, "9789896710453", "Sample");

        ZonedDateTime start = futureStart();
        ZonedDateTime end = futureEnd();

        repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> results = repo.getAuctionItemsByGenre(null);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsMatchingItems() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Author author = new Author("Author A");
        Item item = itemFor(action, "9789896710453", "Sample", author.getName(), "Pub");

        ZonedDateTime start = futureStart();
        ZonedDateTime end = futureEnd();

        repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> results = repo.getAuctionItemsByAuthor(author);
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void getAuctionItemsByAuthorIsCaseInsensitive() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Author author = new Author("Author A");
        Item item = itemFor(action, "9789896710453", "Sample", author.getName(), "Pub");

        ZonedDateTime start = futureStart();
        ZonedDateTime end = futureEnd();

        repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> results = repo.getAuctionItemsByAuthor(new Author("author a"));
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void getAuctionItemsByAuthorReturnsEmptyListWhenNoMatch() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Author author = new Author("Author A");
        Item item = itemFor(action, "9789896710453", "Sample", author.getName(), "Pub");

        ZonedDateTime start = futureStart();
        ZonedDateTime end = futureEnd();

        repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> results = repo.getAuctionItemsByAuthor(new Author("Other"));
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsDefensiveCopy() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Author author = new Author("Author A");
        Item item = itemFor(action, "9789896710453", "Sample", author.getName(), "Pub");

        ZonedDateTime start = futureStart();
        ZonedDateTime end = futureEnd();

        repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> first = repo.getAuctionItemsByAuthor(author);
        first.clear();

        List<Item> second = repo.getAuctionItemsByAuthor(author);
        assertEquals(1, second.size());
    }

    @Test
    void shouldReturnAllItemsForGivenPublication() {
        // Arrange
        _repo.createAuction(_item1, _startingPrice, _start, _end);
        _repo.createAuction(_item2, _startingPrice, _start, _end);
        _repo.createAuction(_item3, _startingPrice, _start, _end);

        // Act
        List<Item> result = _repo.getAuctionItemsByPublication(_pub1);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_item1));
        assertTrue(result.contains(_item2));
        assertFalse(result.contains(_item3));
    }

    @Test
    void shouldReturnEmptyListWhenNoAuctionMatchesPublication() {
        // Arrange
        _repo.createAuction(_item3, _startingPrice, _start, _end); // only pub2 item

        // Act
        List<Item> result = _repo.getAuctionItemsByPublication(_pub1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenRepoIsEmpty() {
        // Act
        List<Item> result = _repo.getAuctionItemsByPublication(_pub1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
