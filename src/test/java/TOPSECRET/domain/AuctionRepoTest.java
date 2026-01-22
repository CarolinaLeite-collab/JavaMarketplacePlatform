package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuctionRepoTest {

    private PublicationType bookType;
    private Year publicationYear;
    private String defaultAuthorName;
    private String defaultPublisherName;

    @BeforeEach
    void setUp() {
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
}
