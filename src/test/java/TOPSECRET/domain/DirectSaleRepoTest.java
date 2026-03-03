package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleRepoTest {

    private DirectSaleFactory factory;
    private DirectSaleRepo repo;

    private DirectSale ds1;
    private DirectSale ds2;

    private Item item1;
    private Item item2;

    private Author author;
    private Genre genre;
    private PublishingCompany publisher;
    private Publication publication;

    @BeforeEach
    void setUp() {
        factory = mock(DirectSaleFactory.class);
        repo = new DirectSaleRepo(factory);

        ds1 = mock(DirectSale.class);
        ds2 = mock(DirectSale.class);

        item1 = mock(Item.class);
        item2 = mock(Item.class);

        author = mock(Author.class);
        genre = mock(Genre.class);
        publisher = mock(PublishingCompany.class);
        publication = mock(Publication.class);

        when(ds1.getItem()).thenReturn(item1);
        when(ds2.getItem()).thenReturn(item2);
    }

    @Test
    void constructor_withFactory_doesNotThrow() {
        assertDoesNotThrow(() -> new DirectSaleRepo(factory));
    }

    @Test
    void createDirectSale_callsFactoryAndStoresReturnedDirectSale() throws Exception {
        // Arrange
        Item item = mock(Item.class);
        Price price = mock(Price.class);
        Period timeLimit = null;

        when(factory.create(item, price, timeLimit)).thenReturn(ds1);
        when(ds1.isByAuthor(author)).thenReturn(true); // para provar que foi guardado

        // Act
        DirectSale created = repo.createDirectSale(item, price, timeLimit);

        // Assert
        assertSame(ds1, created);
        verify(factory, times(1)).create(item, price, timeLimit);

        List<Item> items = repo.getDirectSaleItemsByAuthor(author);
        assertEquals(1, items.size());
        assertSame(item1, items.get(0));
    }

    @Test
    void createDirectSale_whenFactoryThrows_propagatesInstantiationException() throws Exception {
        // Arrange
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        when(factory.create(eq(item), eq(price), isNull()))
                .thenThrow(new InstantiationException("boom"));

        // Act & Assert
        assertThrows(InstantiationException.class, () -> repo.createDirectSale(item, price, null));
    }

    @Test
    void getDirectSaleItemsByAuthor_noSales_returnsEmptyUnmodifiableList() {
        List<Item> items = repo.getDirectSaleItemsByAuthor(author);

        assertNotNull(items);
        assertTrue(items.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> items.add(mock(Item.class)));
    }

    @Test
    void getDirectSaleItemsByAuthor_filtersCorrectly() throws Exception {
        // Arrange: meter 2 directSales lá dentro via factory
        when(factory.create(any(), any(), any())).thenReturn(ds1, ds2);

        repo.createDirectSale(mock(Item.class), mock(Price.class), null);
        repo.createDirectSale(mock(Item.class), mock(Price.class), null);

        when(ds1.isByAuthor(author)).thenReturn(true);
        when(ds2.isByAuthor(author)).thenReturn(false);

        // Act
        List<Item> items = repo.getDirectSaleItemsByAuthor(author);

        // Assert
        assertEquals(1, items.size());
        assertSame(item1, items.get(0));
    }

    @Test
    void getDirectSaleItemsByPublication_filtersCorrectly() throws Exception {
        when(factory.create(any(), any(), any())).thenReturn(ds1, ds2);

        repo.createDirectSale(mock(Item.class), mock(Price.class), null);
        repo.createDirectSale(mock(Item.class), mock(Price.class), null);

        when(ds1.isByPublication(publication)).thenReturn(true);
        when(ds2.isByPublication(publication)).thenReturn(false);

        List<Item> items = repo.getDirectSaleItemsByPublication(publication);

        assertEquals(1, items.size());
        assertSame(item1, items.get(0));
    }

    @Test
    void getDirectSaleItemByPublisher_filtersCorrectly() throws Exception {
        when(factory.create(any(), any(), any())).thenReturn(ds1, ds2);

        repo.createDirectSale(mock(Item.class), mock(Price.class), null);
        repo.createDirectSale(mock(Item.class), mock(Price.class), null);

        when(ds1.isByPublisher(publisher)).thenReturn(true);
        when(ds2.isByPublisher(publisher)).thenReturn(true);

        List<Item> items = repo.getDirectSaleItemByPublisher(publisher);

        assertEquals(2, items.size());
        assertSame(item1, items.get(0));
        assertSame(item2, items.get(1));
    }

    @Test
    void getDirectSaleItemsByGenre_usesPublicationMatchGenre() throws Exception {
        when(factory.create(any(), any(), any())).thenReturn(ds1);

        // ds1.getItem() -> item1 (já stubbed)
        when(item1.getPublication()).thenReturn(publication);
        when(publication.matchGenre(genre)).thenReturn(true);

        repo.createDirectSale(mock(Item.class), mock(Price.class), null);

        List<Item> items = repo.getDirectSaleItemsByGenre(genre);

        assertEquals(1, items.size());
        assertSame(item1, items.get(0));
    }
}