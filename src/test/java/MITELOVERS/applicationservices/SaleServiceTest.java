package MITELOVERS.applicationservices;

import MITELOVERS.domain.repository.ISaleRepo;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private ISaleRepo _saleRepo;

    @InjectMocks
    private SaleService _service;

    @Test
    void findUserSalesReturnsListOfSalesWhenFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(userIdDouble);

        Sale sale1Double = mock(Sale.class);
        Sale sale2Double = mock(Sale.class);

        when(_saleRepo.findByUserId(userIdDouble)).thenReturn(List.of(sale1Double, sale2Double));

        // Act
        List<Sale> result = _service.findUserSales(userDouble);

        // Assert
        assertEquals(2, result.size());
        assertSame(sale1Double, result.get(0));
        assertSame(sale2Double, result.get(1));
    }

    @Test
    void findUserSalesReturnsEmptyListWhenNoSalesFound() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        User userDouble = mock(User.class);
        when(userDouble.identity()).thenReturn(userIdDouble);

        when(_saleRepo.findByUserId(userIdDouble)).thenReturn(List.of());

        // Act
        List<Sale> result = _service.findUserSales(userDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findSaleByIdReturnsSaleWhenFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        Sale saleDouble = mock(Sale.class);

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.of(saleDouble));

        // Act
        Sale result = _service.findSaleById(saleIdDouble);

        // Assert
        assertSame(saleDouble, result);
    }

    @Test
    void findSaleByIdThrowsWhenNotFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.findSaleById(saleIdDouble));
    }

    @Test
    void getSaleLineByIdReturnsSaleLineWhenFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        SaleLine saleLineDouble = mock(SaleLine.class);
        when(saleLineDouble.identity()).thenReturn(saleLineId);

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleLines()).thenReturn(List.of(saleLineDouble));

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.of(saleDouble));

        // Act
        SaleLine result = _service.getSaleLineById(saleIdDouble, saleLineId);

        // Assert
        assertSame(saleLineDouble, result);
    }

    @Test
    void getSaleLineByIdThrowsWhenLineNotFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        Sale saleDouble = mock(Sale.class);
        when(saleDouble.get_saleLines()).thenReturn(List.of());

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.of(saleDouble));

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.getSaleLineById(saleIdDouble, saleLineId));
    }

    @Test
    void getSaleLineByIdThrowsWhenSaleNotFound() {
        // Arrange
        SaleId saleIdDouble = mock(SaleId.class);
        SaleLineId saleLineId = new SaleLineId("SL-1234ABCD");

        when(_saleRepo.ofIdentity(saleIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NoSuchElementException.class,
                () -> _service.getSaleLineById(saleIdDouble, saleLineId));
    }
}