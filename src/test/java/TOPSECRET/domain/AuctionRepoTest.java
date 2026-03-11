package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionRepoTest {

    private AuctionRepo repo;
    private AuctionFactory auctionFactory;

    private Publication pub1;
    private Publication pub2;
    private Item item1;
    private Item item2;
    private Item item3;
    private Genre genre;
    private Price startingPrice;
    private ZonedDateTime start;
    private ZonedDateTime end;

    @BeforeEach
    void setUp() {
        auctionFactory = mock(AuctionFactory.class);
        repo = mockRepoWithFactory(auctionFactory);

        genre = mock(Genre.class);
        pub1 = mock(Publication.class);
        pub2 = mock(Publication.class);
        item1 = mock(Item.class);
        item2 = mock(Item.class);
        item3 = mock(Item.class);
        startingPrice = mock(Price.class);

        start = ZonedDateTime.of(2027, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        end = ZonedDateTime.of(2027, 2, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
    }

    private AuctionRepo mockRepoWithFactory(AuctionFactory factory) {
        return mock(AuctionRepo.class, withSettings()
                .useConstructor(factory)
                .defaultAnswer(Answers.CALLS_REAL_METHODS));
    }

    private Auction mockAuctionWithItem(Item item) {
        Auction auction = mock(Auction.class);
        when(auction.getItem()).thenReturn(item);
        return auction;
    }

    @Test
    void testGetAuctionItemsByGenreNoAuctionShouldReturnEmptyList() {
        // Arrange
        // (no auctions created)

        // Act
        List<Item> emptyList = repo.getAuctionItemsByGenre(genre);

        // Assert
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void testGetAuctionItemsByGenreWithAuctionsShouldReturnNonEmptyList() throws Exception {
        // Arrange
        Auction auction1 = mockAuctionWithItem(item1);
        Auction auction2 = mockAuctionWithItem(item3);
        Price price2 = mock(Price.class);

        when(auction1.isByGenre(genre)).thenReturn(true);
        when(auction2.isByGenre(genre)).thenReturn(true);

        when(auctionFactory.createAuction(item1, startingPrice, start, end)).thenReturn(auction1);
        when(auctionFactory.createAuction(item3, price2, start, end)).thenReturn(auction2);

        repo.createAuction(item1, startingPrice, start, end);
        repo.createAuction(item3, price2, start, end);

        // Act
        List<Item> list = repo.getAuctionItemsByGenre(genre);

        // Assert
        assertNotNull(list);
        assertFalse(list.isEmpty());
        verify(auction1).isByGenre(genre);
        verify(auction2).isByGenre(genre);
    }

    @Test
    void createAuctionAddsAuctionAndCanBeRetrievedByGenre() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Item item = mock(Item.class);
        Price price = mock(Price.class);
        Genre action = mock(Genre.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByGenre(action)).thenReturn(true);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        // Act
        Auction created = localRepo.createAuction(item, price, start, end);
        List<Item> results = localRepo.getAuctionItemsByGenre(action);

        // Assert
        assertNotNull(created);
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(factory).createAuction(item, price, start, end);
    }

    @Test
    void createAuctionWithOutrightAddsAuctionAndCanBeRetrievedByGenre() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Item item = mock(Item.class);
        Price price = mock(Price.class);
        Price outright = mock(Price.class);
        Genre action = mock(Genre.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByGenre(action)).thenReturn(true);
        when(factory.createAuction(item, price, outright, start, end)).thenReturn(auction);

        // Act
        Auction created = localRepo.createAuction(item, price, outright, start, end);
        List<Item> results = localRepo.getAuctionItemsByGenre(action);

        // Assert
        assertNotNull(created);
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(factory).createAuction(item, price, outright, start, end);
    }

    @Test
    void createAuctionThrowsWhenStartDateIsNotInFuture() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        ZonedDateTime badStart = ZonedDateTime.now().minusDays(1);
        ZonedDateTime goodEnd = ZonedDateTime.now().plusDays(1);

        when(factory.createAuction(item, price, badStart, goodEnd))
                .thenThrow(new InstantiationException("Invalid start date"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> localRepo.createAuction(item, price, badStart, goodEnd));

        // Assert
        assertTrue(ex.getMessage().contains("Invalid start date"));
        verify(factory).createAuction(item, price, badStart, goodEnd);
    }

    @Test
    void createAuctionThrowsWhenEndDateBeforeStartDate() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        ZonedDateTime goodStart = ZonedDateTime.now().plusDays(2);
        ZonedDateTime badEnd = ZonedDateTime.now().plusDays(1);

        when(factory.createAuction(item, price, goodStart, badEnd))
                .thenThrow(new InstantiationException("Invalid end date"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> localRepo.createAuction(item, price, goodStart, badEnd));

        // Assert
        assertTrue(ex.getMessage().contains("Invalid end date"));
        verify(factory).createAuction(item, price, goodStart, badEnd);
    }

    @Test
    void createAuctionWithOutrightThrowsWhenFactoryFails() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Item item = mock(Item.class);
        Price price = mock(Price.class);
        Price outright = mock(Price.class);

        when(factory.createAuction(item, price, outright, start, end))
                .thenThrow(new InstantiationException("outright failure"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> localRepo.createAuction(item, price, outright, start, end));

        // Assert
        assertTrue(ex.getMessage().contains("Unable to create auction"));
        verify(factory).createAuction(item, price, outright, start, end);
    }

    @Test
    void getAuctionItemsByGenreFiltersCorrectlyAndIsCaseInsensitive() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);

        Genre actionUpper = mock(Genre.class);
        Genre romance = mock(Genre.class);

        Item itemA = mock(Item.class);
        Item itemB = mock(Item.class);
        Price price = mock(Price.class);

        Auction auctionA = mockAuctionWithItem(itemA);
        Auction auctionB = mockAuctionWithItem(itemB);

        when(auctionA.isByGenre(actionUpper)).thenReturn(true);
        when(auctionB.isByGenre(romance)).thenReturn(true);

        when(factory.createAuction(itemA, price, start, end)).thenReturn(auctionA);
        when(factory.createAuction(itemB, price, start, end)).thenReturn(auctionB);

        localRepo.createAuction(itemA, price, start, end);
        localRepo.createAuction(itemB, price, start, end);

        // Act
        List<Item> actionResult = localRepo.getAuctionItemsByGenre(actionUpper);
        List<Item> romanceResult = localRepo.getAuctionItemsByGenre(romance);

        // Assert
        assertEquals(1, actionResult.size());
        assertSame(itemA, actionResult.get(0));

        assertEquals(1, romanceResult.size());
        assertSame(itemB, romanceResult.get(0));

        verify(auctionA).isByGenre(actionUpper);
        verify(auctionB).isByGenre(romance);
    }

    @Test
    void getAuctionItemsByGenreWithNullGenreReturnsEmptyList() throws Exception {
        // Arrange
        Auction auction = mockAuctionWithItem(item1);
        when(auctionFactory.createAuction(item1, startingPrice, start, end)).thenReturn(auction);

        repo.createAuction(item1, startingPrice, start, end);

        // Act
        List<Item> results = repo.getAuctionItemsByGenre(null);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(auction).isByGenre(null);
    }

    @Test
    void getAuctionItemsByAuthorReturnsMatchingItems() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Author author = mock(Author.class);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByAuthor(author)).thenReturn(true);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        localRepo.createAuction(item, price, start, end);

        // Act
        List<Item> results = localRepo.getAuctionItemsByAuthor(author);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(auction).isByAuthor(author);
    }

    @Test
    void getAuctionItemsByAuthorIsCaseInsensitive() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Author authorLower = mock(Author.class);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByAuthor(authorLower)).thenReturn(true);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        localRepo.createAuction(item, price, start, end);

        // Act
        List<Item> results = localRepo.getAuctionItemsByAuthor(authorLower);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(auction).isByAuthor(authorLower);
    }

    @Test
    void getAuctionItemsByAuthorReturnsEmptyListWhenNoMatch() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Author author = mock(Author.class);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByAuthor(author)).thenReturn(false);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        localRepo.createAuction(item, price, start, end);

        // Act
        List<Item> results = localRepo.getAuctionItemsByAuthor(author);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(auction).isByAuthor(author);
    }

    @Test
    void getAuctionItemsByAuthorReturnsDefensiveCopy() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);
        Author author = mock(Author.class);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByAuthor(author)).thenReturn(true);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        localRepo.createAuction(item, price, start, end);

        // Act
        List<Item> first = localRepo.getAuctionItemsByAuthor(author);
        first.clear();
        List<Item> second = localRepo.getAuctionItemsByAuthor(author);

        // Assert
        assertEquals(1, second.size());
    }

    @Test
    void shouldReturnAllItemsForGivenPublication() throws Exception {
        // Arrange
        Auction auction1 = mockAuctionWithItem(item1);
        Auction auction2 = mockAuctionWithItem(item2);
        Auction auction3 = mockAuctionWithItem(item3);

        when(auction1.isByPublication(pub1)).thenReturn(true);
        when(auction2.isByPublication(pub1)).thenReturn(true);
        when(auction3.isByPublication(pub1)).thenReturn(false);

        when(auctionFactory.createAuction(item1, startingPrice, start, end)).thenReturn(auction1);
        when(auctionFactory.createAuction(item2, startingPrice, start, end)).thenReturn(auction2);
        when(auctionFactory.createAuction(item3, startingPrice, start, end)).thenReturn(auction3);

        repo.createAuction(item1, startingPrice, start, end);
        repo.createAuction(item2, startingPrice, start, end);
        repo.createAuction(item3, startingPrice, start, end);

        // Act
        List<Item> result = repo.getAuctionItemsByPublication(pub1);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(item1));
        assertTrue(result.contains(item2));
        assertFalse(result.contains(item3));
    }

    @Test
    void shouldReturnEmptyListWhenNoAuctionMatchesPublication() throws Exception {
        // Arrange
        Auction auction = mockAuctionWithItem(item3);
        when(auction.isByPublication(pub1)).thenReturn(false);
        when(auctionFactory.createAuction(item3, startingPrice, start, end)).thenReturn(auction);

        repo.createAuction(item3, startingPrice, start, end);

        // Act
        List<Item> result = repo.getAuctionItemsByPublication(pub1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(auction).isByPublication(pub1);
    }

    @Test
    void shouldReturnEmptyListWhenRepoIsEmpty() {
        // Act
        List<Item> result = repo.getAuctionItemsByPublication(pub1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void should_filter_correctly_and_be_case_insensitive() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);

        PublishingCompany publisher1 = mock(PublishingCompany.class);
        PublishingCompany publisher2 = mock(PublishingCompany.class);

        Item itemA = mock(Item.class);
        Item itemB = mock(Item.class);
        Price price = mock(Price.class);

        Auction auctionA = mockAuctionWithItem(itemA);
        Auction auctionB = mockAuctionWithItem(itemB);

        when(auctionA.isByPublishingCompany(publisher1)).thenReturn(true);
        when(auctionB.isByPublishingCompany(publisher2)).thenReturn(true);

        when(factory.createAuction(itemA, price, start, end)).thenReturn(auctionA);
        when(factory.createAuction(itemB, price, start, end)).thenReturn(auctionB);

        localRepo.createAuction(itemA, price, start, end);
        localRepo.createAuction(itemB, price, start, end);

        // Act
        List<Item> itemList1 = localRepo.getAuctionItemsByPublishingCompany(publisher1);
        List<Item> itemList2 = localRepo.getAuctionItemsByPublishingCompany(publisher2);

        // Assert
        assertEquals(1, itemList1.size());
        assertSame(itemA, itemList1.get(0));

        assertEquals(1, itemList2.size());
        assertSame(itemB, itemList2.get(0));

        verify(auctionA).isByPublishingCompany(publisher1);
        verify(auctionB).isByPublishingCompany(publisher2);
    }

    @Test
    void should_return_empty_list_when_null_publisher() throws Exception {
        // Arrange
        Auction auction = mockAuctionWithItem(item1);
        when(auctionFactory.createAuction(item1, startingPrice, start, end)).thenReturn(auction);

        repo.createAuction(item1, startingPrice, start, end);

        // Act
        List<Item> itemList = repo.getAuctionItemsByPublishingCompany(null);

        // Assert
        assertNotNull(itemList);
        assertTrue(itemList.isEmpty());
        verify(auction).isByPublishingCompany(null);
    }

    @Test
    void createAuctionUsesFactoryAndStoresAuction() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);

        Item item = mock(Item.class);
        Price price = mock(Price.class);
        Genre localGenre = mock(Genre.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByGenre(localGenre)).thenReturn(true);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        // Act
        Auction created = localRepo.createAuction(item, price, start, end);

        // Assert
        assertSame(auction, created);
        verify(factory).createAuction(item, price, start, end);

        List<Item> results = localRepo.getAuctionItemsByGenre(localGenre);
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
    }

    @Test
    void createAuctionWithOutrightUsesFactoryAndStoresAuction() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);

        Item item = mock(Item.class);
        Price price = mock(Price.class);
        Price outright = mock(Price.class);

        Auction auction = mockAuctionWithItem(item);
        when(factory.createAuction(item, price, outright, start, end)).thenReturn(auction);

        // Act
        Auction created = localRepo.createAuction(item, price, outright, start, end);

        // Assert
        assertSame(auction, created);
        verify(factory).createAuction(item, price, outright, start, end);
    }

    @Test
    void createAuctionWrapsFactoryInstantiationException() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);

        Item item = mock(Item.class);
        Price price = mock(Price.class);

        when(factory.createAuction(item, price, start, end)).thenThrow(new InstantiationException("boom"));

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> localRepo.createAuction(item, price, start, end));

        // Assert
        assertTrue(ex.getMessage().contains("Unable to create auction"));
        verify(factory).createAuction(item, price, start, end);
    }

    @Test
    void getAuctionItemsByAuthorUsesAuctionPredicate() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);

        Author author = mock(Author.class);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByAuthor(author)).thenReturn(true);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        localRepo.createAuction(item, price, start, end);

        // Act
        List<Item> results = localRepo.getAuctionItemsByAuthor(author);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(auction).isByAuthor(author);
    }

    @Test
    void getAuctionItemsByPublisherUsesAuctionPredicate() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);

        PublishingCompany publisher = mock(PublishingCompany.class);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByPublishingCompany(publisher)).thenReturn(true);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        localRepo.createAuction(item, price, start, end);

        // Act
        List<Item> results = localRepo.getAuctionItemsByPublishingCompany(publisher);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(auction).isByPublishingCompany(publisher);
    }

    @Test
    void getAuctionItemsByPublicationUsesAuctionPredicate() throws Exception {
        // Arrange
        AuctionFactory factory = mock(AuctionFactory.class);
        AuctionRepo localRepo = mockRepoWithFactory(factory);

        Publication publication = mock(Publication.class);
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        Auction auction = mockAuctionWithItem(item);
        when(auction.isByPublication(publication)).thenReturn(true);
        when(factory.createAuction(item, price, start, end)).thenReturn(auction);

        localRepo.createAuction(item, price, start, end);

        // Act
        List<Item> results = localRepo.getAuctionItemsByPublication(publication);

        // Assert
        assertEquals(1, results.size());
        assertSame(item, results.get(0));
        verify(auction).isByPublication(publication);
    }

    // Isolated tests

    @Test
    void getAuctionItemsByAuthorShouldReturnListOfItemsByAuthor() throws InstantiationException {
        // Arrange
        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);
        Item itemDouble1 = mock(Item.class);
        Item itemDouble2 = mock(Item.class);
        Price startingPriceDouble = mock(Price.class);
        Author authorDouble = mock(Author.class);

        when(auctionFactory.createAuction(itemDouble1, startingPriceDouble, start, end))
                .thenReturn(auction1);
        when(auctionFactory.createAuction(itemDouble2, startingPriceDouble, start, end))
                .thenReturn(auction2);

        when(auction1.isByAuthor(authorDouble)).thenReturn(true);
        when(auction2.isByAuthor(authorDouble)).thenReturn(true);
        when(auction1.getItem()).thenReturn(itemDouble1);
        when(auction2.getItem()).thenReturn(itemDouble2);

        // Act
        repo.createAuction(itemDouble1, startingPriceDouble, start, end);
        repo.createAuction(itemDouble2, startingPriceDouble, start, end);

        List<Item> results = repo.getAuctionItemsByAuthor(authorDouble);

        // Assert
        assertEquals(2, results.size());
    }

    @Test
    void getAuctionItemsByAuthorShouldReturnEmptyListOfItemsIfNotByAuthor() throws InstantiationException {
        // Arrange
        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);
        Auction auction3 = mock(Auction.class);
        Item itemDouble1 = mock(Item.class);
        Item itemDouble2 = mock(Item.class);
        Item itemDouble3 = mock(Item.class);
        Price startingPriceDouble = mock(Price.class);
        Author authorDouble = mock(Author.class);

        when(auctionFactory.createAuction(itemDouble1, startingPriceDouble, start, end))
                .thenReturn(auction1);
        when(auctionFactory.createAuction(itemDouble2, startingPriceDouble, start, end))
                .thenReturn(auction2);
        when(auctionFactory.createAuction(itemDouble3, startingPriceDouble, start, end))
                .thenReturn(auction3);

        when(auction1.isByAuthor(authorDouble)).thenReturn(false);
        when(auction2.isByAuthor(authorDouble)).thenReturn(false);
        when(auction3.isByAuthor(authorDouble)).thenReturn(false);
        when(auction1.getItem()).thenReturn(itemDouble1);
        when(auction2.getItem()).thenReturn(itemDouble2);
        when(auction3.getItem()).thenReturn(itemDouble3);

        // Act
        repo.createAuction(itemDouble1, startingPriceDouble, start, end);
        repo.createAuction(itemDouble2, startingPriceDouble, start, end);
        repo.createAuction(itemDouble3, startingPriceDouble, start, end);

        List<Item> results = repo.getAuctionItemsByAuthor(authorDouble);

        // Assert
        assertEquals(0, results.size());
    }

    @Test
    void getAuctionItemsByAuthorShouldListOfItemsOfOnlyItemsByAuthor() throws InstantiationException {
        // Arrange
        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);
        Auction auction3 = mock(Auction.class);
        Item itemDouble1 = mock(Item.class);
        Item itemDouble2 = mock(Item.class);
        Item itemDouble3 = mock(Item.class);
        Price startingPriceDouble = mock(Price.class);
        Author authorDouble = mock(Author.class);

        when(auctionFactory.createAuction(itemDouble1, startingPriceDouble, start, end))
                .thenReturn(auction1);
        when(auctionFactory.createAuction(itemDouble2, startingPriceDouble, start, end))
                .thenReturn(auction2);
        when(auctionFactory.createAuction(itemDouble3, startingPriceDouble, start, end))
                .thenReturn(auction3);

        when(auction1.isByAuthor(authorDouble)).thenReturn(true);
        when(auction2.isByAuthor(authorDouble)).thenReturn(true);
        when(auction3.isByAuthor(authorDouble)).thenReturn(false);
        when(auction1.getItem()).thenReturn(itemDouble1);
        when(auction2.getItem()).thenReturn(itemDouble2);
        when(auction3.getItem()).thenReturn(itemDouble3);

        // Act
        repo.createAuction(itemDouble1, startingPriceDouble, start, end);
        repo.createAuction(itemDouble2, startingPriceDouble, start, end);
        repo.createAuction(itemDouble3, startingPriceDouble, start, end);

        List<Item> results = repo.getAuctionItemsByAuthor(authorDouble);

        // Assert
        assertTrue(results.contains(itemDouble1));
        assertTrue(results.contains(itemDouble2));
        assertFalse(results.contains(itemDouble3));
        assertEquals(2, results.size());
    }
}
