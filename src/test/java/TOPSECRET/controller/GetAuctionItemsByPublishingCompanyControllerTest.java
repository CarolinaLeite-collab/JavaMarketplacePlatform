package TOPSECRET.controller;

import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.repository.*;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuctionItemsByPublishingCompanyControllerTest {

    private UserId _userIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private IItemRepo _iItemRepoDouble;
    private IEditionRepo _iEditionRepoDouble;
    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;
    private ItemId _itemIdDouble;
    private Item _itemDouble;
    private Auction _auctionDouble;
    private EditionId _editionIdDouble;
    private Edition _editionDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;

    @BeforeEach
    void setUp(){

        _userIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _iEditionRepoDouble = mock(IEditionRepo.class);
        _iPublishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);
        _itemIdDouble = mock(ItemId.class);
        _itemDouble = mock(Item.class);
        _auctionDouble = mock(Auction.class);
        _editionIdDouble = mock(EditionId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _editionDouble = mock(Edition.class);

    }

    @Test
    void testConstructor(){
        //Act /SUT
        new GetAuctionItemsByPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iAuctionRepoDouble, _userIdDouble);
    }


    @Test
    void findAllKeysShouldReturnPublicationIdsFromRepo() {
        //Arrange
        PublishingCompanyId publishingCompanyIdDouble2 = mock(PublishingCompanyId.class);

        List<PublishingCompanyId> expected = List.of(_publishingCompanyIdDouble, publishingCompanyIdDouble2);

        when(_iPublishingCompanyRepoDouble.findAllKeys()).thenReturn(expected);

        //SUT
        GetAuctionItemsByPublishingCompanyController controller =  new GetAuctionItemsByPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        //Act
        Iterable<PublishingCompanyId> result = controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnItemsMatchingPublishingCompanyIds() {
        //Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.isByPublishingCompanyId(_publishingCompanyIdDouble)).thenReturn(true);

        //SUT
        GetAuctionItemsByPublishingCompanyController controller =  new GetAuctionItemsByPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        //Act
        List<ItemId> result = controller.getAuctionItemsByPublishingCompany(_publishingCompanyIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(_itemIdDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchPublishingCompanyId() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.getPublishingCompanyId()).thenReturn(_publishingCompanyIdDouble);

        //SUT
        GetAuctionItemsByPublishingCompanyController controller =  new GetAuctionItemsByPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act
        List<ItemId> result = controller.getAuctionItemsByPublishingCompany(_publishingCompanyIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAggregateItemsFromMultipleAuctions() {
        // Arrange
        Auction auctionDouble2 = mock(Auction.class);
        ItemId itemIdDouble2 = mock(ItemId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble, auctionDouble2));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(auctionDouble2.getItemsId()).thenReturn(List.of(itemIdDouble2));
        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.isByPublishingCompanyId(_publishingCompanyIdDouble)).thenReturn(true);

        //SUT
        GetAuctionItemsByPublishingCompanyController controller =  new GetAuctionItemsByPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act
        List<ItemId> result = controller.getAuctionItemsByPublishingCompany(_publishingCompanyIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        //SUT
        GetAuctionItemsByPublishingCompanyController controller =  new GetAuctionItemsByPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByPublishingCompany(_publishingCompanyIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);

        //SUT
        GetAuctionItemsByPublishingCompanyController controller = new GetAuctionItemsByPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iAuctionRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByPublishingCompany(_publishingCompanyIdDouble));
    }

}
