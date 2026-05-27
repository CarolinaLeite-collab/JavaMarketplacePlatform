package MITELOVERS.controllers.cli;

import MITELOVERS.controllers.cli.GetDirectSaleItemsByAuthorController;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublicationId;
import org.junit.jupiter.api.BeforeEach;
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
class GetDirectSaleItemsByAuthorControllerTest {

    //SUT
    @InjectMocks
    private GetDirectSaleItemsByAuthorController _controller;

    @Mock
    private IDirectSaleRepo _iDirectSaleRepoDouble;

    @Mock
    private IItemRepo _iItemRepoDouble;

    @Mock
    private IEditionRepo  _iEditionRepoDouble;

    @Mock
    private IPublicationRepo _iPublicationRepoDouble;

    @Mock
    private IAuthorRepo _iAuthorRepoDouble;

    private AuthorId _authorIdDouble;
    private ItemId _itemIdDouble;
    private Item _itemDouble;
    private DirectSale _directSaleDouble;
    private EditionId _editionIdDouble;
    private Edition _editionDouble;
    private PublicationId _publicationIdDouble;
    private Publication _publicationDouble;


    @BeforeEach
    void setUp() {

        _authorIdDouble = mock(AuthorId.class);
        _itemIdDouble = mock(ItemId.class);
        _itemDouble = mock(Item.class);
        _directSaleDouble = mock(DirectSale.class);
        _editionIdDouble = mock(EditionId.class);
        _publicationIdDouble = mock(PublicationId.class);
        _editionDouble = mock(Edition.class);
        _publicationDouble = mock(Publication.class);

    }

    @Test
    void testAConstructor(){

        //Assert
        assertNotNull(_controller);

    }

    @Test
    void findAllKeysShouldReturnAuthorIdsFromRepo() {
        //Arrange
        AuthorId authorIdDouble2 = mock(AuthorId.class);

        List<AuthorId> expected = List.of(_authorIdDouble, authorIdDouble2);

        when(_iAuthorRepoDouble.findAllKeys()).thenReturn(expected);

        //Act
        Iterable<AuthorId> result = _controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnItemsMatchingAuthorIds() {
        //Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
        when(_publicationDouble.isByAuthorId(_authorIdDouble)).thenReturn(true);

        //Act
        List<ItemId> result = _controller.getDirectSaleItemsByAuthorId(_authorIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(_itemIdDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchAuthorId() {
        //Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
        when(_publicationDouble.isByAuthorId(_authorIdDouble)).thenReturn(false);

        //Act
        List<ItemId> result = _controller.getDirectSaleItemsByAuthorId(_authorIdDouble);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAggregateItemsFromMultipleDirectSales() {
        //Arrange
        DirectSale auctionDouble2 = mock(DirectSale.class);
        ItemId itemIdDouble2 = mock(ItemId.class);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble, auctionDouble2));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(auctionDouble2.getItemsId()).thenReturn(List.of(itemIdDouble2));
        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_iPublicationRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_publicationDouble));
        when(_publicationDouble.isByAuthorId(_authorIdDouble)).thenReturn(true);

        //Act
        List<ItemId> result = _controller.getDirectSaleItemsByAuthorId(_authorIdDouble);

        //Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        //Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByAuthorId(_authorIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {
        //Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);

        //Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByAuthorId(_authorIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenPublicationNotFound() {
        // Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iPublicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.empty());
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByAuthorId(_authorIdDouble));
    }

    @Test
    void shouldReturnDirectSaleItemsByAuthorIdSortedByDescription() {

        //Arrange
        ItemId itemId2 = mock(ItemId.class);
        Item item2 = mock(Item.class);
        EditionId editionId2 = mock(EditionId.class);
        Edition edition2 = mock(Edition.class);
        PublicationId publicationId2 = mock(PublicationId.class);
        Publication publication2 = mock(Publication.class);

        List<Item> expectedItems = List.of(_itemDouble, item2);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));

        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble, itemId2));

        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_iItemRepoDouble.ofIdentity(itemId2)).thenReturn(Optional.of(item2));

        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(item2.getEditionId()).thenReturn(editionId2);

        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.of(_editionDouble));
        when(_iEditionRepoDouble.ofIdentity(editionId2)).thenReturn(Optional.of(edition2));

        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);
        when(edition2.getPublicationId()).thenReturn(publicationId2);

        when(_iPublicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(_publicationDouble));
        when(_iPublicationRepoDouble.ofIdentity(publicationId2)).thenReturn(Optional.of(publication2));

        when(_publicationDouble.isByAuthorId(_authorIdDouble)).thenReturn(true);
        when(publication2.isByAuthorId(_authorIdDouble)).thenReturn(true);

        when(_itemIdDouble.getValue()).thenReturn("1");
        when(itemId2.getValue()).thenReturn("2");

        when(_iItemRepoDouble.findByIdInOrderByDescriptionAsc(List.of("1", "2")))
                .thenReturn(expectedItems);

        //Act
        List<Item> result =
                _controller.getDirectSaleItemsByAuthorIdSortedByDescription(_authorIdDouble);

        //Assert
        assertEquals(expectedItems, result);
    }

    @Test
    void shouldIgnoreItemsWhenPublicationIsNotByAuthorId() {

        //Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));

        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));

        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));

        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);

        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.of(_editionDouble));

        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);

        when(_iPublicationRepoDouble.ofIdentity(_publicationIdDouble)).thenReturn(Optional.of(_publicationDouble));

        when(_publicationDouble.isByAuthorId(_authorIdDouble)).thenReturn(false);

        when(_iItemRepoDouble.findByIdInOrderByDescriptionAsc(List.of()))
                .thenReturn(List.of());

        //Act
        List<Item> result =
                _controller.getDirectSaleItemsByAuthorIdSortedByDescription(_authorIdDouble);

        //Assert
        assertTrue(result.isEmpty());

    }

}
