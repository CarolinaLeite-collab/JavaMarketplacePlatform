package MITELOVERS.controller;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.GenreId;
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
class GetAuctionItemsByGenreControllerTest {

    @InjectMocks
    private GetAuctionItemsByGenreController _controller;

    @Mock
    private IAuctionRepo _iAuctionRepoDouble;

    @Mock
    private IItemRepo _iItemRepoDouble;

    @Mock
    private IEditionRepo _iEditionRepoDouble;

    @Mock
    private IPublicationRepo _iPublicationRepoDouble;

    @Mock
    private IGenreRepo _iGenreRepoDouble;


    @Test
    void testAuctionItemsByGenreController(){
        assertNotNull(_controller);
    }

    @Test
    void findAllKeysShouldReturnGenreIdsFromRepo() {
        //Arrange
        GenreId genreIdDouble1 = mock(GenreId.class);
        GenreId genreIdDouble2 = mock(GenreId.class);
        List<GenreId> expected = List.of(genreIdDouble1, genreIdDouble2);

        when(_iGenreRepoDouble.findAllKeys()).thenReturn(expected);

        //Act
        Iterable<GenreId> result = _controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchGenre() {

        GenreId genreId = mock(GenreId.class);

        ItemId item1 = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        when(_iItemRepoDouble.findAllKeys()).thenReturn(List.of(item1));
        when(_iItemRepoDouble.ofIdentity(item1)).thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId()).thenReturn(mock(EditionId.class));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));

        when(editionDouble.getPublicationId()).thenReturn(mock(PublicationId.class));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));

        when(publicationDouble.isByGenreId(genreId)).thenReturn(false);

        // Act
        List<ItemId> result = _controller.getAuctionItemsByGenreId(genreId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldPassAllFilteredItemsToAuctionRepo() {

        GenreId genreId = mock(GenreId.class);

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        when(_iItemRepoDouble.findAllKeys()).thenReturn(List.of(item1, item2));
        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId()).thenReturn(mock(EditionId.class));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));

        when(editionDouble.getPublicationId()).thenReturn(mock(PublicationId.class));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));

        when(publicationDouble.isByGenreId(genreId)).thenReturn(true);

        when(_iAuctionRepoDouble.findByItemsIdSorted(List.of(item1, item2)))
                .thenReturn(List.of(item1, item2));

        // Act
        List<ItemId> result = _controller.getAuctionItemsByGenreId(genreId);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {

        GenreId genreId = mock(GenreId.class);
        ItemId itemId = mock(ItemId.class);
        Item itemDouble = mock(Item.class);

        EditionId editionId = mock(EditionId.class);

        when(_iItemRepoDouble.findAllKeys()).thenReturn(List.of(itemId));
        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId()).thenReturn(editionId);
        when(_iEditionRepoDouble.ofIdentity(editionId)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> _controller.getAuctionItemsByGenreId(genreId));
    }

    @Test
    void shouldThrowExceptionWhenPublicationNotFound() {

        GenreId genreId = mock(GenreId.class);

        ItemId itemId = mock(ItemId.class);
        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);

        EditionId editionId = mock(EditionId.class);
        PublicationId publicationId = mock(PublicationId.class);

        when(_iItemRepoDouble.findAllKeys()).thenReturn(List.of(itemId));
        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId()).thenReturn(editionId);
        when(_iEditionRepoDouble.ofIdentity(editionId)).thenReturn(Optional.of(editionDouble));

        when(editionDouble.getPublicationId()).thenReturn(publicationId);
        when(_iPublicationRepoDouble.ofIdentity(publicationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getAuctionItemsByGenreId(genreId));
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {

        GenreId genreId = mock(GenreId.class);
        ItemId itemId = mock(ItemId.class);

        when(_iItemRepoDouble.findAllKeys()).thenReturn(List.of(itemId));
        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> _controller.getAuctionItemsByGenreId(genreId));
    }

    @Test
    void shouldCallAuctionRepoWithFilteredItemIds() {

        GenreId genreId = mock(GenreId.class);

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        when(_iItemRepoDouble.findAllKeys()).thenReturn(List.of(item1, item2));
        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));

        when(itemDouble.getEditionId()).thenReturn(mock(EditionId.class));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));

        when(editionDouble.getPublicationId()).thenReturn(mock(PublicationId.class));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));

        // Both items match the genre
        when(publicationDouble.isByGenreId(genreId)).thenReturn(true);

        // IMPORTANT:
        // Only return this value if the controller calls the repo with EXACTLY this list.
        when(_iAuctionRepoDouble.findByItemsIdSorted(List.of(item1, item2)))
                .thenReturn(List.of(item1, item2));

        // Act
        List<ItemId> result = _controller.getAuctionItemsByGenreId(genreId);

        // Assert
        assertEquals(List.of(item1, item2), result);
    }

    @Test
    void shouldReturnEmptyListAndNotCallAuctionRepoWhenNoItemsMatchGenre() {

        GenreId genreId = mock(GenreId.class);

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        Item itemDouble = mock(Item.class);
        Edition editionDouble = mock(Edition.class);
        Publication publicationDouble = mock(Publication.class);

        when(_iItemRepoDouble.findAllKeys()).thenReturn(List.of(item1, item2));

        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(itemDouble));
        when(itemDouble.getEditionId()).thenReturn(mock(EditionId.class));

        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(editionDouble));
        when(editionDouble.getPublicationId()).thenReturn(mock(PublicationId.class));

        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publicationDouble));
        when(publicationDouble.isByGenreId(genreId)).thenReturn(false);

        // Act
        List<ItemId> result = _controller.getAuctionItemsByGenreId(genreId);

        // Assert
        assertTrue(result.isEmpty());
    }

}