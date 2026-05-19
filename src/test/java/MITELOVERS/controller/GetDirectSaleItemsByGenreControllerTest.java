package MITELOVERS.controller;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class GetDirectSaleItemsByGenreControllerTest {

    @Mock
    private IDirectSaleRepo _iDirectSaleRepoDouble;

    @Mock
    private IItemRepo _iItemRepoDouble;

    @Mock
    private IEditionRepo _iEditionRepoDouble;

    @Mock
    private IPublicationRepo _iPublicationRepoDouble;

    // SUT
    @InjectMocks
    private GetDirectSaleItemsByGenreController _controller;


    @Test
    void controllerShouldInstantiate() {
        // SUT
        new GetDirectSaleItemsByGenreController(
                _iDirectSaleRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iPublicationRepoDouble
        );
    }

    @Test
    void shouldReturnDirectSaleItemsOfGivenGenreAsc() {

        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);
        GenreId genreId = mock(GenreId.class);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getEditionId()).thenReturn(mock(EditionId.class));

        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock(PublicationId.class));

        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(publication.isByGenreId(genreId)).thenReturn(true);

        // IMPORTANT: repo returns sorted list only if called with correct args
        when(_iDirectSaleRepoDouble.findByItemsIdSortedByPublicationDateAsc(List.of(itemId)))
                .thenReturn(List.of(itemId));

        List<ItemId> result = _controller.getDirectSaleItemsByGenreAsc(genreId);

        assertEquals(List.of(itemId), result);
    }

    @Test
    void shouldReturnDirectSaleItemsOfGivenGenreDesc() {

        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);
        GenreId genreId = mock(GenreId.class);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getEditionId()).thenReturn(mock(EditionId.class));

        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock(PublicationId.class));

        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(publication.isByGenreId(genreId)).thenReturn(true);

        when(_iDirectSaleRepoDouble.findByItemsIdSortedByPublicationDateDesc(List.of(itemId)))
                .thenReturn(List.of(itemId));

        List<ItemId> result = _controller.getDirectSaleItemsByGenreDesc(genreId);

        assertEquals(List.of(itemId), result);
    }

    @Test
    void shouldThrowWhenItemNotFound() {

        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = mock(ItemId.class);
        GenreId genreId = mock(GenreId.class);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByGenreAsc(genreId));
    }

    @Test
    void shouldThrowWhenEditionNotFound() {

        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);
        GenreId genreId = mock(GenreId.class);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getEditionId()).thenReturn(mock(EditionId.class));

        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByGenreAsc(genreId));
    }

    @Test
    void shouldThrowWhenPublicationNotFound() {

        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        GenreId genreId = mock(GenreId.class);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getEditionId()).thenReturn(mock(EditionId.class));

        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock(PublicationId.class));

        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByGenreAsc(genreId));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchGenre() {

        DirectSale ds = mock(DirectSale.class);
        ItemId itemId = mock(ItemId.class);
        Item item = mock(Item.class);
        Edition edition = mock(Edition.class);
        Publication publication = mock(Publication.class);
        GenreId genreId = mock(GenreId.class);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(ds));
        when(ds.getItemsId()).thenReturn(List.of(itemId));

        when(_iItemRepoDouble.ofIdentity(itemId)).thenReturn(Optional.of(item));
        when(item.getEditionId()).thenReturn(mock(EditionId.class));

        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(edition));
        when(edition.getPublicationId()).thenReturn(mock(PublicationId.class));

        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(publication));
        when(publication.isByGenreId(genreId)).thenReturn(false);

        List<ItemId> result = _controller.getDirectSaleItemsByGenreAsc(genreId);

        assertTrue(result.isEmpty());
    }

}
