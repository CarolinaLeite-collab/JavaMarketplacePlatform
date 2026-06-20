package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleFactory;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.SaleSaleStatus;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import MITELOVERS.persistence.jpa.datamodel.SaleLineDataModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleAssemblerTest {

    @InjectMocks
    private SaleAssembler _saleAssembler;

    @Mock
    private SaleLineAssembler _saleLineAssembler;

    @Mock
    private SaleFactory _saleFactory;

    @Mock
    private Sale _saleDouble;

    @Mock
    private SaleLine _saleLineDouble;

    @Mock
    private SaleLineDataModel _saleLineDataModelDouble;

    @Mock
    private SaleDataModel _saleDataModelDouble;

    // ------------------------------------------------------------
    // toDataModel
    // ------------------------------------------------------------

    @Test
    void toDataModelShouldReturnSaleDataModel() {

        // Arrange
        SaleId saleId = new SaleId("SA-12345678");
        UserId buyerId = new UserId(new Email("buyer@email.com"));
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 19, 10, 0);
        LocalDateTime completedAt = LocalDateTime.of(2026, 6, 19, 11, 0);

        when(_saleDouble.identity()).thenReturn(saleId);
        when(_saleDouble.get_buyerId()).thenReturn(buyerId);
        when(_saleDouble.get_saleSaleStatus()).thenReturn(SaleSaleStatus.COMPLETED);
        when(_saleDouble.get_createdAt()).thenReturn(createdAt);
        when(_saleDouble.get_completedAt()).thenReturn(completedAt);
        when(_saleDouble.get_saleLines()).thenReturn(List.of(_saleLineDouble));

        when(_saleLineAssembler.toDataModel(any(SaleLine.class), any(SaleDataModel.class)))
                .thenReturn(_saleLineDataModelDouble);

        // Act
        SaleDataModel result = _saleAssembler.toDataModel(_saleDouble);

        // Assert
        assertNotNull(result);
        assertEquals("SA-12345678", result.getSaleId());
        assertEquals("buyer@email.com", result.getUserId());
        assertEquals(SaleSaleStatus.COMPLETED, result.getStatus());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(completedAt, result.getCompletedAt());
        assertEquals(1, result.getSaleLines().size());
        assertSame(_saleLineDataModelDouble, result.getSaleLines().get(0));

        verify(_saleLineAssembler).toDataModel(eq(_saleLineDouble), same(result));
    }

    // ------------------------------------------------------------
    // toDomain
    // ------------------------------------------------------------

    @Test
    void toDomainShouldReturnSale() {

        // Arrange
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 19, 10, 0);
        LocalDateTime completedAt = LocalDateTime.of(2026, 6, 19, 11, 0);

        when(_saleDataModelDouble.getSaleId()).thenReturn("SA-12345678");
        when(_saleDataModelDouble.getUserId()).thenReturn("buyer@email.com");
        when(_saleDataModelDouble.getSaleLines()).thenReturn(List.of(_saleLineDataModelDouble));
        when(_saleDataModelDouble.getCreatedAt()).thenReturn(createdAt);
        when(_saleDataModelDouble.getCompletedAt()).thenReturn(completedAt);
        when(_saleDataModelDouble.getStatus()).thenReturn(SaleSaleStatus.COMPLETED);

        when(_saleLineAssembler.toDomain(_saleLineDataModelDouble)).thenReturn(_saleLineDouble);

        when(_saleFactory.createSale(
                any(SaleId.class),
                any(UserId.class),
                eq(List.of(_saleLineDouble)),
                eq(createdAt),
                eq(completedAt),
                eq(SaleSaleStatus.COMPLETED)
        )).thenReturn(_saleDouble);

        // Act
        Sale result = _saleAssembler.toDomain(_saleDataModelDouble);

        // Assert
        assertNotNull(result);
        assertSame(_saleDouble, result);

        verify(_saleLineAssembler).toDomain(_saleLineDataModelDouble);

        verify(_saleFactory).createSale(
                eq(new SaleId("SA-12345678")),
                eq(new UserId(new Email("buyer@email.com"))),
                eq(List.of(_saleLineDouble)),
                eq(createdAt),
                eq(completedAt),
                eq(SaleSaleStatus.COMPLETED)
        );
    }

    @Test
    void toDomainShouldReturnSaleWithNullCompletedAt() {

        // Arrange
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 19, 10, 0);

        when(_saleDataModelDouble.getSaleId()).thenReturn("SA-12345678");
        when(_saleDataModelDouble.getUserId()).thenReturn("buyer@email.com");
        when(_saleDataModelDouble.getSaleLines()).thenReturn(List.of(_saleLineDataModelDouble));
        when(_saleDataModelDouble.getCreatedAt()).thenReturn(createdAt);
        when(_saleDataModelDouble.getCompletedAt()).thenReturn(null);
        when(_saleDataModelDouble.getStatus()).thenReturn(SaleSaleStatus.PENDING);

        when(_saleLineAssembler.toDomain(_saleLineDataModelDouble)).thenReturn(_saleLineDouble);

        when(_saleFactory.createSale(
                any(SaleId.class),
                any(UserId.class),
                eq(List.of(_saleLineDouble)),
                eq(createdAt),
                isNull(),
                eq(SaleSaleStatus.PENDING)
        )).thenReturn(_saleDouble);

        // Act
        Sale result = _saleAssembler.toDomain(_saleDataModelDouble);

        // Assert
        assertNotNull(result);
        assertSame(_saleDouble, result);
    }
}