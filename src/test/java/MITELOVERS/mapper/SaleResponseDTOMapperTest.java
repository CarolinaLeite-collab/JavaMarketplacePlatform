package MITELOVERS.mapper;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.SaleResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SaleResponseDTOMapperTest {

    @Test
    void toModelReturnsCorrectDTOWhenCompletedAtIsPresent() {
        // Arrange
        Sale saleDouble = mock(Sale.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(29.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 18, 10, 0, 0);
        LocalDateTime completedAt = LocalDateTime.of(2026, 6, 18, 12, 0, 0);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(createdAt);
        when(saleDouble.get_completedAt()).thenReturn(completedAt);

        // SUT
        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals("SA-1234ABCD", result.getSaleId());
        assertEquals("pedro@aeiou.com", result.getBuyerId());
        assertEquals(29.99, result.getTotalAmount());
        assertEquals("EUR", result.getCurrency());
        assertEquals(createdAt.toString(), result.getCreatedAt());
        assertEquals(completedAt.toString(), result.getCompletedAt());
    }

    @Test
    void toModelReturnsCorrectDTOWhenCompletedAtIsNull() {
        // Arrange
        Sale saleDouble = mock(Sale.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(29.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 18, 10, 0, 0);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(createdAt);
        when(saleDouble.get_completedAt()).thenReturn(null);

        // SUT
        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals("SA-1234ABCD", result.getSaleId());
        assertEquals("pedro@aeiou.com", result.getBuyerId());
        assertEquals(29.99, result.getTotalAmount());
        assertEquals("EUR", result.getCurrency());
        assertEquals(createdAt.toString(), result.getCreatedAt());
        assertNull(result.getCompletedAt());
    }

    @Test
    void toModelReturnsCorrectSaleId() {
        // Arrange
        Sale saleDouble = mock(Sale.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(29.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(LocalDateTime.of(2026, 6, 18, 10, 0, 0));
        when(saleDouble.get_completedAt()).thenReturn(null);

        // SUT
        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals("SA-1234ABCD", result.getSaleId());
    }

    @Test
    void toModelReturnsCorrectBuyerId() {
        // Arrange
        Sale saleDouble = mock(Sale.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(29.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(LocalDateTime.of(2026, 6, 18, 10, 0, 0));
        when(saleDouble.get_completedAt()).thenReturn(null);

        // SUT
        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals("pedro@aeiou.com", result.getBuyerId());
    }

    @Test
    void toModelReturnsCorrectTotalAmount() {
        // Arrange
        Sale saleDouble = mock(Sale.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(29.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(LocalDateTime.of(2026, 6, 18, 10, 0, 0));
        when(saleDouble.get_completedAt()).thenReturn(null);

        // SUT
        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals(29.99, result.getTotalAmount());
    }

    @Test
    void toModelReturnsCorrectCurrency() {
        // Arrange
        Sale saleDouble = mock(Sale.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(29.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(LocalDateTime.of(2026, 6, 18, 10, 0, 0));
        when(saleDouble.get_completedAt()).thenReturn(null);

        // SUT
        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals("EUR", result.getCurrency());
    }

    @Test
    void toModelReturnsCorrectCreatedAt() {
        // Arrange
        Sale saleDouble = mock(Sale.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(29.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 18, 10, 0, 0);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(createdAt);
        when(saleDouble.get_completedAt()).thenReturn(null);

        // SUT
        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals(createdAt.toString(), result.getCreatedAt());
    }

    @Test
    void toModelReturnsCorrectCompletedAt() {
        // Arrange
        Sale saleDouble = mock(Sale.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("pedro@aeiou.com");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(29.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 18, 10, 0, 0);
        LocalDateTime completedAt = LocalDateTime.of(2026, 6, 18, 12, 0, 0);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(createdAt);
        when(saleDouble.get_completedAt()).thenReturn(completedAt);

        // SUT
        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals(completedAt.toString(), result.getCompletedAt());
    }
}