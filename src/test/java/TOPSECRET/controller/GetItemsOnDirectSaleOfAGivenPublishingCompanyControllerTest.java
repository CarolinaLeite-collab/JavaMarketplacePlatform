package TOPSECRET.controller;

import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.repository.IEditionRepo;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.repository.IPublishingCompanyRepo;
import TOPSECRET.domain.valueobject.EditionId;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.PublishingCompanyId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetItemsOnDirectSaleOfAGivenPublishingCompanyControllerTest {
    private UserId _userIdDouble;
    private IDirectSaleRepo _iDirectSaleRepoDouble;
    private IItemRepo _iItemRepoDouble;
    private IEditionRepo _iEditionRepoDouble;
    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;
    private ItemId _itemIdDouble;
    private Item _itemDouble;
    private DirectSale _directSaleDouble;
    private EditionId _editionIdDouble;
    private Edition _editionDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;

    @BeforeEach
    void setUp(){

        _userIdDouble = mock(UserId.class);
        _iDirectSaleRepoDouble = mock(IDirectSaleRepo.class);
        _iItemRepoDouble = mock(IItemRepo.class);
        _iEditionRepoDouble = mock(IEditionRepo.class);
        _iPublishingCompanyRepoDouble = mock(IPublishingCompanyRepo.class);
        _itemIdDouble = mock(ItemId.class);
        _itemDouble = mock(Item.class);
        _directSaleDouble = mock(DirectSale.class);
        _editionIdDouble = mock(EditionId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _editionDouble = mock(Edition.class);

    }

    @Test
    void constructorShouldSuccessfullyGetItemsIdOnDirectSaleOfAGivenPublishingCompany(){
        //Act /SUT
        new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);
    }


    @Test
    void findAllKeysShouldReturnPublicationIdsFromRepo() {
        //Arrange
        PublishingCompanyId publishingCompanyIdDouble2 = mock(PublishingCompanyId.class);

        List<PublishingCompanyId> expected = List.of(_publishingCompanyIdDouble, publishingCompanyIdDouble2);

        when(_iPublishingCompanyRepoDouble.findAllKeys()).thenReturn(expected);

        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController controller =  new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        Iterable<PublishingCompanyId> result = controller.findAllKeys();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnItemsMatchingPublishingCompanyIds() {
        //Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.isByPublishingCompanyId(_publishingCompanyIdDouble)).thenReturn(true);

        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController controller =  new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        //Act
        List<ItemId> result = controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble);

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(_itemIdDouble));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsMatchPublishingCompanyId() {
        // Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_iEditionRepoDouble.ofIdentity(any())).thenReturn(Optional.of(_editionDouble));
        when(_editionDouble.getPublishingCompanyId()).thenReturn(_publishingCompanyIdDouble);

        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController controller =  new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        // Act
        List<ItemId> result = controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble);

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
        when(_editionDouble.isByPublishingCompanyId(_publishingCompanyIdDouble)).thenReturn(true);

        //SUT
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController controller =  new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        // Act
        List<ItemId> result = controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble);

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
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController controller =  new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble));
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
        GetItemsOnDirectSaleOfAGivenPublishingCompanyController controller =  new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_iPublishingCompanyRepoDouble, _iItemRepoDouble, _iEditionRepoDouble, _iDirectSaleRepoDouble, _userIdDouble);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble));
    }

}