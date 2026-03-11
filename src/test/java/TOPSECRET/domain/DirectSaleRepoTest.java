package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleRepoTest {

    private DirectSaleFactory _factoryDouble;
    private DirectSale _dsDouble1;
    private DirectSale _dsDouble2;
    private Item _itemDouble1;
    private Item _itemDouble2;
    private Publication _publicationDouble;
    private Period _periodDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(DirectSaleFactory.class);
        _dsDouble1 = mock(DirectSale.class);
        _dsDouble2 = mock(DirectSale.class);
        _itemDouble1 = mock(Item.class);
        _itemDouble2 = mock(Item.class);
        _publicationDouble = mock(Publication.class);
        _periodDouble = mock(Period.class);

    }

    @Test
    void constructorShouldBuildDirectSaleRepo() {

        // Act & SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Assert
        assertNotNull(dsr);
    }

    @Test
    void addDirectSaleShouldReturnDirectSaleFromFactory() {

        // Arrange
        Price priceDouble = mock(Price.class);
        when(_factoryDouble.createDirectSale(_itemDouble1, priceDouble, _periodDouble)).thenReturn(_dsDouble1);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act
        DirectSale created = dsr.addDirectSale(_itemDouble1, priceDouble, _periodDouble);

        // Assert
        assertSame(_dsDouble1, created);
        verify(_factoryDouble, times(1)).createDirectSale(_itemDouble1, priceDouble, _periodDouble);

    }

   @Test
    void addDirectSaleShouldPropagateExceptionFromFactory() {

        // Arrange
        Price priceDouble = mock(Price.class);
        when(_factoryDouble.createDirectSale(eq(_itemDouble1), eq(priceDouble), isNull()))
                .thenThrow(new IllegalStateException("boom"));

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> dsr.addDirectSale(_itemDouble1, priceDouble, null));
    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnListOfItemsByAuthor() {

        //Arrange
        Price priceDouble = mock(Price.class);
        Author authorDouble = mock(Author.class);
        when(_factoryDouble.createDirectSale(_itemDouble1, priceDouble, _periodDouble))
                .thenReturn(_dsDouble1);
        when(_factoryDouble.createDirectSale(_itemDouble2, priceDouble, _periodDouble))
                .thenReturn(_dsDouble2);

        when(_dsDouble1.isByAuthor(authorDouble)).thenReturn(true);
        when(_dsDouble2.isByAuthor(authorDouble)).thenReturn(true);
        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_itemDouble1, priceDouble, _periodDouble);
        dsr.addDirectSale(_itemDouble2, priceDouble, _periodDouble);

        List<Item> result = dsr.getDirectSaleItemsByAuthor(authorDouble);

        //Assert
        assertEquals(2, result.size());

    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnEmptyListOfItemsIfNotByAuthor() {

        //Arrange
        Price startingPriceDouble = mock(Price.class);
        Author authorDouble = mock(Author.class);

        when(_factoryDouble.createDirectSale(_itemDouble1, startingPriceDouble, _periodDouble))
                .thenReturn(_dsDouble1);
        when(_factoryDouble.createDirectSale(_itemDouble2, startingPriceDouble, _periodDouble))
                .thenReturn(_dsDouble2);

        when(_dsDouble1.isByAuthor(authorDouble)).thenReturn(false);
        when(_dsDouble2.isByAuthor(authorDouble)).thenReturn(false);
        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        //Act
        dsr.addDirectSale(_itemDouble1, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(_itemDouble2, startingPriceDouble, _periodDouble);

        List<Item> results = dsr.getDirectSaleItemsByAuthor(authorDouble);

        //Assert
        assertTrue(results.isEmpty());

    }

    @Test
    void getDirectSaleItemsByAuthorShouldListOfItemsOfOnlyItemsByAuthor(){

        //Arrange
        DirectSale dsDouble3 = mock(DirectSale.class);
        Item itemDouble3 = mock(Item.class);
        Price startingPriceDouble = mock(Price.class);
        Author authorDouble = mock(Author.class);

        when(_factoryDouble.createDirectSale(_itemDouble1, startingPriceDouble, _periodDouble))
                .thenReturn(_dsDouble1);
        when(_factoryDouble.createDirectSale(_itemDouble2, startingPriceDouble, _periodDouble))
                .thenReturn(_dsDouble2);
        when(_factoryDouble.createDirectSale(itemDouble3, startingPriceDouble, _periodDouble))
                .thenReturn(dsDouble3);

        when(_dsDouble1.isByAuthor(authorDouble)).thenReturn(true);
        when(_dsDouble2.isByAuthor(authorDouble)).thenReturn(true);
        when(dsDouble3.isByAuthor(authorDouble)).thenReturn(false);
        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);
        when(dsDouble3.getItem()).thenReturn(itemDouble3);

        //SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        //Act
        dsr.addDirectSale(_itemDouble1, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(_itemDouble2, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(itemDouble3, startingPriceDouble, _periodDouble);

        List<Item> results = dsr.getDirectSaleItemsByAuthor(authorDouble);

        //Assert
        assertTrue(results.contains(_itemDouble1));
        assertTrue(results.contains(_itemDouble2));
        assertFalse(results.contains(itemDouble3));
        assertEquals(2, results.size());

    }

    @Test
    void getDirectSaleItemsByPublicationFiltersCorrectly(){

        // Arrange
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        when(_dsDouble1.isByPublication(_publicationDouble)).thenReturn(true);
        when(_dsDouble2.isByPublication(_publicationDouble)).thenReturn(false);

        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act

        dsr.addDirectSale(mock(Item.class), mock(Price.class), null);
        dsr.addDirectSale(mock(Item.class), mock(Price.class), null);

        List<Item> items = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        // Assert
        assertEquals(1, items.size());
        assertSame(_itemDouble1, items.get(0));
    }

    @Test
    void getDirectSaleItemsByPublisherFiltersCorrectly(){

        // Arrange
        PublishingCompany pcDouble = mock(PublishingCompany.class);
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        when(_dsDouble1.isByPublisher(pcDouble)).thenReturn(true);
        when(_dsDouble2.isByPublisher(pcDouble)).thenReturn(true);

        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(mock(Item.class), mock(Price.class), null);
        dsr.addDirectSale(mock(Item.class), mock(Price.class), null);

        List<Item> items = dsr.getDirectSaleItemsByPublisher(pcDouble);

        // Assert
        assertEquals(2, items.size());
        assertSame(_itemDouble1, items.get(0));
        assertSame(_itemDouble2, items.get(1));
    }

    @Test
    void getDirectSaleItemsByGenreShouldReturnEmptyListWhenNoneMatch() {

        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(_dsDouble1.isByGenre(genreDouble)).thenReturn(false);
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(mock(Item.class), mock(Price.class), Period.ofDays(10));
        List<Item> result = dsr.getDirectSaleItemsByGenre(genreDouble);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void getDirectSaleItemsByGenreShouldReturnAllItemsWhenAllMatch() {

        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(_dsDouble1.isByGenre(genreDouble)).thenReturn(true);
        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);

        when(_dsDouble2.isByGenre(genreDouble)).thenReturn(true);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(mock(Item.class), mock(Price.class), Period.ofDays(10));
        dsr.addDirectSale(mock(Item.class), mock(Price.class), Period.ofDays(5));

        List<Item> result = dsr.getDirectSaleItemsByGenre(genreDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_itemDouble1));
        assertTrue(result.contains(_itemDouble2));

    }

    // Isolated tests for US023

    @Test
    void getDirectSaleItemsByPublicationShouldReturnItemsOfThatPublication() {

        // Arrange
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        when(_dsDouble1.isByPublication(_publicationDouble)).thenReturn(true);
        when(_dsDouble2.isByPublication(_publicationDouble)).thenReturn(true);

        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_itemDouble1, mock(Price.class), null);
        dsr.addDirectSale(_itemDouble2, mock(Price.class), null);

        List<Item> resultsList = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        //Assert
        assertEquals(2, resultsList.size());
        assertTrue(resultsList.containsAll(List.of(_itemDouble1, _itemDouble2)));

    }

    @Test
    void getDirectSaleItemsByPublicationShouldReturnEmptyAndUnmodifiableListWhenNoDirectSalesMatch() {

        // Arrange
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        when(_dsDouble1.isByPublication(_publicationDouble)).thenReturn(false);
        when(_dsDouble2.isByPublication(_publicationDouble)).thenReturn(false);

        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);
        when(_dsDouble2.getItem()).thenReturn(_itemDouble2);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_itemDouble1, mock(Price.class), null);
        dsr.addDirectSale(_itemDouble2, mock(Price.class), null);

        List<Item> resultsList = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        // Assert
        assertNotNull(resultsList);
        assertTrue(resultsList.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> resultsList.add(mock(Item.class)));

    }

    @Test
    void getDirectSaleItemsByPublicationShouldReturnOnlyItemsOfGivenPublication() {

        // Arrange
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_dsDouble1, _dsDouble2);

        when(_dsDouble1.isByPublication(_publicationDouble)).thenReturn(true);
        when(_dsDouble2.isByPublication(_publicationDouble)).thenReturn(false);

        when(_dsDouble1.getItem()).thenReturn(_itemDouble1);

        // SUT
        DirectSaleRepo dsr = new DirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_itemDouble1, mock(Price.class), null);
        dsr.addDirectSale(_itemDouble2, mock(Price.class), null);

        List<Item> resultsList = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        // Assert
        assertEquals(1, resultsList.size());
        assertTrue(resultsList.contains(_itemDouble1));
        assertFalse(resultsList.contains(_itemDouble2));
        assertSame(_itemDouble1, resultsList.get(0));
    }

}