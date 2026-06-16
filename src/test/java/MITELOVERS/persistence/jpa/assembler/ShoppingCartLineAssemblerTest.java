package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.shoppingcart.ShoppingCartLineFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartLineDataModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartLineAssemblerTest {

    @Mock
    private ShoppingCartLineFactory _shoppingCartLineFactoryDouble;

    @Test
    void testToDomainShouldThrowIfShoppingCartLineDmIsNull() {

        // SUT
        ShoppingCartLineAssembler assembler = new ShoppingCartLineAssembler(_shoppingCartLineFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(null)
        );
    }

    @Test
    void testToDomainShouldReturnAShoppingCartLine() {

        // Arrange
        ShoppingCartLineDataModel shoppingCartLineDmDouble = mock(ShoppingCartLineDataModel.class);
        PriceDataModel priceDataModelDouble = mock(PriceDataModel.class);
        LocalDateTime addedAt = LocalDateTime.now();

        when(shoppingCartLineDmDouble.getShoppingCartLineId()).thenReturn("SCL-A1B2C3D4");
        when(shoppingCartLineDmDouble.getDirectSaleId()).thenReturn("DS-A1B2C3D4");
        when(shoppingCartLineDmDouble.getSellerId()).thenReturn("seller@email.com");
        when(shoppingCartLineDmDouble.getPriceAtAddition()).thenReturn(priceDataModelDouble);
        when(priceDataModelDouble.getNumericValue()).thenReturn(20.0);
        when(priceDataModelDouble.getCurrency()).thenReturn("EUR");
        when(shoppingCartLineDmDouble.getAddedAt()).thenReturn(addedAt);

        ShoppingCartLine expected = mock(ShoppingCartLine.class);

        when(_shoppingCartLineFactoryDouble.createNewShoppingCartLine(
                any(), any(), any(), any(), any()
        )).thenReturn(expected);

        // SUT
        ShoppingCartLineAssembler assembler = new ShoppingCartLineAssembler(_shoppingCartLineFactoryDouble);

        // Act
        ShoppingCartLine result = assembler.toDomain(shoppingCartLineDmDouble);

        // Assert
        assertSame(expected, result);
    }

    @Test
    void testToDataModelShouldThrowIfShoppingCartLineIsNull() {

        // SUT
        ShoppingCartLineAssembler assembler = new ShoppingCartLineAssembler(_shoppingCartLineFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDataModel(null)
        );
    }

    @Test
    void testToDataModelShouldReturnAShoppingCartLineDataModel() {

        // Arrange
        ShoppingCartLine shoppingCartLineDouble = mock(ShoppingCartLine.class);
        ShoppingCartLineId shoppingCartLineIdDouble = mock(ShoppingCartLineId.class);
        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        UserId sellerIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);
        Currency currencyDouble = mock(Currency.class);
        LocalDateTime addedAt = LocalDateTime.now();

        when(shoppingCartLineDouble.identity()).thenReturn(shoppingCartLineIdDouble);
        when(shoppingCartLineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);
        when(shoppingCartLineDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(shoppingCartLineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(priceDouble.getValue()).thenReturn(20.0);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);
        when(shoppingCartLineDouble.getAddedAt()).thenReturn(addedAt);

        // SUT
        ShoppingCartLineAssembler assembler = new ShoppingCartLineAssembler(_shoppingCartLineFactoryDouble);

        // Act
        ShoppingCartLineDataModel result = assembler.toDataModel(shoppingCartLineDouble);

        // Assert
        assertNotNull(result);
    }

}