package MITELOVERS.controller;

import MITELOVERS.controllers.cli.GetAuctionItemsByPublishingCompanyController;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
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
class GetAuctionItemsByPublishingCompanyControllerTest {

    @InjectMocks
    private GetAuctionItemsByPublishingCompanyController controller;

    @Mock
    private IAuctionRepo _iAuctionRepoDouble;

    @Mock
    private IItemRepo _iItemRepoDouble;

    @Mock
    private IEditionRepo _iEditionRepoDouble;

    @Mock
    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;

    @Test
    void testConstructor(){
        assertNotNull(controller);
    }

    @Test
    void findAllKeysShouldReturnPublicationIdsFromRepo() {
        //Arrange
        PublishingCompanyId id1 = mock(PublishingCompanyId.class);
        PublishingCompanyId id2 = mock(PublishingCompanyId.class);

        List<PublishingCompanyId> expected = List.of(id1, id2);

        when(_iPublishingCompanyRepoDouble.findAllKeys()).thenReturn(expected);

        //Act
        Iterable<PublishingCompanyId> result = controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnItemsMatchingPublishingCompanyIds() {
        //Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublishingCompanyId publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(editionDouble.isByPublishingCompanyId(publishingCompanyIdDouble)).thenReturn(true);

        //Act
        List<ItemId> result = controller.getAuctionItemsByPublishingCompany(publishingCompanyIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(itemIdDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchPublishingCompanyId() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        ItemId itemIdDouble = mock(ItemId.class);
        PublishingCompanyId publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(editionDouble.isByPublishingCompanyId(publishingCompanyIdDouble)).thenReturn(false);

        // Act
        List<ItemId> result = controller.getAuctionItemsByPublishingCompany(publishingCompanyIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAggregateItemsFromMultipleAuctions() {
        // Arrange
        Auction auction1 = mock(Auction.class);
        Auction auction2 = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        ItemId itemId1 = mock(ItemId.class);
        ItemId itemId2 = mock(ItemId.class);
        PublishingCompanyId publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auction1, auction2));
        when(auction1.getItemsId()).thenReturn(List.of(itemId1));
        when(auction2.getItemsId()).thenReturn(List.of(itemId2));

        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(editionDouble.isByPublishingCompanyId(publishingCompanyIdDouble)).thenReturn(true);

        // Act
        List<ItemId> result = controller.getAuctionItemsByPublishingCompany(publishingCompanyIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        ItemId itemIdDouble = mock(ItemId.class);
        PublishingCompanyId publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByPublishingCompany(publishingCompanyIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublishingCompanyId publishingCompanyIdDouble = mock(PublishingCompanyId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByPublishingCompany(publishingCompanyIdDouble));
    }
}