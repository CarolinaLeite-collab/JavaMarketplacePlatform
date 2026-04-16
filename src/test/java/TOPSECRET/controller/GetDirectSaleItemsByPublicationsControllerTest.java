package TOPSECRET.controller;

import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.repository.IEditionRepo;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.repository.IPublicationRepo;
import TOPSECRET.domain.valueobject.EditionId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetDirectSaleItemsByPublicationsControllerTest {

    private UserId _userIdDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private IItemRepo _iItemRepoDouble;
    private IEditionRepo  _iEditionRepoDouble;
    private IPublicationRepo _iPublicationRepoDouble;
    private ItemId _itemIdDouble;
    private Item _itemDouble;
    private DirectSale _directSaleDouble;
    private EditionId _editionIdDouble;
    private Edition _editionDouble;
    private PublicationId _publicationIdDouble;

    @BeforeEach
    void setUp(){

        _userIdDouble = mock(UserId.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _iEditionRepoDouble = mock(IEditionRepo.class);
        _iPublicationRepoDouble = mock(IPublicationRepo.class);
        _itemIdDouble = mock(ItemId.class);
        _itemDouble = mock(Item.class);
        _directSaleDouble = mock(DirectSale.class);
        _editionIdDouble = mock(EditionId.class);
        _publicationIdDouble = mock(PublicationId.class);
        _editionDouble = mock(Edition.class);

    }

    @Test
    void testDirectSaleItemsByPublicationControllerConstructor(){

        //SUT
        new GetDirectSaleItemsByPublicationsController(_iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

    }

    @Test
    void findAllKeysShouldReturnPublicationIdsFromRepo() {
        //Arrange
        PublicationId publicationIdDouble2 = mock(PublicationId.class);

        List<PublicationId> expected = List.of(_publicationIdDouble, publicationIdDouble2);

        when(_iPublicationRepoDouble.findAllKeys()).thenReturn(expected);

        //SUT
        GetDirectSaleItemsByPublicationsController controller =  new GetDirectSaleItemsByPublicationsController(_iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        Iterable<PublicationId> result = controller.findAllKeys();

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

        //SUT
        GetDirectSaleItemsByPublicationsController controller =  new GetDirectSaleItemsByPublicationsController(_iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        List<ItemId> result = controller.getDirectSaleItemsByPublication(_publicationIdDouble);

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

        //SUT
        GetDirectSaleItemsByPublicationsController controller =  new GetDirectSaleItemsByPublicationsController(_iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        // Act
        List<ItemId> result = controller.getDirectSaleItemsByPublication(_publicationIdDouble);

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

        //SUT
        GetDirectSaleItemsByPublicationsController controller =  new GetDirectSaleItemsByPublicationsController(_iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        // Act
        List<ItemId> result = controller.getDirectSaleItemsByPublication(_publicationIdDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        // Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.empty());

        //SUT
        GetDirectSaleItemsByPublicationsController controller =  new GetDirectSaleItemsByPublicationsController(_iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getDirectSaleItemsByPublication(_publicationIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {
        // Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);

        //SUT
        GetDirectSaleItemsByPublicationsController controller =  new GetDirectSaleItemsByPublicationsController(_iPublicationRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getDirectSaleItemsByPublication(_publicationIdDouble));
    }



}
