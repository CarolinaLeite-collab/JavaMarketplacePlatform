package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.sale.SaleLineFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import MITELOVERS.persistence.jpa.datamodel.SaleLineDataModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleLineAssemblerTest {

    @Mock
    private SaleLineFactory _saleLineFactoryDouble;

    @Test
    void testToDataModelShouldReturnASaleLineDataModel() {

        // Arrange
        SaleLine saleLineDouble = mock(SaleLine.class);
        SaleDataModel saleDataModelDouble = mock(SaleDataModel.class);
        SaleLineId saleLineIdDouble = mock(SaleLineId.class);
        UserId sellerIdDouble = mock(UserId.class);
        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        Price priceDouble = mock(Price.class);
        Currency currencyDouble = mock(Currency.class);

        when(saleLineDouble.identity()).thenReturn(saleLineIdDouble);
        when(saleLineDouble.get_sellerId()).thenReturn(sellerIdDouble);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);
        when(saleLineDouble.get_priceAtSale()).thenReturn(priceDouble);

        when(priceDouble.getValue()).thenReturn(20.0);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        // SUT
        SaleLineAssembler assembler = new SaleLineAssembler(_saleLineFactoryDouble);

        // Act
        SaleLineDataModel result = assembler.toDataModel(saleLineDouble, saleDataModelDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testToDomainShouldReturnASaleLine() {

        // Arrange
        SaleLineDataModel saleLineDataModelDouble = mock(SaleLineDataModel.class);
        PriceDataModel priceDataModelDouble = mock(PriceDataModel.class);
        SaleLine expected = mock(SaleLine.class);

        when(saleLineDataModelDouble.getSaleLineId()).thenReturn("SL-A1B2C3D4");
        when(saleLineDataModelDouble.getSellerId()).thenReturn("email@email.com");
        when(saleLineDataModelDouble.getDirectSaleId()).thenReturn("DS-A1B2C3D4");
        when(saleLineDataModelDouble.getPrice()).thenReturn(priceDataModelDouble);

        when(priceDataModelDouble.getNumericValue()).thenReturn(20.0);
        when(priceDataModelDouble.getCurrency()).thenReturn("EUR");

        when(_saleLineFactoryDouble.createSale(
                any(SaleLineId.class),
                any(UserId.class),
                any(Price.class),
                any(DirectSaleId.class)
        )).thenReturn(expected);

        // SUT
        SaleLineAssembler assembler = new SaleLineAssembler(_saleLineFactoryDouble);

        // Act
        SaleLine result = assembler.toDomain(saleLineDataModelDouble);

        // Assert
        assertSame(expected, result);
    }

    @Test
    void testToDataModelShouldThrowIfSaleLineIsNull() {

        // SUT
        SaleLineAssembler assembler = new SaleLineAssembler(_saleLineFactoryDouble);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDataModel(null, mock(SaleDataModel.class))
        );
    }

    @Test
    void testToDomainShouldThrowIfSaleLineDataModelIsNull() {

        // SUT
        SaleLineAssembler assembler = new SaleLineAssembler(_saleLineFactoryDouble);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDomain(null)
        );
    }
}