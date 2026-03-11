package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleRepoTest {

    private DirectSaleFactory _factoryDouble;
    private DirectSaleRepo repo;

    private DirectSale _dsDouble1;
    private DirectSale _dsDouble2;

    private Item _itemDouble1;
    private Item _itemDouble2;

    private Author _authorDouble;
    private Genre _genreDouble;
    private PublishingCompany _pcDouble;
    private Publication _publicationDouble;
    private Period _periodDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(DirectSaleFactory.class);
        repo = new DirectSaleRepo(_factoryDouble);

        _dsDouble1 = mock(DirectSale.class);
        _dsDouble2 = mock(DirectSale.class);

        _itemDouble1 = mock(Item.class);
        _itemDouble2 = mock(Item.class);

        _authorDouble = mock(Author.class);
        _genreDouble = mock(Genre.class);
        _pcDouble = mock(PublishingCompany.class);
        _publicationDouble = mock(Publication.class);

        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        _periodDouble = mock(Period.class);
    }

    @Test
    void constructor_withFactory_doesNotThrow() {
        assertDoesNotThrow(() -> new DirectSaleRepo(_factoryDouble));
    }

    @Test
    void createDirectSale_callsFactoryAndStoresReturnedDirectSale() throws Exception {
        // Arrange
        Item item = mock(Item.class);
        Price price = mock(Price.class);
        Period timeLimit = null;

        when(_factoryDouble.createDirectSale(item, price, timeLimit)).thenReturn(_dsDouble1);
        when(_dsDouble1.isByAuthor(_authorDouble)).thenReturn(true); // para provar que foi guardado

        // Act
        DirectSale created = repo.createDirectSale(item, price, timeLimit);

        // Assert
        assertSame(_dsDouble1, created);
        verify(_factoryDouble, times(1)).createDirectSale(item, price, timeLimit);

        List<Item> items = repo.getDirectSaleItemsByAuthor(_authorDouble);
        assertEquals(1, items.size());
        assertSame(_itemDouble1, items.get(0));
    }

    @Test
    void createDirectSale_whenFactoryThrows_propagatesInstantiationException() throws Exception {
        // Arrange
        Item item = mock(Item.class);
        Price price = mock(Price.class);

        when(_factoryDouble.createDirectSale(eq(item), eq(price), isNull()))
                .thenThrow(new IllegalStateException("boom"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> repo.createDirectSale(item, price, null));
    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnListOfItemsByAuthor() throws InstantiationException {

        //Arrange
        DirectSale _directSaleDouble = mock(DirectSale.class);
        DirectSale _directSaleDouble2 = mock(DirectSale.class);
        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);
        Price _startingPriceDouble = mock(Price.class);
        Author _authorDouble = mock(Author.class);

        when(_factoryDouble.createDirectSale(_itemDouble1, _startingPriceDouble, _periodDouble))
                .thenReturn(_directSaleDouble);
        when(_factoryDouble.createDirectSale(_itemDouble2, _startingPriceDouble, _periodDouble))
                .thenReturn(_directSaleDouble2);

        when(_directSaleDouble.isByAuthor(_authorDouble)).thenReturn(true);
        when(_directSaleDouble2.isByAuthor(_authorDouble)).thenReturn(true);
        when(_directSaleDouble.getItem()).thenReturn(_itemDouble1);
        when(_directSaleDouble2.getItem()).thenReturn(_itemDouble2);

        //SUT
        DirectSaleRepo _dsr = new DirectSaleRepo(_factoryDouble);

        //Act
        _dsr.createDirectSale(_itemDouble1, _startingPriceDouble, _periodDouble);
        _dsr.createDirectSale(_itemDouble2, _startingPriceDouble, _periodDouble);

        List<Item> results = _dsr.getDirectSaleItemsByAuthor(_authorDouble);

        //Assert
        assertEquals(2, results.size());

    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnEmptyListOfItemsIfNotByAuthor() throws InstantiationException {

        //Arrange
        DirectSale _directSaleDouble = mock(DirectSale.class);
        DirectSale _directSaleDouble2 = mock(DirectSale.class);
        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);
        Price _startingPriceDouble = mock(Price.class);
        Author _authorDouble = mock(Author.class);

        when(_factoryDouble.createDirectSale(_itemDouble1, _startingPriceDouble, _periodDouble))
                .thenReturn(_directSaleDouble);
        when(_factoryDouble.createDirectSale(_itemDouble2, _startingPriceDouble, _periodDouble))
                .thenReturn(_directSaleDouble2);

        when(_directSaleDouble.isByAuthor(_authorDouble)).thenReturn(false);
        when(_directSaleDouble2.isByAuthor(_authorDouble)).thenReturn(false);
        when(_directSaleDouble.getItem()).thenReturn(_itemDouble1);
        when(_directSaleDouble2.getItem()).thenReturn(_itemDouble2);

        //SUT
        DirectSaleRepo _dsr = new DirectSaleRepo(_factoryDouble);

        //Act
        _dsr.createDirectSale(_itemDouble1, _startingPriceDouble, _periodDouble);
        _dsr.createDirectSale(_itemDouble2, _startingPriceDouble, _periodDouble);

        List<Item> results = _dsr.getDirectSaleItemsByAuthor(_authorDouble);

        //Assert
        assertTrue(results.isEmpty());

    }

    @Test
    void getAuctionItemsByAuthorShouldListOfItemsOfOnlyItemsByAuthor() throws InstantiationException {

        //Arrange
        DirectSale _directSaleDouble = mock(DirectSale.class);
        DirectSale _directSaleDouble2 = mock(DirectSale.class);
        DirectSale _directSaleDouble3 = mock(DirectSale.class);
        Item _itemDouble1 = mock(Item.class);
        Item _itemDouble2 = mock(Item.class);
        Item _itemDouble3 = mock(Item.class);
        Price _startingPriceDouble = mock(Price.class);
        Author _authorDouble = mock(Author.class);

        when(_factoryDouble.createDirectSale(_itemDouble1, _startingPriceDouble, _periodDouble))
                .thenReturn(_directSaleDouble);
        when(_factoryDouble.createDirectSale(_itemDouble2, _startingPriceDouble, _periodDouble))
                .thenReturn(_directSaleDouble2);
        when(_factoryDouble.createDirectSale(_itemDouble3, _startingPriceDouble, _periodDouble))
                .thenReturn(_directSaleDouble3);

        when(_directSaleDouble.isByAuthor(_authorDouble)).thenReturn(true);
        when(_directSaleDouble2.isByAuthor(_authorDouble)).thenReturn(true);
        when(_directSaleDouble3.isByAuthor(_authorDouble)).thenReturn(false);
        when(_directSaleDouble.getItem()).thenReturn(_itemDouble1);
        when(_directSaleDouble2.getItem()).thenReturn(_itemDouble2);
        when(_directSaleDouble3.getItem()).thenReturn(_itemDouble3);

        //SUT
        DirectSaleRepo _dsr = new DirectSaleRepo(_factoryDouble);

        //Act
        _dsr.createDirectSale(_itemDouble1, _startingPriceDouble, _periodDouble);
        _dsr.createDirectSale(_itemDouble2, _startingPriceDouble, _periodDouble);
        _dsr.createDirectSale(_itemDouble3, _startingPriceDouble, _periodDouble);

        List<Item> results = _dsr.getDirectSaleItemsByAuthor(_authorDouble);

        //Assert
        assertTrue(results.contains(_itemDouble1));
        assertTrue(results.contains(_itemDouble2));
        assertFalse(results.contains(_itemDouble3));
        assertEquals(2, results.size());

    }

    @Test
    void getDirectSaleItemsByPublication_filtersCorrectly() throws Exception {
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        repo.createDirectSale(mock(Item.class), mock(Price.class), null);
        repo.createDirectSale(mock(Item.class), mock(Price.class), null);

        when(_dsDouble1.isByPublication(_publicationDouble)).thenReturn(true);
        when(_dsDouble2.isByPublication(_publicationDouble)).thenReturn(false);

        List<Item> items = repo.getDirectSaleItemsByPublication(_publicationDouble);

        assertEquals(1, items.size());
        assertSame(_itemDouble1, items.get(0));
    }

    @Test
    void getDirectSaleItemByPublisher_filtersCorrectly() throws Exception {
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        repo.createDirectSale(mock(Item.class), mock(Price.class), null);
        repo.createDirectSale(mock(Item.class), mock(Price.class), null);

        when(_dsDouble1.isByPublisher(_pcDouble)).thenReturn(true);
        when(_dsDouble2.isByPublisher(_pcDouble)).thenReturn(true);

        List<Item> items = repo.getDirectSaleItemByPublisher(_pcDouble);

        assertEquals(2, items.size());
        assertSame(_itemDouble1, items.get(0));
        assertSame(_itemDouble2, items.get(1));
    }

    @Test
    void should_return_empty_list_when_no_direct_sales_match_genre() {

        // Arrange
        when(_dsDouble1.isByGenre(_genreDouble)).thenReturn(false);
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1);

        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);
        repo.createDirectSale(mock(Item.class), mock(Price.class), Period.ofDays(10));

        // Act
        List<Item> result = repo.getDirectSaleItemsByGenre(_genreDouble);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void should_return_items_matching_genre() {

        // Arrange
        when(_dsDouble1.isByGenre(_genreDouble)).thenReturn(true);
        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);

        when(_dsDouble2.isByGenre(_genreDouble)).thenReturn(true);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        repo.createDirectSale(mock(Item.class), mock(Price.class), Period.ofDays(10));
        repo.createDirectSale(mock(Item.class), mock(Price.class), Period.ofDays(5));

        // Act
        List<Item> result = repo.getDirectSaleItemsByGenre(_genreDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_itemDouble1));
        assertTrue(result.contains(_itemDouble2));

    }

}