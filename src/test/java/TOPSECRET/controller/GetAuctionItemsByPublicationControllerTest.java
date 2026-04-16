package TOPSECRET.controller;

import TOPSECRET.domain.auction.Auction;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.repository.IEditionRepo;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.repository.IPublicationRepo;
import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAuctionItemsByPublicationControllerTest {

    private UserId _buyerIdDouble;
    private IAuctionRepo _iAuctionRepoDouble;
    private IItemRepo _iItemRepoDouble;
    private IEditionRepo _iEditionRepoDouble;
    private Auction _auctionDouble;
    private Item _itemDouble;
    private Edition _editionDouble;
    private PublicationId _publicationIdDouble;
    private EditionId _editionIdDouble;
    private ItemId _itemIdDouble;
    private IPublicationRepo _iPublicationRepoDouble;

    @BeforeEach
    void setUp() {

        _buyerIdDouble = mock(UserId.class);
        _iAuctionRepoDouble = mock(IAuctionRepo.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _iEditionRepoDouble = mock(IEditionRepo.class);
        _auctionDouble = mock(Auction.class);
        _itemDouble = mock(Item.class);
        _editionDouble = mock(Edition.class);
        _publicationIdDouble = mock(PublicationId.class);
        _editionIdDouble = mock(EditionId.class);
        _itemIdDouble = mock(ItemId.class);
        _iPublicationRepoDouble = mock(IPublicationRepo.class);
    }

    @Test
    void testAuctionItemsByPublicationController(){

        // SUT
        new GetAuctionItemsByPublicationController(_iAuctionRepoDouble, _iPublicationRepoDouble, _iItemRepoDouble,
                _iEditionRepoDouble, _buyerIdDouble);
    }

    @Test
    void findAllKeysShouldReturnPublicationIdsFromRepo() {
        //Arrange
        PublicationId publicationIdDouble2 = mock(PublicationId.class);

        List<PublicationId> expected = List.of(_publicationIdDouble, publicationIdDouble2);

        when(_iPublicationRepoDouble.findAllKeys()).thenReturn(expected);

        //SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(
                _iAuctionRepoDouble, _iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _buyerIdDouble);

        //Act
        Iterable<PublicationId> result = controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnsItemsMatchingPublicationIds() {
        //Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);
        when(_editionDouble.isByPublicationId(_publicationIdDouble)).thenReturn(true);

        //SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(
                _iAuctionRepoDouble, _iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _buyerIdDouble);

        //Act
        List<ItemId> result = controller.getAuctionItemsByPublicationId(_publicationIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(_itemIdDouble));
    }

    @Test
    void shouldReturnsEmptyListWhenNoItemsMatchPublication() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.isByPublicationId(_publicationIdDouble)).thenReturn(false);

        //SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(
                _iAuctionRepoDouble, _iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _buyerIdDouble);

        // Act
        List<ItemId> result = controller.getAuctionItemsByPublicationId(_publicationIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAggregatesItemsFromMultipleAuctions() {
        // Arrange
        Auction auctionDouble2 = mock(Auction.class);
        ItemId itemIdDouble2 = mock(ItemId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble, auctionDouble2));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(auctionDouble2.getItemsId()).thenReturn(List.of(itemIdDouble2));
        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.isByPublicationId(_publicationIdDouble)).thenReturn(true);

        //SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(
                _iAuctionRepoDouble, _iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _buyerIdDouble);

        // Act
        List<ItemId> result = controller.getAuctionItemsByPublicationId(_publicationIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowsExceptionWhenItemNotFound() {
        // Arrange
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        //SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(
                _iAuctionRepoDouble, _iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _buyerIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByPublicationId(_publicationIdDouble));
    }

    @Test
    void shouldThrowsExceptionWhenEditionNotFound() {
        // Arrange
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(_auctionDouble));
        when(_auctionDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());

        //SUT
        GetAuctionItemsByPublicationController controller = new GetAuctionItemsByPublicationController(
                _iAuctionRepoDouble, _iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _buyerIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByPublicationId(_publicationIdDouble));
    }
}