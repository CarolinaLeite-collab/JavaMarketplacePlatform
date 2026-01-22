package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuctionRepoTest {

    @Test
    void createAuctionAddsAuctionAndCanBeRetrievedByGenre() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("Sample"))
                .author(new Author("Someone"))
                .publisher(new Publisher("SomePub"))
                .genre(action)
                .build();

        Item item = new Item(pub, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

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
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("Sample"))
                .author(new Author("Someone"))
                .publisher(new Publisher("SomePub"))
                .genre(action)
                .build();

        Item item = new Item(pub, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

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
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("Sample"))
                .author(new Author("Someone"))
                .publisher(new Publisher("SomePub"))
                .genre(g)
                .build();

        Item item = new Item(pub, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().minusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> repo.createAuction(item, new Price(5.0, Currency.EUR), start, end));
        assertEquals("Invalid start date", ex.getMessage());
    }

    @Test
    void createAuctionThrowsWhenEndDateBeforeStartDate() {
        AuctionRepo repo = new AuctionRepo();

        Genre g = new Genre("Y");
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("Sample"))
                .author(new Author("Someone"))
                .publisher(new Publisher("SomePub"))
                .genre(g)
                .build();

        Item item = new Item(pub, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(2);
        ZonedDateTime end = ZonedDateTime.now().plusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> repo.createAuction(item, new Price(5.0, Currency.EUR), start, end));
        assertEquals("Invalid end date", ex.getMessage());
    }

    @Test
    void getAuctionItemsByGenreFiltersCorrectlyAndIsCaseInsensitive() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Genre romance = new Genre("Romance");

        Publication pubA = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("0306406152"))
                .year(Year.of(2019))
                .title(new Title("A"))
                .author(new Author("A"))
                .publisher(new Publisher("P"))
                .genre(action)
                .build();

        Publication pubB = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("B"))
                .author(new Author("B"))
                .publisher(new Publisher("P"))
                .genre(romance)
                .build();

        Item itemA = new Item(pubA, Condition.GOOD);
        Item itemB = new Item(pubB, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

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
        Publication pub = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("Sample"))
                .author(new Author("Someone"))
                .publisher(new Publisher("SomePub"))
                .genre(action)
                .build();

        Item item = new Item(pub, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> results = repo.getAuctionItemsByGenre(null);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}
