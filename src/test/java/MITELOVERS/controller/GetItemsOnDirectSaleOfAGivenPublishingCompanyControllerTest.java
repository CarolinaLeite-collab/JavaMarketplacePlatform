package MITELOVERS.controller;

import MITELOVERS.controllers.cli.GetItemsOnDirectSaleOfAGivenPublishingCompanyController;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class GetItemsOnDirectSaleOfAGivenPublishingCompanyControllerTest {

    //SUT
    @InjectMocks
    private GetItemsOnDirectSaleOfAGivenPublishingCompanyController _controller;

    @Mock
    private IDirectSaleRepo _iDirectSaleRepoDouble;

    @Mock
    private IItemRepo _iItemRepoDouble;

    @Mock
    private IEditionRepo _iEditionRepoDouble;

    @Mock
    private IPublishingCompanyRepo _iPublishingCompanyRepoDouble;

    private ItemId _itemIdDouble;
    private Item _itemDouble;
    private DirectSale _directSaleDouble;
    private EditionId _editionIdDouble;
    private Edition _editionDouble;
    private PublishingCompanyId _publishingCompanyIdDouble;

    @BeforeEach
    void setUp(){

        _itemIdDouble = mock(ItemId.class);
        _itemDouble = mock(Item.class);
        _directSaleDouble = mock(DirectSale.class);
        _editionIdDouble = mock(EditionId.class);
        _publishingCompanyIdDouble = mock(PublishingCompanyId.class);
        _editionDouble = mock(Edition.class);

    }

    @Test
    void constructorShouldSuccessfullyGetItemsIdOnDirectSaleOfAGivenPublishingCompany(){
        assertNotNull(_controller);
    }


    @Test
    void findAllKeysShouldReturnPublicationIdsFromRepo() {
        //Arrange
        PublishingCompanyId publishingCompanyIdDouble2 = mock(PublishingCompanyId.class);

        List<PublishingCompanyId> expected = List.of(_publishingCompanyIdDouble, publishingCompanyIdDouble2);

        when(_iPublishingCompanyRepoDouble.findAllKeys()).thenReturn(expected);

        //Act
        Iterable<PublishingCompanyId> result = _controller.findAllKeys();

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

        //Act
        List<ItemId> result = _controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble);

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
        when(_editionDouble.isByPublishingCompanyId(_publishingCompanyIdDouble)).thenReturn(false);

        // Act
        List<ItemId> result = _controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble);

        // Assert
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
        when(_editionDouble.isByPublishingCompanyId(_publishingCompanyIdDouble)).thenReturn(true);

        //Act
        List<ItemId> result = _controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble);

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
                () -> _controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble));
    }

    @Test
    void shouldThrowExceptionWhenEditionNotFound() {
        // Arrange
        when(_iDirectSaleRepoDouble.findAll()).thenReturn(List.of(_directSaleDouble));
        when(_directSaleDouble.getItemsId()).thenReturn(List.of(_itemIdDouble));
        when(_iEditionRepoDouble.ofIdentity(_editionIdDouble)).thenReturn(Optional.empty());
        when(_iItemRepoDouble.ofIdentity(_itemIdDouble)).thenReturn(Optional.of(_itemDouble));
        when(_itemDouble.getEditionId()).thenReturn(_editionIdDouble);

        //Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.getDirectSaleItemsByPublishingCompany(_publishingCompanyIdDouble));
    }

}
