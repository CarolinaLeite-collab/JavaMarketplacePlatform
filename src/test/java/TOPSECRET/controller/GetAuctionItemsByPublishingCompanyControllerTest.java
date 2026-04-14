package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.PublishingCompanyId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuctionItemsByPublishingCompanyControllerTest {

    private UserId _buyerIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private PublishingCompanyId _publisherIdDouble;

    private Item _item1Double;
    private Item _item2Double;

    @BeforeEach
    void setUp() {
        _buyerIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _publisherIdDouble = mock(PublishingCompanyId.class);

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
        when(_iAuctionRepoDouble.getAuctionItemsByPublishingCompanyId(_publisherIdDouble)).thenReturn(List.of());

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_iAuctionRepoDouble, _buyerIdDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(_publisherIdDouble);

        // Assert
        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(_iAuctionRepoDouble).getAuctionItemsByPublishingCompanyId(_publisherIdDouble);
    }

    @Test
    void shouldReturnCorrectListForPublishingCompany() {
        // Arrange
        List<Item> expectedItems = List.of(_item1Double, _item2Double);
        when(_iAuctionRepoDouble.getAuctionItemsByPublishingCompanyId(_publisherIdDouble)).thenReturn(expectedItems);

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_iAuctionRepoDouble, _buyerIdDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(_publisherIdDouble);

        // Assert
        assertEquals(2, items.size());
        assertSame(_item1Double, items.get(0));
        assertSame(_item2Double, items.get(1));
        verify(_iAuctionRepoDouble).getAuctionItemsByPublishingCompanyId(_publisherIdDouble);
    }

    @Test
    void shouldReturnEmptyWhenPublishingCompanyDoesNotMatch() {
        // Arrange
        PublishingCompanyId otherPublisherIdDouble = mock(PublishingCompanyId.class);
        when(_iAuctionRepoDouble.getAuctionItemsByPublishingCompanyId(otherPublisherIdDouble)).thenReturn(List.of());

        //SUT
        GetAuctionItemsByPublishingCompanyController ctl = new GetAuctionItemsByPublishingCompanyController(_iAuctionRepoDouble, _buyerIdDouble);

        // Act
        List<Item> items = ctl.getAuctionItemsByPublishingCompany(otherPublisherIdDouble);

        // Assert
        assertNotNull(items);
        assertTrue(items.isEmpty());
        verify(_iAuctionRepoDouble).getAuctionItemsByPublishingCompanyId(otherPublisherIdDouble);
    }
}