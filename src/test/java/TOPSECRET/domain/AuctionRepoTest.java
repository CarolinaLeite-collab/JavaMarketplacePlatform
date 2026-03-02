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
    private Genre _genre;
    private Price _startingPrice;
    private ZonedDateTime _start;
    private ZonedDateTime _end;

    @BeforeEach
    void setUp() {
        _repo = new AuctionRepo();

        _genre = new Genre("action");

        _pub1 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("1111111111"))
                .year(Year.of(2020))
                .title(new Title("Architectonica Percepta"))
                .author(new Author("Paulo Providência"))
                .publisher(new PublishingCompany("Park Books"))
                .genre(_genre)
                .build();

        _pub2 = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("838894522X"))
                .year(Year.of(1980))
                .title(new Title("Louis I. Khan: The idea of order"))
                .author(new Author("Klaus-Peter Gast"))
                .publisher(new PublishingCompany("Birkhauser"))
                .genre(_genre)
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

    @Test
    void testGetAuctionItemsByGenreNoAuctionShouldReturnEmptyList() {

        //act
        List<Item> emptyList = _repo.getAuctionItemsByGenre(_genre);

        //assert
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

    @Test
    void testGetAuctionItemsByGenreWithAuctionsShouldReturnNonEmptyList() {

        //arrange
        _repo.createAuction(_item1, _startingPrice, _start, _end);
        _repo.createAuction(_item3, new Price(25.0, Currency.EUR), _start, _end);

        //act
        List<Item> list = _repo.getAuctionItemsByGenre(_genre);

        //assert
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }


    @Test
    void createAuctionAddsAuctionAndCanBeRetrievedByGenre() {
        AuctionRepo repo = new AuctionRepo();

        Genre action = new Genre("Action");
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(new Author(defaultAuthorName))
                .publisher(new PublishingCompany(defaultPublisherName))
                .genre(action)
                .build();
        Item item = new Item(_pub1, Condition.GOOD);

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
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(new Author(defaultAuthorName))
                .publisher(new PublishingCompany(defaultPublisherName))
                .genre(action)
                .build();
        Item item = new Item(publication, Condition.GOOD);

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
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(new Author(defaultAuthorName))
                .publisher(new PublishingCompany(defaultPublisherName))
                .genre(g)
                .build();
        Item item = new Item(publication, Condition.GOOD);

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
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(new Author(defaultAuthorName))
                .publisher(new PublishingCompany(defaultPublisherName))
                .genre(g)
                .build();
        Item item = new Item(publication, Condition.GOOD);

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
                .type(bookType)
                .identifier(new ISBN("0306406152"))
                .year(publicationYear)
                .title(new Title("A"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .genre(action)
                .build();
        Item itemA = new Item(pubA, Condition.GOOD);

        Publication pubB = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("B"))
                .author(new Author("B"))
                .publisher(new PublishingCompany("P"))
                .genre(romance)
                .build();
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
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(new Author(defaultAuthorName))
                .publisher(new PublishingCompany(defaultPublisherName))
                .genre(action)
                .build();
        Item item = new Item(publication, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

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
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(author)
                .publisher(new PublishingCompany("Pub"))
                .genre(action)
                .build();
        Item item = new Item(publication, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

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
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(author)
                .publisher(new PublishingCompany("Pub"))
                .genre(action)
                .build();
        Item item = new Item(publication, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

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
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(author)
                .publisher(new PublishingCompany("Pub"))
                .genre(action)
                .build();
        Item item = new Item(publication, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

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
        Publication publication = Publication.builder()
                .type(bookType)
                .identifier(new ISBN("9789896710453"))
                .year(publicationYear)
                .title(new Title("Sample"))
                .author(author)
                .publisher(new PublishingCompany("Pub"))
                .genre(action)
                .build();
        Item item = new Item(publication, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

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

    @Test
    void should_filter_correctly_and_be_case_insensitive() {
        AuctionRepo repo = new AuctionRepo();

        PublishingCompany publisher1 = new PublishingCompany("publisher1");
        PublishingCompany publisher2 = new PublishingCompany("publisher2");

        Publication pubA = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("0306406152"))
                .year(Year.of(2019))
                .title(new Title("A"))
                .author(new Author("A"))
                .publisher(publisher1)
                .genre(new Genre("action"))
                .build();

        Publication pubB = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9789896710453"))
                .year(Year.of(2019))
                .title(new Title("B"))
                .author(new Author("B"))
                .publisher(publisher2)
                .genre(new Genre("romance"))
                .build();

        Item itemA = new Item(pubA, Condition.GOOD);
        Item itemB = new Item(pubB, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        repo.createAuction(itemA, new Price(1.0, Currency.EUR), start, end);
        repo.createAuction(itemB, new Price(1.0, Currency.EUR), start, end);

        List<Item> item1 = repo.getAuctionItemsByPublisher(new PublishingCompany("pubLIshEr1"));
        assertEquals(1, item1.size());
        assertSame(itemA, item1.get(0));

        List<Item> item2 = repo.getAuctionItemsByPublisher(publisher2);
        assertEquals(1, item2.size());
        assertSame(itemB, item2.get(0));
    }
    @Test
    void should_return_empty_list_when_null_publisher() {
        AuctionRepo repo = new AuctionRepo();

        PublishingCompany publisher1 = new PublishingCompany("publisher1");
        Publication pubA = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("0306406152"))
                .year(Year.of(2019))
                .title(new Title("A"))
                .author(new Author("A"))
                .publisher(publisher1)
                .genre(new Genre("action"))
                .build();

        Item item = new Item(pubA, Condition.GOOD);

        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        List<Item> item3 = repo.getAuctionItemsByPublisher(null);
        assertNotNull(item3);
        assertTrue(item3.isEmpty());
    }
}
