package MITELOVERS.controller;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublicationId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
public class GetDirectSaleItemsByPublicationsControllerTest {

    //SUT
    @InjectMocks
    private GetDirectSaleItemsByPublicationsController _controller;

    @Mock
    private IDirectSaleRepo _iDirectSaleRepoDouble;

    @Mock
    private IItemRepo _iItemRepoDouble;

    @Mock
    private IEditionRepo  _iEditionRepoDouble;

    @Mock
    private IPublicationRepo _iPublicationRepoDouble;

    private ItemId _itemIdDouble;
    private Item _itemDouble;
    private DirectSale _directSaleDouble;
    private EditionId _editionIdDouble;
    private Edition _editionDouble;
    private PublicationId _publicationIdDouble;

    @BeforeEach
    void setUp(){

        _itemIdDouble = mock(ItemId.class);
        _itemDouble = mock(Item.class);
        _directSaleDouble = mock(DirectSale.class);
        _editionIdDouble = mock(EditionId.class);
        _publicationIdDouble = mock(PublicationId.class);
        _editionDouble = mock(Edition.class);

    }

    @Test
    void testDirectSaleItemsByPublicationControllerConstructor(){

        assertNotNull(_controller);
    }

    @Test
    void findAllKeysShouldReturnPublicationIdsFromRepo() {
        //Arrange
        PublicationId publicationIdDouble2 = mock(PublicationId.class);

        List<PublicationId> expected = List.of(_publicationIdDouble, publicationIdDouble2);

        when(_iPublicationRepoDouble.findAllKeys()).thenReturn(expected);

        //Act
        Iterable<PublicationId> result = _controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnItemsMatchingPublicationIds() {
        //Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.isByPublicationId(_publicationIdDouble)).thenReturn(true);

        //Act
        List<ItemId> result = _controller.getDirectSaleItemsByPublication(_publicationIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(_itemIdDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchPublicationId() {
        // Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.getPublicationId()).thenReturn(_publicationIdDouble);

        // Act
        List<ItemId> result = _controller.getDirectSaleItemsByPublication(_publicationIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAggregateItemsFromMultipleDirectSales() {
        // Arrange
        DirectSale auctionDouble2 = mock(DirectSale.class);
        ItemId itemIdDouble2 = mock(ItemId.class);

        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble, auctionDouble2));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(auctionDouble2.getItemsId()).thenReturn(List.of(itemIdDouble2));
        when(_iItemRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.isByPublicationId(_publicationIdDouble)).thenReturn(true);

        // Act
        List<ItemId> result = _controller.getDirectSaleItemsByPublication(_publicationIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        // Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByPublication(_publicationIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {
        // Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByPublication(_publicationIdDouble));
    }



}
