package TOPSECRET.domain;

import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.AuthorId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectSaleTest {

    private Item _itemDouble;
    private Price _priceDouble;
    private Period _period;

    @BeforeEach
    void setUp() {

        _itemDouble = mock(Item.class);
        _priceDouble = mock(Price.class);
        _period = Period.ofMonths(3);
    }

    @Test
    void constructorShouldBuildDirectSaleWithTimeLimit() {

        // Act
        DirectSale directSale = new DirectSale(_itemDouble, _priceDouble, _period); // SUT

        // Assert
        assertEquals(_itemDouble, directSale.getItem());
        assertEquals(_priceDouble, directSale.getPrice());
        assertEquals(_period, directSale.getTimeLimit());
    }

    @Test
    void constructorShouldBuildDirectSaleWithoutTimeLimit() {

        // Act
        DirectSale directSale = new DirectSale(_itemDouble, _priceDouble, null); // SUT

        // Assert
        assertEquals(_itemDouble, directSale.getItem());
        assertEquals(_priceDouble, directSale.getPrice());
        assertNull(directSale.getTimeLimit());
    }

    @Test
    void constructorShouldThrowExceptionWhenPriceIsNull() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_itemDouble, null, _period)); // SUT

        assertEquals("Price is required for a direct sale", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenItemIsNull() {
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(null, _priceDouble, _period)); // SUT

        assertEquals("Item is required for a direct sale", ex.getMessage());
    }

    @Test
    void constructorShouldThrowWhenTimeLimitIsNegative() {

        // Arrange
        Period negativeLimit = Period.ofMonths(-3);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new DirectSale(_itemDouble, _priceDouble, negativeLimit)); // SUT

        assertEquals("Time limit cannot be negative", ex.getMessage());
    }

    // Isolated IsByAuthor Test
    @Test
    void isByAuthorShouldReturnTrueWhenAuthorMatches() {

        //Arrange
        AuthorId _authorIdDouble = mock(AuthorId.class);
        when(_itemDouble.isByAuthor(_authorIdDouble)).thenReturn(true);

        // SUT
        DirectSale ds = new DirectSale(_itemDouble,  _priceDouble, _period);

        //Act
        boolean result = ds.isByAuthor(_authorIdDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void isByAuthorShouldReturnFalseWhenAuthorIsDifferent() {

        //Arrange
        AuthorId _author2IdDouble = mock(AuthorId.class);
        when(_itemDouble.isByAuthor(_author2IdDouble)).thenReturn(false);

        // SUT
        DirectSale ds = new DirectSale(_itemDouble,  _priceDouble, _period);

        //Act
        boolean result = ds.isByAuthor(_author2IdDouble);

        //Assert
        assertFalse(result);

    }

    @Test
    void isByAuthorShouldDelegateToItem() {

        //Arrange
        AuthorId _authorIdDouble = mock(AuthorId.class);

        //SUT
        DirectSale ds = new DirectSale(_itemDouble,  _priceDouble, _period);

        //Act
        ds.isByAuthor(_authorIdDouble);

        //Assert
        verify(_itemDouble, times(1)).isByAuthor(_authorIdDouble);
    }

    @Test
    void isByPublisherShouldReturnTrueWhenPublisherMatches()  {

        // Arrange
        PublishingCompany publisherDouble = mock(PublishingCompany.class);
        when(_itemDouble.isByPublishingCompany(publisherDouble)).thenReturn(true);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble, _priceDouble, _period);

        // Act
        boolean result = directSale.isByPublisher(publisherDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void isByPublisherShouldReturnFalseWhenPublisherDoesNotMatch() {

        // Arrange
        PublishingCompany publisherDouble = mock(PublishingCompany.class);
        when(_itemDouble.isByPublishingCompany(publisherDouble)).thenReturn(false);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble, _priceDouble, _period);

        // Act
        boolean result = directSale.isByPublisher(publisherDouble);

        // Assert
        assertFalse(result);

    }

    @Test
    void isByPublisherShouldDelegateToItem() {

        // Arrange
        PublishingCompany publisherDouble = mock(PublishingCompany.class);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble, _priceDouble, _period);

        // Act
        directSale.isByPublisher(publisherDouble);

        // Assert
        verify(_itemDouble).isByPublishingCompany(publisherDouble);
    }

    @Test
    void isByGenreShouldReturnTrueWhenGenreMatches() {

        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(_itemDouble.isByGenre(genreIdDouble)).thenReturn(true);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        //Act
        boolean result = directSale.isByGenre(genreIdDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void isByGenreShouldReturnFalseWhenGenreDoesNotMatch() {

        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(_itemDouble.isByGenre(genreIdDouble)).thenReturn(false);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        //Act
        boolean result = directSale.isByGenre(genreIdDouble);

        //Assert
        assertFalse(result);
    }

    @Test
    void isByGenreShouldDelegateToItem() {

        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        //SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        //Act
        directSale.isByGenre(genreIdDouble);

        //Assert
        verify(_itemDouble).isByGenre(genreIdDouble);
    }

    // isByPublication isolated tests

    @Test
    void isByPublicationShouldReturnTrueWhenPublicationMatches() {

        // Arrange
        Publication publicationDouble = mock(Publication.class);
        when(_itemDouble.isByPublication(publicationDouble)).thenReturn(true);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        // Act
        boolean result = directSale.isByPublication(publicationDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void isByPublicationShouldReturnFalseWhenPublicationIsDifferent(){

        // Arrange
        Publication publicationDouble = mock(Publication.class);
        when(_itemDouble.isByPublication(publicationDouble)).thenReturn(false);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        // Act
        boolean result = directSale.isByPublication(publicationDouble);

        // Assert
        assertFalse(result);

    }

    @Test
    void isByPublicationShouldDelegateToItem(){

        // Arrange
        Publication publicationDouble = mock(Publication.class);

        // SUT
        DirectSale directSale = new DirectSale(_itemDouble,_priceDouble, _period);

        // Act
        directSale.isByPublication(publicationDouble);

        //Assert
        verify(_itemDouble).isByPublication(publicationDouble);

    }

}