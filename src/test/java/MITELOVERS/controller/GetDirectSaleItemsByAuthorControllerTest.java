package MITELOVERS.controller;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(GetDirectSaleItemsByAuthorController.class)
@ActiveProfiles("jpa")
class GetDirectSaleItemsByAuthorControllerTest {

    //SUT
    @Autowired
    private GetDirectSaleItemsByAuthorController _controller;

    @MockBean
    private IDirectSaleRepo _iDirectSaleRepoDouble;

    @MockBean
    private IItemRepo _iItemRepoDouble;

    @MockBean
    private IEditionRepo  _iEditionRepoDouble;

    @MockBean
    private IPublicationRepo _iPublicationRepoDouble;

    @MockBean
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



}
