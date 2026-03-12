package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetItemsOnDirectSaleOfAGivenPublishingCompanyControllerTest {
    private Item _itemDouble;
    private PublishingCompany _publisherCompanyDouble;
    private DirectSaleRepo _directSaleRepoDouble;

    @BeforeEach
    void setUp() {
        _publisherCompanyDouble = mock(PublishingCompany.class);
        _itemDouble = mock(Item.class);
        _directSaleRepoDouble = mock(DirectSaleRepo.class);
    }

    @Test
    void constructorShouldSuccessfullyGetItemsOnDirectSaleOfAGivenPublishingCompany(){
        //Act /SUT
        new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_directSaleRepoDouble);
    }
    @Test
    void constructorThrowsNullPointerExceptionWhenDirectSaleRepoIsNull() {
        //Act & Assert
        assertThrows(NullPointerException.class, () -> new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(null));
    }

    @Test
    void getDirectSaleItemByPublisherThrowsIllegalArgumentExceptionWhenPublisherIsNull() {
        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController ctr = new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_directSaleRepoDouble);
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ctr.getDirectSaleItemByPublisher(null));
    }

    @Test
    void getDirectSaleItemByPublisherDelegatesToRepository() {
        //Arrange
        List<Item> expected = List.of(_itemDouble);
        when(_directSaleRepoDouble.getDirectSaleItemsByPublisher(_publisherCompanyDouble)).thenReturn(expected);
        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController ctr = new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_directSaleRepoDouble);
        //Act
        List<Item> actual = ctr.getDirectSaleItemByPublisher(_publisherCompanyDouble);
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void getDirectSaleItemByPublisherReturnsEmptyListWhenRepositoryReturnsEmpty() {
        //Arrange
        List<Item> expected = List.of();
        when(_directSaleRepoDouble.getDirectSaleItemsByPublisher(_publisherCompanyDouble)).thenReturn(expected);
        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController ctr = new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_directSaleRepoDouble);
        //Act
        List<Item> actual = ctr.getDirectSaleItemByPublisher(_publisherCompanyDouble);
        //Assert
        assertEquals(expected, actual);
    }
}