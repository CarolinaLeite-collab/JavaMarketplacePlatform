package TOPSECRET.domain;

import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoDirectSaleRepoTest {

    private DirectSaleFactory _factoryDouble;
    private DirectSale _ds1Double;
    private DirectSale _ds2Double;
    private Item _item1Double;
    private Item _item2Double;
    private Publication _publicationDouble;
    private Period _periodDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(DirectSaleFactory.class);
        _ds1Double = mock(DirectSale.class);
        _ds2Double = mock(DirectSale.class);
        _item1Double = mock(Item.class);
        _item2Double = mock(Item.class);
        _publicationDouble = mock(Publication.class);
        _periodDouble = mock(Period.class);

    }

    @Test
    void constructorShouldBuildDirectSaleRepo() {

        // Act & SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Assert
        assertNotNull(dsr);
    }

    @Test
    void addDirectSaleShouldReturnDirectSaleFromFactory() {

        // Arrange
        Price priceDouble = mock(Price.class);
        when(_factoryDouble.createDirectSale(_item1Double, priceDouble, _periodDouble)).thenReturn(_ds1Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        DirectSale created = dsr.addDirectSale(_item1Double, priceDouble, _periodDouble);

        // Assert
        assertSame(_ds1Double, created);
        verify(_factoryDouble, times(1)).createDirectSale(_item1Double, priceDouble, _periodDouble);

    }

   @Test
    void addDirectSaleShouldPropagateExceptionFromFactory() {

        // Arrange
        Price priceDouble = mock(Price.class);
        when(_factoryDouble.createDirectSale(eq(_item1Double), eq(priceDouble), isNull()))
                .thenThrow(new IllegalStateException("boom"));

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> dsr.addDirectSale(_item1Double, priceDouble, null));
    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnListOfItemsByAuthor() {

        //Arrange
        Price priceDouble = mock(Price.class);
        Author authorDouble = mock(Author.class);
        when(_factoryDouble.createDirectSale(_item1Double, priceDouble, _periodDouble))
                .thenReturn(_ds1Double);
        when(_factoryDouble.createDirectSale(_item2Double, priceDouble, _periodDouble))
                .thenReturn(_ds2Double);

        when(_ds1Double.isByAuthor(authorDouble)).thenReturn(true);
        when(_ds2Double.isByAuthor(authorDouble)).thenReturn(true);
        when(_ds1Double.getItem()).thenReturn(_item1Double);
        when(_ds2Double.getItem()).thenReturn(_item2Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_item1Double, priceDouble, _periodDouble);
        dsr.addDirectSale(_item2Double, priceDouble, _periodDouble);

        List<Item> result = dsr.getDirectSaleItemsByAuthor(authorDouble);

        //Assert
        assertEquals(2, result.size());

    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnEmptyListOfItemsIfNotByAuthor() {

        //Arrange
        Price startingPriceDouble = mock(Price.class);
        Author authorDouble = mock(Author.class);

        when(_factoryDouble.createDirectSale(_item1Double, startingPriceDouble, _periodDouble))
                .thenReturn(_ds1Double);
        when(_factoryDouble.createDirectSale(_item2Double, startingPriceDouble, _periodDouble))
                .thenReturn(_ds2Double);

        when(_ds1Double.isByAuthor(authorDouble)).thenReturn(false);
        when(_ds2Double.isByAuthor(authorDouble)).thenReturn(false);
        when(_ds1Double.getItem()).thenReturn(_item1Double);
        when(_ds2Double.getItem()).thenReturn(_item2Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //Act
        dsr.addDirectSale(_item1Double, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(_item2Double, startingPriceDouble, _periodDouble);

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

        when(_factoryDouble.createDirectSale(_item1Double, startingPriceDouble, _periodDouble))
                .thenReturn(_ds1Double);
        when(_factoryDouble.createDirectSale(_item2Double, startingPriceDouble, _periodDouble))
                .thenReturn(_ds2Double);
        when(_factoryDouble.createDirectSale(itemDouble3, startingPriceDouble, _periodDouble))
                .thenReturn(dsDouble3);

        when(_ds1Double.isByAuthor(authorDouble)).thenReturn(true);
        when(_ds2Double.isByAuthor(authorDouble)).thenReturn(true);
        when(dsDouble3.isByAuthor(authorDouble)).thenReturn(false);
        when(_ds1Double.getItem()).thenReturn(_item1Double);
        when(_ds2Double.getItem()).thenReturn(_item2Double);
        when(dsDouble3.getItem()).thenReturn(itemDouble3);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //Act
        dsr.addDirectSale(_item1Double, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(_item2Double, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(itemDouble3, startingPriceDouble, _periodDouble);

        List<Item> results = dsr.getDirectSaleItemsByAuthor(authorDouble);

        //Assert
        assertTrue(results.contains(_item1Double));
        assertTrue(results.contains(_item2Double));
        assertFalse(results.contains(itemDouble3));
        assertEquals(2, results.size());

    }

    @Test
    void getDirectSaleItemsByPublicationFiltersCorrectly(){

        // Arrange
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double, _ds2Double);

        when(_ds1Double.isByPublication(_publicationDouble)).thenReturn(true);
        when(_ds2Double.isByPublication(_publicationDouble)).thenReturn(false);

        when(_ds1Double.getItem()).thenReturn(_item1Double);
        when(_ds2Double.getItem()).thenReturn(_item2Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act

        dsr.addDirectSale(mock(Item.class), mock(Price.class), null);
        dsr.addDirectSale(mock(Item.class), mock(Price.class), null);

        List<Item> items = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        // Assert
        assertEquals(1, items.size());
        assertSame(_item1Double, items.get(0));
    }

    @Test
    void getDirectSaleItemsByPublisherFiltersCorrectly(){

        // Arrange
        PublishingCompany pcDouble = mock(PublishingCompany.class);
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double, _ds2Double);

        when(_ds1Double.isByPublisher(pcDouble)).thenReturn(true);
        when(_ds2Double.isByPublisher(pcDouble)).thenReturn(true);

        when(_ds1Double.getItem()).thenReturn(_item1Double);
        when(_ds2Double.getItem()).thenReturn(_item2Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(mock(Item.class), mock(Price.class), null);
        dsr.addDirectSale(mock(Item.class), mock(Price.class), null);

        List<Item> items = dsr.getDirectSaleItemsByPublisher(pcDouble);

        // Assert
        assertEquals(2, items.size());
        assertSame(_item1Double, items.get(0));
        assertSame(_item2Double, items.get(1));
    }

    @Test
    void getDirectSaleItemsByGenreShouldReturnEmptyListWhenNoneMatch() {

        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(_ds1Double.isByGenre(genreDouble)).thenReturn(false);
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

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
        when(_ds1Double.isByGenre(genreDouble)).thenReturn(true);
        when(_ds1Double.getItem()).thenReturn(_item1Double);

        when(_ds2Double.isByGenre(genreDouble)).thenReturn(true);
        when(_ds2Double.getItem()).thenReturn(_item2Double);

        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double, _ds2Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(mock(Item.class), mock(Price.class), Period.ofDays(10));
        dsr.addDirectSale(mock(Item.class), mock(Price.class), Period.ofDays(5));

        List<Item> result = dsr.getDirectSaleItemsByGenre(genreDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(_item1Double));
        assertTrue(result.contains(_item2Double));

    }

    // Isolated tests for US023

    @Test
    void getDirectSaleItemsByPublicationShouldReturnItemsOfThatPublication() {

        // Arrange
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double, _ds2Double);

        when(_ds1Double.isByPublication(_publicationDouble)).thenReturn(true);
        when(_ds2Double.isByPublication(_publicationDouble)).thenReturn(true);

        when(_ds1Double.getItem()).thenReturn(_item1Double);
        when(_ds2Double.getItem()).thenReturn(_item2Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_item1Double, mock(Price.class), null);
        dsr.addDirectSale(_item2Double, mock(Price.class), null);

        List<Item> resultsList = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        //Assert
        assertEquals(2, resultsList.size());
        assertTrue(resultsList.containsAll(List.of(_item1Double, _item2Double)));

    }

    @Test
    void getDirectSaleItemsByPublicationShouldReturnEmptyAndUnmodifiableListWhenNoDirectSalesMatch() {

        // Arrange
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double, _ds2Double);

        when(_ds1Double.isByPublication(_publicationDouble)).thenReturn(false);
        when(_ds2Double.isByPublication(_publicationDouble)).thenReturn(false);

        when(_ds1Double.getItem()).thenReturn(_item1Double);
        when(_ds2Double.getItem()).thenReturn(_item2Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_item1Double, mock(Price.class), null);
        dsr.addDirectSale(_item2Double, mock(Price.class), null);

        List<Item> resultsList = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        // Assert
        assertNotNull(resultsList);
        assertTrue(resultsList.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> resultsList.add(mock(Item.class)));

    }

    @Test
    void getDirectSaleItemsByPublicationShouldReturnOnlyItemsOfGivenPublication() {

        // Arrange
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double, _ds2Double);

        when(_ds1Double.isByPublication(_publicationDouble)).thenReturn(true);
        when(_ds2Double.isByPublication(_publicationDouble)).thenReturn(false);

        when(_ds1Double.getItem()).thenReturn(_item1Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_item1Double, mock(Price.class), null);
        dsr.addDirectSale(_item2Double, mock(Price.class), null);

        List<Item> resultsList = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        // Assert
        assertEquals(1, resultsList.size());
        assertTrue(resultsList.contains(_item1Double));
        assertFalse(resultsList.contains(_item2Double));
        assertSame(_item1Double, resultsList.get(0));
    }

}