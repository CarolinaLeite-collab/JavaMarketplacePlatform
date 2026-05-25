package MITELOVERS.controller;

import MITELOVERS.controllers.cli.GetAuctionItemsByPublicationController;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublicationId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class GetAuctionItemsByPublicationControllerTest {

    @InjectMocks
    private GetAuctionItemsByPublicationController controller;

    @Mock
    private IAuctionRepo _iAuctionRepoDouble;

    @Mock
    private IItemRepo _iItemRepoDouble;

    @Mock
    private IEditionRepo _iEditionRepoDouble;

    @Mock
    private IPublicationRepo _iPublicationRepoDouble;


    @Test
    void testAuctionItemsByPublicationController() {
        assertNotNull(controller);
    }

    @Test
    void findAllKeysShouldReturnPublicationIdsFromRepo() {
        //Arrange
        PublicationId publicationIdDouble1 = mock(PublicationId.class);
        PublicationId publicationIdDouble2 = mock(PublicationId.class);
        List<PublicationId> expected = List.of(publicationIdDouble1, publicationIdDouble2);

        when(_iPublicationRepoDouble.findAllKeys()).thenReturn(expected);

        //Act
        Iterable<PublicationId> result = controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnsItemsMatchingPublicationIds() {
        //Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublicationId publicationIdDouble1 = mock(PublicationId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(editionDouble.isByPublicationId(publicationIdDouble1)).thenReturn(true);

        //Act
        List<ItemId> result = controller.getAuctionItemsByPublicationId(publicationIdDouble1);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(itemIdDouble));
    }

    @Test
    void shouldReturnsEmptyListWhenNoItemsMatchPublication() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        ItemId itemIdDouble = mock(ItemId.class);
        PublicationId publicationIdDouble1 = mock(PublicationId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(editionDouble.isByPublicationId(publicationIdDouble1)).thenReturn(false);

        // Act
        List<ItemId> result = controller.getAuctionItemsByPublicationId(publicationIdDouble1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAggregatesItemsFromMultipleAuctions() {
        // Arrange
        Auction auctionDouble1 = mock(Auction.class);
        Auction auctionDouble2 = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        ItemId itemIdDouble1 = mock(ItemId.class);
        ItemId itemIdDouble2 = mock(ItemId.class);
        PublicationId publicationIdDouble1 = mock(PublicationId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble1, auctionDouble2));
        when(auctionDouble1.getItemsId()).thenReturn(List.of(itemIdDouble1));
        when(auctionDouble2.getItemsId()).thenReturn(List.of(itemIdDouble2));

        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(editionDouble.isByPublicationId(publicationIdDouble1)).thenReturn(true);

        // Act
        List<ItemId> result = controller.getAuctionItemsByPublicationId(publicationIdDouble1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowsExceptionWhenItemNotFound() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        ItemId itemIdDouble = mock(ItemId.class);
        PublicationId publicationIdDouble1 = mock(PublicationId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByPublicationId(publicationIdDouble1));
    }

    @Test
    void shouldThrowsExceptionWhenEditionNotFound() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);

        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublicationId publicationIdDouble1 = mock(PublicationId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByPublicationId(publicationIdDouble1));
    }
}