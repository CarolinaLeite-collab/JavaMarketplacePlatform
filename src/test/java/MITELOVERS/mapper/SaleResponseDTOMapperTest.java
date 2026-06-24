package MITELOVERS.mapper;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import MITELOVERS.dto.response.SaleResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    void toModelMapsSaleLines() {
        // Arrange
        Sale saleDouble = mock(Sale.class);
        SaleLine saleLineDouble = mock(SaleLine.class);

        SaleId saleIdDouble = mock(SaleId.class);
        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");

        UserId buyerIdDouble = mock(UserId.class);
        when(buyerIdDouble.toString()).thenReturn("buyer@aeiou.com");

        Price totalAmountDouble = mock(Price.class);
        Currency totalCurrencyDouble = mock(Currency.class);
        when(totalAmountDouble.getValue()).thenReturn(29.99);
        when(totalAmountDouble.getCurrency()).thenReturn(totalCurrencyDouble);
        when(totalCurrencyDouble.toString()).thenReturn("EUR");

        SaleLineId saleLineIdDouble = mock(SaleLineId.class);
        when(saleLineIdDouble.toString()).thenReturn("SL-1234ABCD");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("seller@aeiou.com");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1234ABCD");

        Price priceAtSaleDouble = mock(Price.class);
        Currency lineCurrencyDouble = mock(Currency.class);
        when(priceAtSaleDouble.getValue()).thenReturn(14.99);
        when(priceAtSaleDouble.getCurrency()).thenReturn(lineCurrencyDouble);
        when(lineCurrencyDouble.toString()).thenReturn("EUR");

        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 18, 10, 0);

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(totalAmountDouble);
        when(saleDouble.get_createdAt()).thenReturn(createdAt);
        when(saleDouble.get_completedAt()).thenReturn(null);
        when(saleDouble.get_saleLines()).thenReturn(List.of(saleLineDouble));

        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineIdDouble);
        when(saleLineDouble.get_sellerId()).thenReturn(sellerIdDouble);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);
        when(saleLineDouble.get_priceAtSale()).thenReturn(priceAtSaleDouble);

        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertEquals(1, result.getSaleLines().size());

        SaleLineResponseDTO lineResult = result.getSaleLines().getFirst();
        assertEquals("SL-1234ABCD", lineResult.getSaleLineId());
        assertEquals("seller@aeiou.com", lineResult.getSellerId());
        assertEquals("DS-1234ABCD", lineResult.getDirectSaleId());
        assertEquals(14.99, lineResult.getPrice());
        assertEquals("EUR", lineResult.getCurrency());
    }

    @Test
    void toModelReturnsEmptySaleLinesWhenSaleHasNoLines() {
        // Arrange
        Sale saleDouble = mock(Sale.class);
        SaleId saleIdDouble = mock(SaleId.class);
        UserId buyerIdDouble = mock(UserId.class);
        Price priceDouble = mock(Price.class);
        Currency currencyDouble = mock(Currency.class);
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 18, 10, 0);

        when(saleIdDouble.toString()).thenReturn("SA-1234ABCD");
        when(buyerIdDouble.toString()).thenReturn("buyer@aeiou.com");
        when(priceDouble.getValue()).thenReturn(0.0);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);
        when(currencyDouble.toString()).thenReturn("EUR");

        when(saleDouble.get_saleId()).thenReturn(saleIdDouble);
        when(saleDouble.get_buyerId()).thenReturn(buyerIdDouble);
        when(saleDouble.get_totalAmount()).thenReturn(priceDouble);
        when(saleDouble.get_createdAt()).thenReturn(createdAt);
        when(saleDouble.get_completedAt()).thenReturn(null);
        when(saleDouble.get_saleLines()).thenReturn(List.of());

        SaleResponseDTOMapper mapper = new SaleResponseDTOMapper();

        // Act
        SaleResponseDTO result = mapper.toModel(saleDouble);

        // Assert
        assertNotNull(result.getSaleLines());
        assertTrue(result.getSaleLines().isEmpty());
    }
}
