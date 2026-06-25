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

    // ──────────── toDomain ────────────

    @Test
    void toDomainThrowsWhenShoppingCartDmIsNull() {
        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain(null));
    }

    @Test
    void toDomainReturnsShoppingCartWhenTotalAmountIsPresent() {
        // Arrange
        ShoppingCartDataModel shoppingCartDMDouble = mock(ShoppingCartDataModel.class);
        PriceDataModel priceDataModelDouble = mock(PriceDataModel.class);
        ShoppingCartLineDataModel cartLineDmDouble = mock(ShoppingCartLineDataModel.class);
        ShoppingCartLine cartLineDouble = mock(ShoppingCartLine.class);

        when(shoppingCartDMDouble.getShoppingCartId()).thenReturn("SC-A1B2C3D4");
        when(shoppingCartDMDouble.getBuyerId()).thenReturn("email@email.com");
        when(shoppingCartDMDouble.getTotalAmount()).thenReturn(priceDataModelDouble);
        when(priceDataModelDouble.getNumericValue()).thenReturn(20.0);
        when(priceDataModelDouble.getCurrency()).thenReturn("EUR");
        when(shoppingCartDMDouble.getShoppingCartLines()).thenReturn(List.of(cartLineDmDouble));
        when(_shoppingCartLineAssemblerDouble.toDomain(cartLineDmDouble)).thenReturn(cartLineDouble);

        ShoppingCart expected = mock(ShoppingCart.class);
        when(_shoppingCartFactoryDouble.createShoppingCart(any(), any(), any(), any())).thenReturn(expected);

        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act
        ShoppingCart result = assembler.toDomain(shoppingCartDMDouble);

        // Assert
        assertSame(expected, result);
    }

    @Test
    void toDomainReturnsShoppingCartWhenTotalAmountIsNull() {
        // Arrange
        ShoppingCartDataModel shoppingCartDMDouble = mock(ShoppingCartDataModel.class);

        when(shoppingCartDMDouble.getShoppingCartId()).thenReturn("SC-A1B2C3D4");
        when(shoppingCartDMDouble.getBuyerId()).thenReturn("email@email.com");
        when(shoppingCartDMDouble.getTotalAmount()).thenReturn(null);
        when(shoppingCartDMDouble.getShoppingCartLines()).thenReturn(List.of());

        ShoppingCart expected = mock(ShoppingCart.class);
        when(_shoppingCartFactoryDouble.createShoppingCart(any(), any(), any(), any())).thenReturn(expected);

        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act
        ShoppingCart result = assembler.toDomain(shoppingCartDMDouble);

        // Assert
        assertSame(expected, result);
    }

    @Test
    void toDomainReturnsShoppingCartWithEmptyLines() {
        // Arrange
        ShoppingCartDataModel shoppingCartDMDouble = mock(ShoppingCartDataModel.class);

        when(shoppingCartDMDouble.getShoppingCartId()).thenReturn("SC-A1B2C3D4");
        when(shoppingCartDMDouble.getBuyerId()).thenReturn("email@email.com");
        when(shoppingCartDMDouble.getTotalAmount()).thenReturn(null);
        when(shoppingCartDMDouble.getShoppingCartLines()).thenReturn(List.of());

        ShoppingCart expected = mock(ShoppingCart.class);
        when(_shoppingCartFactoryDouble.createShoppingCart(any(), any(), any(), any())).thenReturn(expected);

        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act
        ShoppingCart result = assembler.toDomain(shoppingCartDMDouble);

        // Assert
        assertSame(expected, result);
    }

    // ──────────── toDataModel ────────────

    @Test
    void toDataModelThrowsWhenShoppingCartIsNull() {
        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDataModel(null));
    }

    @Test
    void toDataModelReturnsDataModelWhenTotalAmountIsPresent() {
        // Arrange
        ShoppingCart shoppingCartDouble = mock(ShoppingCart.class);
        ShoppingCartId shoppingCartIdDouble = mock(ShoppingCartId.class);
        UserId buyerIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);
        Currency currencyDouble = mock(Currency.class);

        when(shoppingCartDouble.identity()).thenReturn(shoppingCartIdDouble);
        when(shoppingCartIdDouble.toString()).thenReturn("SC-A1B2C3D4");
        when(shoppingCartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(buyerIdDouble.toString()).thenReturn("email@email.com");
        when(shoppingCartDouble.getTotalAmount()).thenReturn(priceDouble);
        when(priceDouble.getValue()).thenReturn(20.0);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);
        when(currencyDouble.toString()).thenReturn("EUR");
        when(shoppingCartDouble.getCartLines()).thenReturn(List.of(mock(ShoppingCartLine.class)));

        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act
        ShoppingCartDataModel result = assembler.toDataModel(shoppingCartDouble);

        // Assert
        assertNotNull(result);
        assertEquals("SC-A1B2C3D4", result.getShoppingCartId());
        assertEquals("email@email.com", result.getBuyerId());
        assertNotNull(result.getTotalAmount());
        assertEquals(20.0, result.getTotalAmount().getNumericValue());
        assertEquals("EUR", result.getTotalAmount().getCurrency());
    }

    @Test
    void toDataModelReturnsDataModelWhenTotalAmountIsNull() {
        // Arrange
        ShoppingCart shoppingCartDouble = mock(ShoppingCart.class);
        ShoppingCartId shoppingCartIdDouble = mock(ShoppingCartId.class);
        UserId buyerIdDouble = mock(UserId.class);

        when(shoppingCartDouble.identity()).thenReturn(shoppingCartIdDouble);
        when(shoppingCartIdDouble.toString()).thenReturn("SC-A1B2C3D4");
        when(shoppingCartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(buyerIdDouble.toString()).thenReturn("email@email.com");
        when(shoppingCartDouble.getTotalAmount()).thenReturn(null);
        when(shoppingCartDouble.getCartLines()).thenReturn(List.of());

        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act
        ShoppingCartDataModel result = assembler.toDataModel(shoppingCartDouble);


        // Assert
        assertNotNull(result);
        assertEquals("SC-A1B2C3D4", result.getShoppingCartId());
        assertEquals("email@email.com", result.getBuyerId());
        assertNull(result.getTotalAmount());
        assertTrue(result.getShoppingCartLines().isEmpty());
    }

    @Test
    void toDataModelReturnsDataModelWithEmptyLines() {
        // Arrange
        ShoppingCart shoppingCartDouble = mock(ShoppingCart.class);
        ShoppingCartId shoppingCartIdDouble = mock(ShoppingCartId.class);
        UserId buyerIdDouble = mock(UserId.class);

        when(shoppingCartDouble.identity()).thenReturn(shoppingCartIdDouble);
        when(shoppingCartIdDouble.toString()).thenReturn("SC-A1B2C3D4");
        when(shoppingCartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(buyerIdDouble.toString()).thenReturn("email@email.com");
        when(shoppingCartDouble.getTotalAmount()).thenReturn(null);
        when(shoppingCartDouble.getCartLines()).thenReturn(List.of());

        // SUT
        ShoppingCartAssembler assembler = new ShoppingCartAssembler(_shoppingCartFactoryDouble, _shoppingCartLineAssemblerDouble);

        // Act
        ShoppingCartDataModel result = assembler.toDataModel(shoppingCartDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.getShoppingCartLines().isEmpty());
    }
}