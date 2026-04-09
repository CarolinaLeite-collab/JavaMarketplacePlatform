package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.directsale.DirectSaleFactory;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.DirectSaleId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class MemoDirectSaleRepoTest {

    private DirectSaleFactory _factoryDouble;
    private DirectSale _ds1Double;
    private DirectSale _ds2Double;
    private DirectSaleId _dsIdDouble;
    private List<Item> _items1;
    private List<Item> _items2;
    private Item _item1Double;
    private Item _item2Double;
    private Publication _publicationDouble;
    private Period _periodDouble;

    @BeforeEach
    void setUp() {
        _factoryDouble = mock(DirectSaleFactory.class);
        _ds1Double = mock(DirectSale.class);
        _ds2Double = mock(DirectSale.class);
        _dsIdDouble = mock(DirectSaleId.class);
        _items1 = new ArrayList<>();
        _items2 = new ArrayList<>();
        _item1Double = mock(Item.class);
        _item2Double = mock(Item.class);
        _items1.add(_item1Double);
        _items2.add(_item2Double);

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
        _items1.add(_item1Double);
        Price priceDouble = mock(Price.class);
        when(_factoryDouble.createDirectSale(_items1, priceDouble, _periodDouble)).thenReturn(_ds1Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        DirectSale created = dsr.addDirectSale(_items1, priceDouble, _periodDouble);

        // Assert
        assertSame(_ds1Double, created);
        verify(_factoryDouble, times(1)).createDirectSale(_items1, priceDouble, _periodDouble);

    }

    @Test
    void addDirectSaleShouldPropagateExceptionFromFactory() {

        // Arrange
        Price priceDouble = mock(Price.class);
        when(_factoryDouble.createDirectSale(eq(_items1), eq(priceDouble), isNull()))
                .thenThrow(new IllegalStateException("boom"));

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> dsr.addDirectSale(_items1, priceDouble, null));
    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnListOfItemsByAuthor() {

        //Arrange
        Price priceDouble = mock(Price.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_factoryDouble.createDirectSale(_items1, priceDouble, _periodDouble))
                .thenReturn(_ds1Double);
        when(_factoryDouble.createDirectSale(_items2, priceDouble, _periodDouble))
                .thenReturn(_ds2Double);

        when(_ds1Double.isByAuthor(authorIdDouble)).thenReturn(true);
        when(_ds2Double.isByAuthor(authorIdDouble)).thenReturn(true);
        when(_ds1Double.getItems()).thenReturn(_items1);
        when(_ds2Double.getItems()).thenReturn(_items2);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_items1, priceDouble, _periodDouble);
        dsr.addDirectSale(_items2, priceDouble, _periodDouble);

        List<Item> result = dsr.getDirectSaleItemsByAuthor(authorIdDouble);

        //Assert
        assertEquals(2, result.size());

    }

    @Test
    void getDirectSaleItemsByAuthorShouldReturnEmptyListOfItemsIfNotByAuthor() {

        //Arrange
        Price startingPriceDouble = mock(Price.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_factoryDouble.createDirectSale(_items1, startingPriceDouble, _periodDouble))
                .thenReturn(_ds1Double);
        when(_factoryDouble.createDirectSale(_items2, startingPriceDouble, _periodDouble))
                .thenReturn(_ds2Double);

        when(_ds1Double.isByAuthor(authorIdDouble)).thenReturn(false);
        when(_ds2Double.isByAuthor(authorIdDouble)).thenReturn(false);
        when(_ds1Double.getItems()).thenReturn(_items1);
        when(_ds2Double.getItems()).thenReturn(_items2);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //Act
        dsr.addDirectSale(_items1, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(_items2, startingPriceDouble, _periodDouble);

        List<Item> results = dsr.getDirectSaleItemsByAuthor(authorIdDouble);

        //Assert
        assertTrue(results.isEmpty());

    }

    @Test
    void getDirectSaleItemsByAuthorShouldListOfItemsOfOnlyItemsByAuthor(){

        //Arrange
        DirectSale dsDouble3 = mock(DirectSale.class);
        List<Item> items3 = new ArrayList<>();
        Item itemDouble3 = mock(Item.class);
        items3.add(itemDouble3);

        Price startingPriceDouble = mock(Price.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_factoryDouble.createDirectSale(_items1, startingPriceDouble, _periodDouble))
                .thenReturn(_ds1Double);
        when(_factoryDouble.createDirectSale(_items2, startingPriceDouble, _periodDouble))
                .thenReturn(_ds2Double);
        when(_factoryDouble.createDirectSale(items3, startingPriceDouble, _periodDouble))
                .thenReturn(dsDouble3);

        when(_ds1Double.isByAuthor(authorIdDouble)).thenReturn(true);
        when(_ds2Double.isByAuthor(authorIdDouble)).thenReturn(true);
        when(dsDouble3.isByAuthor(authorIdDouble)).thenReturn(false);
        when(_ds1Double.getItems()).thenReturn(_items1);
        when(_ds2Double.getItems()).thenReturn(_items2);
        when(dsDouble3.getItems()).thenReturn(items3);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //Act
        dsr.addDirectSale(_items1, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(_items2, startingPriceDouble, _periodDouble);
        dsr.addDirectSale(items3, startingPriceDouble, _periodDouble);

        List<Item> results = dsr.getDirectSaleItemsByAuthor(authorIdDouble);

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

        when(_ds1Double.getItems()).thenReturn(_items1);
        when(_ds2Double.getItems()).thenReturn(_items2);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act

        dsr.addDirectSale(_items1, mock(Price.class), null);
        dsr.addDirectSale(_items2, mock(Price.class), null);

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

        when(_ds1Double.isByPublishingCompany(pcDouble)).thenReturn(true);
        when(_ds2Double.isByPublishingCompany(pcDouble)).thenReturn(true);

        when(_ds1Double.getItems()).thenReturn(_items1);
        when(_ds2Double.getItems()).thenReturn(_items2);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_items1, mock(Price.class), null);
        dsr.addDirectSale(_items2, mock(Price.class), null);

        List<Item> items = dsr.getDirectSaleItemsByPublisher(pcDouble);

        // Assert
        assertEquals(2, items.size());
        assertSame(_item1Double, items.get(0));
        assertSame(_item2Double, items.get(1));
    }

    @Test
    void getDirectSaleItemsByGenreShouldReturnEmptyListWhenNoneMatch() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(_ds1Double.isByGenre(genreIdDouble)).thenReturn(false);
        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_items1, mock(Price.class), Period.ofDays(10));
        List<Item> result = dsr.getDirectSaleItemsByGenre(genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void getDirectSaleItemsByGenreShouldReturnAllItemsWhenAllMatch() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(_ds1Double.isByGenre(genreIdDouble)).thenReturn(true);
        when(_ds1Double.getItems()).thenReturn(_items1);

        when(_ds2Double.isByGenre(genreIdDouble)).thenReturn(true);
        when(_ds2Double.getItems()).thenReturn(_items2);

        when(_factoryDouble.createDirectSale(any(), any(), any())).thenReturn(_ds1Double, _ds2Double);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_items1, mock(Price.class), Period.ofDays(10));
        dsr.addDirectSale(_items2, mock(Price.class), Period.ofDays(5));

        List<Item> result = dsr.getDirectSaleItemsByGenre(genreIdDouble);

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

        when(_ds1Double.getItems()).thenReturn(_items1);
        when(_ds2Double.getItems()).thenReturn(_items2);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_items1, mock(Price.class), null);
        dsr.addDirectSale(_items2, mock(Price.class), null);

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

        when(_ds1Double.getItems()).thenReturn(_items1);
        when(_ds2Double.getItems()).thenReturn(_items2);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_items1, mock(Price.class), null);
        dsr.addDirectSale(_items2, mock(Price.class), null);

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

        when(_ds1Double.getItems()).thenReturn(_items1);

        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        // Act
        dsr.addDirectSale(_items1, mock(Price.class), null);
        dsr.addDirectSale(_items2, mock(Price.class), null);

        List<Item> resultsList = dsr.getDirectSaleItemsByPublication(_publicationDouble);

        // Assert
        assertEquals(1, resultsList.size());
        assertTrue(resultsList.contains(_item1Double));
        assertFalse(resultsList.contains(_item2Double));
        assertSame(_item1Double, resultsList.get(0));
    }

    @Test
    void shouldSaveDirectSale(){
        // SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        DirectSale ds = dsr.save(_ds1Double);

        //assert
        assertSame(ds, _ds1Double);
    }

    @Test
    void shouldReturnOptionalWhenDSIsPresent(){
        //arrange
        when(_ds1Double.identity()).thenReturn(_dsIdDouble);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        dsr.save(_ds1Double);

        Optional<DirectSale> result = dsr.ofIdentity(_dsIdDouble);

        //assert
        assertTrue(result.isPresent());
        assertSame(_ds1Double, result.get());
    }

    @Test
    void shouldReturnEmptyWhenDSNotPresent(){
        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        Optional<DirectSale> result = dsr.ofIdentity(_dsIdDouble);

        //assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenContainsIdAndFalseOtherwise(){
        //arrange
        when(_ds1Double.identity()).thenReturn(_dsIdDouble);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        dsr.save(_ds1Double);
        DirectSaleId dsId = mock(DirectSaleId.class);

        //assert
        assertTrue(dsr.containsOfIdentity(_dsIdDouble));
        assertFalse(dsr.containsOfIdentity(dsId));
    }

    @Test
    void shouldFindAll(){
        //arrange
        DirectSaleId dsIdDouble = mock(DirectSaleId.class);
        when(_ds1Double.identity()).thenReturn(_dsIdDouble);
        when(_ds2Double.identity()).thenReturn(dsIdDouble);

        //SUT
        MemoDirectSaleRepo dsr = new MemoDirectSaleRepo(_factoryDouble);

        //act
        dsr.save(_ds1Double);
        dsr.save(_ds2Double);

        //act
        Iterable<DirectSale> result = dsr.findAll();

        //assert
        List<DirectSale> list = new ArrayList<>();
        result.forEach(list::add);

        assertEquals(2, list.size());
        assertTrue(list.contains(_ds1Double));
        assertTrue(list.contains(_ds2Double));
    }

}