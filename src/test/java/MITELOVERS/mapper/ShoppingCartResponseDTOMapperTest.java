package MITELOVERS.mapper;

import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.valueobject.Currency;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.ShoppingCartResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingCartResponseDTOMapperTest {

    @Test
    void toModelReturnsCorrectDTOWhenTotalAmountIsPresent() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A1B2C3D4");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(cartDouble.identity()).thenReturn(cartIdDouble);
        when(cartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(cartDouble.getTotalAmount()).thenReturn(priceDouble);

        // SUT
        ShoppingCartResponseDTOMapper mapper = new ShoppingCartResponseDTOMapper();

        // Act
        ShoppingCartResponseDTO result = mapper.toModel(cartDouble);

        // Assert
        assertNotNull(result);
        assertEquals("SC-A1B2C3D4", result.getShoppingCartId());
        assertEquals("pedro@aeiou.com", result.getBuyerId());
        assertEquals(14.99, result.getTotalAmount());
        assertEquals("EUR", result.getCurrency());
    }

    @Test
    void toModelReturnsCorrectDTOWhenTotalAmountIsNull() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A1B2C3D4");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        when(cartDouble.identity()).thenReturn(cartIdDouble);
        when(cartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(cartDouble.getTotalAmount()).thenReturn(null);

        // SUT
        ShoppingCartResponseDTOMapper mapper = new ShoppingCartResponseDTOMapper();

        // Act
        ShoppingCartResponseDTO result = mapper.toModel(cartDouble);

        // Assert
        assertNotNull(result);
        assertEquals("SC-A1B2C3D4", result.getShoppingCartId());
        assertEquals("pedro@aeiou.com", result.getBuyerId());
        assertNull(result.getTotalAmount());
        assertNull(result.getCurrency());
    }

    @Test
    void toModelReturnsCorrectShoppingCartId() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A1B2C3D4");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        when(cartDouble.identity()).thenReturn(cartIdDouble);
        when(cartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(cartDouble.getTotalAmount()).thenReturn(null);

        // SUT
        ShoppingCartResponseDTOMapper mapper = new ShoppingCartResponseDTOMapper();

        // Act
        ShoppingCartResponseDTO result = mapper.toModel(cartDouble);

        // Assert
        assertEquals("SC-A1B2C3D4", result.getShoppingCartId());
    }

    @Test
    void toModelReturnsCorrectBuyerId() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A1B2C3D4");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        when(cartDouble.identity()).thenReturn(cartIdDouble);
        when(cartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(cartDouble.getTotalAmount()).thenReturn(null);

        // SUT
        ShoppingCartResponseDTOMapper mapper = new ShoppingCartResponseDTOMapper();

        // Act
        ShoppingCartResponseDTO result = mapper.toModel(cartDouble);

        // Assert
        assertEquals("pedro@aeiou.com", result.getBuyerId());
    }

    @Test
    void toModelReturnsCorrectTotalAmount() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A1B2C3D4");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(cartDouble.identity()).thenReturn(cartIdDouble);
        when(cartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(cartDouble.getTotalAmount()).thenReturn(priceDouble);

        // SUT
        ShoppingCartResponseDTOMapper mapper = new ShoppingCartResponseDTOMapper();

        // Act
        ShoppingCartResponseDTO result = mapper.toModel(cartDouble);

        // Assert
        assertEquals(14.99, result.getTotalAmount());
    }

    @Test
    void toModelReturnsCorrectCurrency() {
        // Arrange
        ShoppingCart cartDouble = mock(ShoppingCart.class);

        ShoppingCartId cartIdDouble = mock(ShoppingCartId.class);
        when(cartIdDouble.toString()).thenReturn("SC-A1B2C3D4");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(cartDouble.identity()).thenReturn(cartIdDouble);
        when(cartDouble.getBuyerId()).thenReturn(buyerIdDouble);
        when(cartDouble.getTotalAmount()).thenReturn(priceDouble);

        // SUT
        ShoppingCartResponseDTOMapper mapper = new ShoppingCartResponseDTOMapper();

        // Act
        ShoppingCartResponseDTO result = mapper.toModel(cartDouble);

        // Assert
        assertEquals("EUR", result.getCurrency());
    }
}