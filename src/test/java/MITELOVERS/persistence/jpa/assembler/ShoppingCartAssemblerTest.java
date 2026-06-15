package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartFactory;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.datamodel.PriceDataModel;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartDataModel;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartLineDataModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartAssemblerTest {

    @Mock
    private ShoppingCartFactory _shoppingCartFactoryDouble;

    @Mock
    private ShoppingCartLineAssembler _shoppingCartLineAssemblerDouble;

    @Test
    void testToDomainShouldThrowIfShoppingCartDmIsNull() {

        //SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(null)

        );

    }

    @Test
    void testToDomainShouldReturnAShoppingCart() {

        // Arrange
        ShoppingCartDataModel shoppingCartDataModelDouble = mock(ShoppingCartDataModel.class);
        PriceDataModel priceDataModelDouble = mock(PriceDataModel.class);
        ShoppingCartLineDataModel cartLineDmDouble = mock(ShoppingCartLineDataModel.class);
        ShoppingCartLine cartLineDouble = mock(ShoppingCartLine.class);

        when(shoppingCartDataModelDouble.getShoppingCartId()).thenReturn("SC-A1B2C3D4");
        when(shoppingCartDataModelDouble.getBuyerId()).thenReturn("email@email.com");
        when(shoppingCartDataModelDouble.getTotalAmount()).thenReturn(priceDataModelDouble);
        when(priceDataModelDouble.getNumericValue()).thenReturn(20.0);
        when(priceDataModelDouble.getCurrency()).thenReturn("EUR");
        when(shoppingCartDataModelDouble.getShoppingCartLines()).thenReturn(List.of(cartLineDmDouble));
        when(_shoppingCartLineAssemblerDouble.toDomain(cartLineDmDouble)).thenReturn(cartLineDouble);

        ShoppingCart expected = mock(ShoppingCart.class);

        when(_shoppingCartFactoryDouble.createShoppingCart(
                any(), any(), any(), any()
        )).thenReturn(expected);

        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act
        ShoppingCart result = assembler.toDomain(shoppingCartDataModelDouble);

        // Assert
        assertSame(expected, result);
    }

    @Test
    void testToDataModelShouldThrowIfShoppingCartIsNull() {

        //SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDataModel(null)

        );

    }

    @Test
    void testToDataModelShouldReturnAShoppingCartDataModel() {

        //Arrange
        ShoppingCart shoppingCartDouble = mock(ShoppingCart.class);
        UserId buyerIdDouble = mock(UserId.class);
        ShoppingCartId shoppingCartIdDouble = mock(ShoppingCartId.class);
        Price priceDouble = mock(Price.class);

        when(priceDouble.getCurrency()).thenReturn(mock(Currency.class));
        when(shoppingCartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(shoppingCartDouble.identity()).thenReturn(shoppingCartIdDouble);
        when(shoppingCartDouble.getTotalAmount()).thenReturn(priceDouble);
        when(shoppingCartDouble.getCartLines()).thenReturn(List.of(mock(ShoppingCartLine.class)));

        //SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        //Act
        ShoppingCartDataModel result = assembler.toDataModel(shoppingCartDouble);

        //Assert
        assertNotNull(result);

    }
}