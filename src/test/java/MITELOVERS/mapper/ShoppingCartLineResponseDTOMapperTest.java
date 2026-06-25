package MITELOVERS.mapper;

import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.ShoppingCartLineResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingCartLineResponseDTOMapperTest {

    @Test
    void toModelReturnsCorrectDTO() {
        // Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);
        when(lineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        LocalDateTime addedAt = LocalDateTime.of(2026, 6, 18, 15, 30, 0);

        when(lineDouble.identity()).thenReturn(lineIdDouble);
        when(lineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);
        when(lineDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(lineDouble.getAddedAt()).thenReturn(addedAt);

        // SUT
        ShoppingCartLineResponseDTOMapper mapper = new ShoppingCartLineResponseDTOMapper();

        // Act
        ShoppingCartLineResponseDTO result = mapper.toModel(lineDouble);

        // Assert
        assertNotNull(result);
        assertEquals("SCL-1234ABCD", result.getShoppingCartLineId());
        assertEquals("DS-1A2B3C4DE", result.getDirectSaleId());
        assertEquals("ana@aeiou.com", result.getSellerId());
        assertEquals(14.99, result.getPriceAtAddition());
        assertEquals("EUR", result.getCurrency());
        assertEquals(addedAt.toString(), result.getAddedAt());
    }

    @Test
    void toModelReturnsCorrectShoppingCartLineId() {
        // Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);
        when(lineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(lineDouble.identity()).thenReturn(lineIdDouble);
        when(lineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);
        when(lineDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(lineDouble.getAddedAt()).thenReturn(LocalDateTime.of(2026, 6, 18, 15, 30, 0));

        // SUT
        ShoppingCartLineResponseDTOMapper mapper = new ShoppingCartLineResponseDTOMapper();

        // Act
        ShoppingCartLineResponseDTO result = mapper.toModel(lineDouble);

        // Assert
        assertEquals("SCL-1234ABCD", result.getShoppingCartLineId());
    }

    @Test
    void toModelReturnsCorrectDirectSaleId() {
        // Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);
        when(lineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(lineDouble.identity()).thenReturn(lineIdDouble);
        when(lineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);
        when(lineDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(lineDouble.getAddedAt()).thenReturn(LocalDateTime.of(2026, 6, 18, 15, 30, 0));

        // SUT
        ShoppingCartLineResponseDTOMapper mapper = new ShoppingCartLineResponseDTOMapper();

        // Act
        ShoppingCartLineResponseDTO result = mapper.toModel(lineDouble);

        // Assert
        assertEquals("DS-1A2B3C4DE", result.getDirectSaleId());
    }

    @Test
    void toModelReturnsCorrectSellerId() {
        // Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);
        when(lineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(lineDouble.identity()).thenReturn(lineIdDouble);
        when(lineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);
        when(lineDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(lineDouble.getAddedAt()).thenReturn(LocalDateTime.of(2026, 6, 18, 15, 30, 0));

        // SUT
        ShoppingCartLineResponseDTOMapper mapper = new ShoppingCartLineResponseDTOMapper();

        // Act
        ShoppingCartLineResponseDTO result = mapper.toModel(lineDouble);

        // Assert
        assertEquals("ana@aeiou.com", result.getSellerId());
    }

    @Test
    void toModelReturnsCorrectPrice() {
        // Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);
        when(lineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(lineDouble.identity()).thenReturn(lineIdDouble);
        when(lineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);
        when(lineDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(lineDouble.getAddedAt()).thenReturn(LocalDateTime.of(2026, 6, 18, 15, 30, 0));

        // SUT
        ShoppingCartLineResponseDTOMapper mapper = new ShoppingCartLineResponseDTOMapper();

        // Act
        ShoppingCartLineResponseDTO result = mapper.toModel(lineDouble);

        // Assert
        assertEquals(14.99, result.getPriceAtAddition());
        assertEquals("EUR", result.getCurrency());
    }

    @Test
    void toModelReturnsCorrectAddedAt() {
        // Arrange
        ShoppingCartLine lineDouble = mock(ShoppingCartLine.class);

        ShoppingCartLineId lineIdDouble = mock(ShoppingCartLineId.class);
        when(lineIdDouble.toString()).thenReturn("SCL-1234ABCD");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        LocalDateTime addedAt = LocalDateTime.of(2026, 6, 18, 15, 30, 0);

        when(lineDouble.identity()).thenReturn(lineIdDouble);
        when(lineDouble.getDirectSaleId()).thenReturn(directSaleIdDouble);
        when(lineDouble.getSellerId()).thenReturn(sellerIdDouble);
        when(lineDouble.getPriceAtAddition()).thenReturn(priceDouble);
        when(lineDouble.getAddedAt()).thenReturn(addedAt);

        // SUT
        ShoppingCartLineResponseDTOMapper mapper = new ShoppingCartLineResponseDTOMapper();

        // Act
        ShoppingCartLineResponseDTO result = mapper.toModel(lineDouble);

        // Assert
        assertEquals(addedAt.toString(), result.getAddedAt());
    }
}