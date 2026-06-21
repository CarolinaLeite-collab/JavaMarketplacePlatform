package MITELOVERS.mapper;

import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SaleLineResponseDTOMapperTest {

    @Test
    void toModelReturnsCorrectDTO() {
        // Arrange
        SaleLine saleLineDouble = mock(SaleLine.class);

        SaleLineId saleLineIdDouble = mock(SaleLineId.class);
        when(saleLineIdDouble.toString()).thenReturn("SL-1234ABCD");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineIdDouble);
        when(saleLineDouble.get_sellerId()).thenReturn(sellerIdDouble);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);
        when(saleLineDouble.get_priceAtSale()).thenReturn(priceDouble);

        // SUT
        SaleLineResponseDTOMapper mapper = new SaleLineResponseDTOMapper();

        // Act
        SaleLineResponseDTO result = mapper.toModel(saleLineDouble);

        // Assert
        assertEquals("SL-1234ABCD", result.getSaleLineId());
        assertEquals("ana@aeiou.com", result.getUserId());
        assertEquals("DS-1A2B3C4DE", result.getDirectSaleId());
        assertEquals(14.99, result.getPrice());
        assertEquals("EUR", result.getCurrency());
    }

    @Test
    void toModelReturnsCorrectSaleLineId() {
        // Arrange
        SaleLine saleLineDouble = mock(SaleLine.class);

        SaleLineId saleLineIdDouble = mock(SaleLineId.class);
        when(saleLineIdDouble.toString()).thenReturn("SL-1234ABCD");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineIdDouble);
        when(saleLineDouble.get_sellerId()).thenReturn(sellerIdDouble);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);
        when(saleLineDouble.get_priceAtSale()).thenReturn(priceDouble);

        // SUT
        SaleLineResponseDTOMapper mapper = new SaleLineResponseDTOMapper();

        // Act
        SaleLineResponseDTO result = mapper.toModel(saleLineDouble);

        // Assert
        assertEquals("SL-1234ABCD", result.getSaleLineId());
    }

    @Test
    void toModelReturnsCorrectSellerId() {
        // Arrange
        SaleLine saleLineDouble = mock(SaleLine.class);

        SaleLineId saleLineIdDouble = mock(SaleLineId.class);
        when(saleLineIdDouble.toString()).thenReturn("SL-1234ABCD");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineIdDouble);
        when(saleLineDouble.get_sellerId()).thenReturn(sellerIdDouble);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);
        when(saleLineDouble.get_priceAtSale()).thenReturn(priceDouble);

        // SUT
        SaleLineResponseDTOMapper mapper = new SaleLineResponseDTOMapper();

        // Act
        SaleLineResponseDTO result = mapper.toModel(saleLineDouble);

        // Assert
        assertEquals("ana@aeiou.com", result.getUserId());
    }

    @Test
    void toModelReturnsCorrectDirectSaleId() {
        // Arrange
        SaleLine saleLineDouble = mock(SaleLine.class);

        SaleLineId saleLineIdDouble = mock(SaleLineId.class);
        when(saleLineIdDouble.toString()).thenReturn("SL-1234ABCD");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineIdDouble);
        when(saleLineDouble.get_sellerId()).thenReturn(sellerIdDouble);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);
        when(saleLineDouble.get_priceAtSale()).thenReturn(priceDouble);

        // SUT
        SaleLineResponseDTOMapper mapper = new SaleLineResponseDTOMapper();

        // Act
        SaleLineResponseDTO result = mapper.toModel(saleLineDouble);

        // Assert
        assertEquals("DS-1A2B3C4DE", result.getDirectSaleId());
    }

    @Test
    void toModelReturnsCorrectPrice() {
        // Arrange
        SaleLine saleLineDouble = mock(SaleLine.class);

        SaleLineId saleLineIdDouble = mock(SaleLineId.class);
        when(saleLineIdDouble.toString()).thenReturn("SL-1234ABCD");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineIdDouble);
        when(saleLineDouble.get_sellerId()).thenReturn(sellerIdDouble);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);
        when(saleLineDouble.get_priceAtSale()).thenReturn(priceDouble);

        // SUT
        SaleLineResponseDTOMapper mapper = new SaleLineResponseDTOMapper();

        // Act
        SaleLineResponseDTO result = mapper.toModel(saleLineDouble);

        // Assert
        assertEquals(14.99, result.getPrice());
    }

    @Test
    void toModelReturnsCorrectCurrency() {
        // Arrange
        SaleLine saleLineDouble = mock(SaleLine.class);

        SaleLineId saleLineIdDouble = mock(SaleLineId.class);
        when(saleLineIdDouble.toString()).thenReturn("SL-1234ABCD");

        UserId sellerIdDouble = mock(UserId.class);
        when(sellerIdDouble.toString()).thenReturn("ana@aeiou.com");

        DirectSaleId directSaleIdDouble = mock(DirectSaleId.class);
        when(directSaleIdDouble.toString()).thenReturn("DS-1A2B3C4DE");

        Currency currencyDouble = mock(Currency.class);
        when(currencyDouble.toString()).thenReturn("EUR");

        Price priceDouble = mock(Price.class);
        when(priceDouble.getValue()).thenReturn(14.99);
        when(priceDouble.getCurrency()).thenReturn(currencyDouble);

        when(saleLineDouble.get_saleLineId()).thenReturn(saleLineIdDouble);
        when(saleLineDouble.get_sellerId()).thenReturn(sellerIdDouble);
        when(saleLineDouble.get_directSaleId()).thenReturn(directSaleIdDouble);
        when(saleLineDouble.get_priceAtSale()).thenReturn(priceDouble);

        // SUT
        SaleLineResponseDTOMapper mapper = new SaleLineResponseDTOMapper();

        // Act
        SaleLineResponseDTO result = mapper.toModel(saleLineDouble);

        // Assert
        assertEquals("EUR", result.getCurrency());
    }
}