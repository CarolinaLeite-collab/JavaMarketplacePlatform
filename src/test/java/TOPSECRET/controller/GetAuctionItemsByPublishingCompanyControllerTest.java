package TOPSECRET.controller;

import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuctionItemsByPublishingCompanyControllerTest {

    private UserId _buyerIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private PublishingCompany _publisherDouble;

    private Item _item1Double;
    private Item _item2Double;

    @BeforeEach
    void setUp() {
        _buyerIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _publisherDouble = mock(PublishingCompany.class);

        _item1Double = mock(Item.class);
        _item2Double = mock(Item.class);
    }

    @Test
    void constructorWithValidDependenciesDoesNotThrow() {
        assertDoesNotThrow(() ->
                new GetAuctionItemsByPublishingCompanyController(_iAuctionRepoDouble, _buyerIdDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoAuctions() {
        // Arrange
        when(_iAuctionRepoDouble.getAuctionItemsByPublishingCompany(_publisherDouble)).thenReturn(List.of());

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_iAuctionRepoDouble, _buyerIdDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(_publisherDouble);

        // Assert
        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(_iAuctionRepoDouble).getAuctionItemsByPublishingCompany(_publisherDouble);
    }

    @Test
    void shouldReturnCorrectListForPublishingCompany() {
        // Arrange
        List<Item> expectedItems = List.of(_item1Double, _item2Double);
        when(_iAuctionRepoDouble.getAuctionItemsByPublishingCompany(_publisherDouble)).thenReturn(expectedItems);

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_iAuctionRepoDouble, _buyerIdDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(_publisherDouble);

        // Assert
        assertEquals(2, items.size());
        assertSame(_item1Double, items.get(0));
        assertSame(_item2Double, items.get(1));
        verify(_iAuctionRepoDouble).getAuctionItemsByPublishingCompany(_publisherDouble);
    }

    @Test
    void shouldReturnEmptyWhenPublishingCompanyDoesNotMatch() {
        // Arrange
        PublishingCompany otherPublisherDouble = mock(PublishingCompany.class);
        when(_iAuctionRepoDouble.getAuctionItemsByPublishingCompany(otherPublisherDouble)).thenReturn(List.of());

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_iAuctionRepoDouble, _buyerIdDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(otherPublisherDouble);

        // Assert
        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(_iAuctionRepoDouble).getAuctionItemsByPublishingCompany(otherPublisherDouble);
    }
}