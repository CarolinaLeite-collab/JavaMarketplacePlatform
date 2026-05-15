package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.AuthorId;
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
class GetItemsOnAuctionOfAGivenAuthorControllerTest {

    @InjectMocks
    private GetItemsOnAuctionOfAGivenAuthorController controller;

    @Mock
    private IAuctionRepo _iAuctionRepoDouble;

    @Mock
    private IItemRepo _iItemRepoDouble;

    @Mock
    private IEditionRepo _iEditionRepoDouble;

    @Mock
    private IPublicationRepo _iPublicationRepoDouble;

    @Mock
    private IAuthorRepo _iAuthorRepoDouble;

    @Test
    void testAConstructor() {
        assertNotNull(controller);
    }

    @Test
    void findAllKeysShouldReturnAuthorIdsFromRepo() {
        //Arrange
        AuthorId authorIdDouble1 = mock(AuthorId.class);
        AuthorId authorIdDouble2 = mock(AuthorId.class);

        List<AuthorId> expected = List.of(authorIdDouble1, authorIdDouble2);

        when(_iAuthorRepoDouble.findAllKeys()).thenReturn(expected);

        //Act
        Iterable<AuthorId> result = controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnItemsMatchingAuthorIds() {
        //Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(editionDouble.getPublicationId()).thenReturn(publicationIdDouble);
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationDouble.isByAuthorId(authorIdDouble)).thenReturn(true);

        //Act
        List<ItemId> result = controller.getAuctionItemsByAuthorId(authorIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(itemIdDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchAuthorId() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        ItemId itemIdDouble = mock(ItemId.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationDouble.isByAuthorId(authorIdDouble)).thenReturn(false);

        // Act
        List<ItemId> result = controller.getAuctionItemsByAuthorId(authorIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAggregateItemsFromMultipleAuctions() {
        // Arrange
        Auction auctionDouble1 = mock(Auction.class);
        Auction auctionDouble2 = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        ItemId itemIdDouble1 = mock(ItemId.class);
        ItemId itemIdDouble2 = mock(ItemId.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble1, auctionDouble2));
        when(auctionDouble1.getItemsId()).thenReturn(List.of(itemIdDouble1));
        when(auctionDouble2.getItemsId()).thenReturn(List.of(itemIdDouble2));

        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationDouble.isByAuthorId(authorIdDouble)).thenReturn(true);

        // Act
        List<ItemId> result = controller.getAuctionItemsByAuthorId(authorIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        ItemId itemIdDouble = mock(ItemId.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByAuthorId(authorIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);

        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByAuthorId(authorIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenPublicationNotFound() {
        // Arrange
        Auction auctionDouble = mock(Auction.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        ItemId itemIdDouble = mock(ItemId.class);
        EditionId editionIdDouble = mock(EditionId.class);
        PublicationId publicationIdDouble = mock(PublicationId.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        when(_iAuctionRepoDouble.findAll()).thenReturn(List.of(auctionDouble));
        when(auctionDouble.getItemsId()).thenReturn(List.of(itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(itemIdDouble)).thenReturn(Optional.of(itemDouble));
        when(itemDouble.getEditionId()).thenReturn(editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(editionIdDouble)).thenReturn(Optional.of(editionDouble));
        when(editionDouble.getPublicationId()).thenReturn(publicationIdDouble);
        when(_iPublicationRepoDouble.ofIdentity(publicationIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getAuctionItemsByAuthorId(authorIdDouble));
    }
}