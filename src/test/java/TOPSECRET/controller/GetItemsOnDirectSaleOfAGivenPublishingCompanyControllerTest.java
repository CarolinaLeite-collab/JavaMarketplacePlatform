package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetItemsOnDirectSaleOfAGivenPublishingCompanyControllerTest {
    private Item _itemDouble;
    private PublishingCompany _publisherCompanyDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private UserId _buyerIdDouble;

    @BeforeEach
    void setUp() {
        _publisherCompanyDouble = mock(PublishingCompany.class);
        _itemDouble = mock(Item.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _buyerIdDouble = mock(UserId.class);
    }

    @Test
    void constructorShouldSuccessfullyGetItemsOnDirectSaleOfAGivenPublishingCompany(){
        //Act /SUT
        new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iDirectSaleRepoDouble, _buyerIdDouble);
    }

    @Test
    void getDirectSaleItemByPublisherThrowsIllegalArgumentExceptionWhenPublisherIsNull() {
        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController ctr = new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iDirectSaleRepoDouble, _buyerIdDouble);
        //Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ctr.getDirectSaleItemByPublisher(null));
    }

    @Test
    void getDirectSaleItemByPublisherDelegatesToRepository() {
        //Arrange
        List<Item> expected = List.of(_itemDouble);
        when(_iDirectSaleRepoDouble.getDirectSaleItemsByPublisher(_publisherCompanyDouble)).thenReturn(expected);
        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController ctr = new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iDirectSaleRepoDouble, _buyerIdDouble);
        //Act
        List<Item> actual = ctr.getDirectSaleItemByPublisher(_publisherCompanyDouble);
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void getDirectSaleItemByPublisherReturnsEmptyListWhenRepositoryReturnsEmpty() {
        //Arrange
        List<Item> expected = List.of();
        when(_iDirectSaleRepoDouble.getDirectSaleItemsByPublisher(_publisherCompanyDouble)).thenReturn(expected);
        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController ctr = new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iDirectSaleRepoDouble, _buyerIdDouble);
        //Act
        List<Item> actual = ctr.getDirectSaleItemByPublisher(_publisherCompanyDouble);
        //Assert
        assertEquals(expected, actual);
    }
}