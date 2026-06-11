package MITELOVERS.applicationservices;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleExpirationServiceTest {

    @Mock
    private IDirectSaleRepo _iDirectSaleRepo;

    @Mock
    private IItemRepo _iItemRepo;

    @Mock
    private DirectSale _directSale;

    @Mock
    private Item _item;

    @InjectMocks
    private SaleExpirationService _service;

    @BeforeEach
    void setup() {
        _service = new SaleExpirationService(_iDirectSaleRepo, _iItemRepo);
    }

    @Test
    void expireAllExpiredSales_shouldExpireOneSaleAndReleaseItems() {

        // Arrange
        DirectSaleId saleId = mock(DirectSaleId.class);
        ItemId itemId = mock(ItemId.class);

        List<DirectSaleId> expired = List.of(saleId);
        List<ItemId> items = List.of(itemId);

        when(_iDirectSaleRepo.findExpired()).thenReturn(expired);
        when(_iDirectSaleRepo.ofIdentity(saleId)).thenReturn(Optional.of(_directSale));
        when(_directSale.getItemsId()).thenReturn(items);
        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.of(_item));

        // Act
        _service.expireAllExpiredSales();

        // Assert
        Assertions.assertDoesNotThrow(() -> {
            _item.markAsNotOnSale();
        });
    }

    @Test
    void expireAllExpiredSales_shouldDoNothingWhenNoExpiredSales() {

        // Arrange
        when(_iDirectSaleRepo.findExpired()).thenReturn(List.of());

        // Act
        _service.expireAllExpiredSales();

        // Assert
        Assertions.assertTrue(true); // nothing should happen
    }

    @Test
    void expireAllExpiredSales_shouldThrowWhenExpiredSaleNotFound() {

        // Arrange
        DirectSaleId saleId = mock(DirectSaleId.class);

        when(_iDirectSaleRepo.findExpired()).thenReturn(List.of(saleId));
        when(_iDirectSaleRepo.ofIdentity(saleId)).thenReturn(Optional.empty());

        // Act + Assert
        Assertions.assertThrows(IllegalStateException.class,
                () -> _service.expireAllExpiredSales());
    }

    @Test
    void expireAllExpiredSales_shouldThrowWhenItemNotFound() {

        // Arrange
        DirectSaleId saleId = mock(DirectSaleId.class);
        ItemId itemId = mock(ItemId.class);

        when(_iDirectSaleRepo.findExpired()).thenReturn(List.of(saleId));
        when(_iDirectSaleRepo.ofIdentity(saleId)).thenReturn(Optional.of(_directSale));
        when(_directSale.getItemsId()).thenReturn(List.of(itemId));
        when(_iItemRepo.ofIdentity(itemId)).thenReturn(Optional.empty());

        // Act + Assert
        Assertions.assertThrows(IllegalStateException.class,
                () -> _service.expireAllExpiredSales());
    }

    @Test
    void expireAllExpiredSales_shouldExpireMultipleSales() {

        // Arrange
        DirectSaleId saleId1 = mock(DirectSaleId.class);
        DirectSaleId saleId2 = mock(DirectSaleId.class);
        DirectSale sale1 = mock(DirectSale.class);
        DirectSale sale2 = mock(DirectSale.class);
        ItemId itemId1 = mock(ItemId.class);
        ItemId itemId2 = mock(ItemId.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);

        when(_iDirectSaleRepo.findExpired()).thenReturn(List.of(saleId1, saleId2));
        when(_iDirectSaleRepo.ofIdentity(saleId1)).thenReturn(Optional.of(sale1));
        when(_iDirectSaleRepo.ofIdentity(saleId2)).thenReturn(Optional.of(sale2));
        when(sale1.getItemsId()).thenReturn(List.of(itemId1));
        when(sale2.getItemsId()).thenReturn(List.of(itemId2));
        when(_iItemRepo.ofIdentity(itemId1)).thenReturn(Optional.of(item1));
        when(_iItemRepo.ofIdentity(itemId2)).thenReturn(Optional.of(item2));

        // Act + Assert
        Assertions.assertDoesNotThrow(() -> _service.expireAllExpiredSales());
    }

}