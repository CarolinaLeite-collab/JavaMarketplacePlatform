package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuctionItemsByPublishingCompanyControllerTest {

    private User _buyerDouble;
    private AuctionRepo _auctionRepoDouble;
    private PublishingCompany _publisherDouble;

    private Item _item1Double;
    private Item _item2Double;

    @BeforeEach
    void setUp() {
        _buyerDouble = mock(User.class);
        _auctionRepoDouble = mock(AuctionRepo.class);
        _publisherDouble = mock(PublishingCompany.class);

        _item1Double = mock(Item.class);
        _item2Double = mock(Item.class);
    }

    @Test
    void constructorWithValidDependenciesDoesNotThrow() {
        assertDoesNotThrow(() ->
                new GetAuctionItemsByPublishingCompanyController(_auctionRepoDouble, _buyerDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoAuctions() {
        // Arrange
        when(_auctionRepoDouble.getAuctionItemsByPublishingCompany(_publisherDouble)).thenReturn(List.of());

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_auctionRepoDouble, _buyerDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(_publisherDouble);

        // Assert
        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(_auctionRepoDouble).getAuctionItemsByPublishingCompany(_publisherDouble);
    }

    @Test
    void shouldReturnCorrectListForPublishingCompany() {
        // Arrange
        List<Item> expectedItems = List.of(_item1Double, _item2Double);
        when(_auctionRepoDouble.getAuctionItemsByPublishingCompany(_publisherDouble)).thenReturn(expectedItems);

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_auctionRepoDouble, _buyerDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(_publisherDouble);

        // Assert
        assertEquals(2, items.size());
        assertSame(_item1Double, items.get(0));
        assertSame(_item2Double, items.get(1));
        verify(_auctionRepoDouble).getAuctionItemsByPublishingCompany(_publisherDouble);
    }

    @Test
    void shouldReturnEmptyWhenPublishingCompanyDoesNotMatch() {
        // Arrange
        PublishingCompany otherPublisherDouble = mock(PublishingCompany.class);
        when(_auctionRepoDouble.getAuctionItemsByPublishingCompany(otherPublisherDouble)).thenReturn(List.of());

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_auctionRepoDouble, _buyerDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(otherPublisherDouble);

        // Assert
        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(_auctionRepoDouble).getAuctionItemsByPublishingCompany(otherPublisherDouble);
    }
}