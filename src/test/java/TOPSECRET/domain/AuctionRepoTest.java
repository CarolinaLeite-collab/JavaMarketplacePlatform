package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        // Arrange
        // (no auctions created)

        // Act
        List<Item> emptyList = _repo.getAuctionItemsByGenre(_genre);

        // Assert
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

    @Test
    void testGetAuctionItemsByGenreWithAuctionsShouldReturnNonEmptyList() {

        // Arrange
        _repo.createAuction(_item1, _startingPrice, _start, _end);
        _repo.createAuction(_item3, new Price(25.0, Currency.EUR), _start, _end);

        // Act
        List<Item> list = _repo.getAuctionItemsByGenre(_genre);

        // Assert
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }


    @Test
    void createAuctionAddsAuctionAndCanBeRetrievedByGenre() {
        // Arrange
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

        // Act
        Auction auction = repo.createAuction(item, new Price(10.0, Currency.EUR), start, end);

        // Assert
        assertNotNull(auction);

        List<Item> results = repo.getAuctionItemsByGenre(action);
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void createAuctionWithOutrightAddsAuctionAndCanBeRetrievedByGenre() {
        // Arrange
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

        // Act
        Auction auction = repo.createAuction(item, new Price(10.0, Currency.EUR), new Price(20.0, Currency.EUR), start, end);

        // Assert
        assertNotNull(auction);

        List<Item> results = repo.getAuctionItemsByGenre(action);
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void createAuctionThrowsWhenStartDateIsNotInFuture() {
        // Arrange
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

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> repo.createAuction(item, new Price(5.0, Currency.EUR), start, end));

        // Assert
        assertTrue(ex.getMessage().contains("Invalid start date"));
    }

    @Test
    void createAuctionThrowsWhenEndDateBeforeStartDate() {
        // Arrange
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

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> repo.createAuction(item, new Price(5.0, Currency.EUR), start, end));

        // Assert
        assertTrue(ex.getMessage().contains("Invalid end date"));
    }

    @Test
    void getAuctionItemsByGenreFiltersCorrectlyAndIsCaseInsensitive() {
        // Arrange
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

        // Act
        List<Item> actionResult = repo.getAuctionItemsByGenre(new Genre("ACTION"));
        List<Item> romanceResult = repo.getAuctionItemsByGenre(romance);

        // Assert
        assertEquals(1, actionResult.size());
        assertSame(itemA, actionResult.get(0));

        assertEquals(1, romanceResult.size());
        assertSame(itemB, romanceResult.get(0));
    }

    @Test
    void getAuctionItemsByGenreWithNullGenreReturnsEmptyList() {
        // Arrange
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

        // Act
        List<Item> results = repo.getAuctionItemsByGenre(null);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsMatchingItems() {
        // Arrange
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

        // Act
        List<Item> results = repo.getAuctionItemsByAuthor(author);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void getAuctionItemsByAuthorIsCaseInsensitive() {
        // Arrange
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

        // Act
        List<Item> results = repo.getAuctionItemsByAuthor(new Author("author a"));

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void getAuctionItemsByAuthorReturnsEmptyListWhenNoMatch() {
        // Arrange
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

        // Act
        List<Item> results = repo.getAuctionItemsByAuthor(new Author("Other"));

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getAuctionItemsByAuthorReturnsDefensiveCopy() {
        // Arrange
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

        // Act
        List<Item> first = repo.getAuctionItemsByAuthor(author);
        first.clear();

        List<Item> second = repo.getAuctionItemsByAuthor(author);

        // Assert
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
        // Arrange
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

        // Act
        List<Item> item1 = repo.getAuctionItemsByPublisher(new PublishingCompany("pubLIshEr1"));
        List<Item> item2 = repo.getAuctionItemsByPublisher(publisher2);

        // Assert
        assertEquals(1, item1.size());
        assertSame(itemA, item1.get(0));

        assertEquals(1, item2.size());
        assertSame(itemB, item2.get(0));
    }
    @Test
    void should_return_empty_list_when_null_publisher() {
        // Arrange
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

        // Act
        List<Item> item3 = repo.getAuctionItemsByPublisher(null);

        // Assert
        assertNotNull(item3);
        assertTrue(item3.isEmpty());
    }

    @Test
    void createAuctionUsesFactoryAndStoresAuction() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo repo = new AuctionRepo(factory);

        Item item = mock(Item.class);
        Price price = new Price(5.0, Currency.EUR);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);
        Genre genre = new Genre("X");

        Auction auction = mock(Auction.class);
        when(auction.isByGenre(genre)).thenReturn(true);
        when(auction.getItem()).thenReturn(item);
        when(factory.create(item, price, start, end)).thenReturn(auction);

        // Act
        Auction created = repo.createAuction(item, price, start, end);

        // Assert
        assertSame(auction, created);
        verify(factory).create(item, price, start, end);

        List<Item> results = repo.getAuctionItemsByGenre(genre);
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void createAuctionWithOutrightUsesFactoryAndStoresAuction() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo repo = new AuctionRepo(factory);

        Item item = mock(Item.class);
        Price price = new Price(5.0, Currency.EUR);
        Price outright = new Price(15.0, Currency.EUR);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        Auction auction = mock(Auction.class);
        when(factory.create(item, price, outright, start, end)).thenReturn(auction);

        // Act
        Auction created = repo.createAuction(item, price, outright, start, end);

        // Assert
        assertSame(auction, created);
        verify(factory).create(item, price, outright, start, end);
    }

    @Test
    void createAuctionWrapsFactoryInstantiationException() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo repo = new AuctionRepo(factory);

        Item item = mock(Item.class);
        Price price = new Price(5.0, Currency.EUR);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        when(factory.create(item, price, start, end)).thenThrow(new InstantiationException("boom"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> repo.createAuction(item, price, start, end));

        // Assert
        assertTrue(ex.getMessage().contains("Unable to create auction"));
    }

    @Test
    void getAuctionItemsByAuthorUsesAuctionPredicate() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo repo = new AuctionRepo(factory);

        Author author = new Author("Author X");
        Item item = mock(Item.class);
        Price price = new Price(5.0, Currency.EUR);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        Auction auction = mock(Auction.class);
        when(auction.isByAuthor(author)).thenReturn(true);
        when(auction.getItem()).thenReturn(item);
        when(factory.create(item, price, start, end)).thenReturn(auction);

        repo.createAuction(item, price, start, end);

        // Act
        List<Item> results = repo.getAuctionItemsByAuthor(author);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(auction).isByAuthor(author);
    }

    @Test
    void getAuctionItemsByPublisherUsesAuctionPredicate() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo repo = new AuctionRepo(factory);

        PublishingCompany publisher = new PublishingCompany("Publisher X");
        Item item = mock(Item.class);
        Price price = new Price(5.0, Currency.EUR);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        Auction auction = mock(Auction.class);
        when(auction.isByPublisher(publisher)).thenReturn(true);
        when(auction.getItem()).thenReturn(item);
        when(factory.create(item, price, start, end)).thenReturn(auction);

        repo.createAuction(item, price, start, end);

        // Act
        List<Item> results = repo.getAuctionItemsByPublisher(publisher);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(auction).isByPublisher(publisher);
    }

    @Test
    void getAuctionItemsByPublicationUsesAuctionPredicate() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo repo = new AuctionRepo(factory);

        Publication publication = mock(Publication.class);
        Item item = mock(Item.class);
        Price price = new Price(5.0, Currency.EUR);
        ZonedDateTime start = ZonedDateTime.now().plusDays(1);
        ZonedDateTime end = ZonedDateTime.now().plusDays(2);

        Auction auction = mock(Auction.class);
        when(auction.isByPublication(publication)).thenReturn(true);
        when(auction.getItem()).thenReturn(item);
        when(factory.create(item, price, start, end)).thenReturn(auction);

        repo.createAuction(item, price, start, end);

        // Act
        List<Item> results = repo.getAuctionItemsByPublication(publication);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(auction).isByPublication(publication);
    }
}
