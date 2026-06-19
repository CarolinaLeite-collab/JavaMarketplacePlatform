package MITELOVERS.domain.sale;

import MITELOVERS.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleTest {

    @Mock
    private SaleId _saleIdDouble;

    @Mock
    private UserId _buyerIdDouble;

    @Mock
    private SaleLine _saleLineDouble;

    @Mock
    private Price _priceDouble;

    private final String saleIdNullMessage = "saleId cannot be null!";

    private final String buyerIdNullMessage = "buyerId cannot be null!";

    private final String saleLinesNullMessage = "saleLines cannot be null!";

    private final String saleLinesEmptyMessage = "saleLines cannot be empty!";

    private final String createdAtNullMessage = "createdAt cannot be null!";

    private final String saleStatusNullMessage = "saleStatus cannot be null!";

    private final String differentCurrenciesMessage = "All SaleLines must have the same currency";

    @Test
    void testConstructor() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        // Act
        Sale sale = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Assert
        assertNotNull(sale);
    }

    @Test
    void shouldCreateSaleWithGeneratedId() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        // Act
        Sale sale = new Sale(
                _buyerIdDouble,
                List.of(_saleLineDouble)
        );

        // Assert
        assertNotNull(sale);
        assertNotNull(sale.identity());
    }

    @Test
    void shouldReturnSaleIdentity() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Act
        SaleId result = sale.identity();

        // Assert
        assertEquals(_saleIdDouble, result);
    }

    @Test
    void shouldThrowExceptionWhenSaleLinesIsNull() {
        //Act
        NullPointerException exception = assertThrows(
                        NullPointerException.class,
                        () -> new Sale(
                                _saleIdDouble,
                                _buyerIdDouble,
                                null,
                                LocalDateTime.now(),
                                null,
                                SaleSaleStatus.PENDING
                        ));

        //Assert
        assertEquals(saleLinesNullMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCurrenciesAreDifferent() {
        // Arrange
        SaleLine saleLine1 = mock(SaleLine.class);
        SaleLine saleLine2 = mock(SaleLine.class);

        Price price1 = mock(Price.class);
        Price price2 = mock(Price.class);

        when(saleLine1.get_priceAtSale()).thenReturn(price1);
        when(saleLine2.get_priceAtSale()).thenReturn(price2);

        when(price1.getCurrency()).thenReturn(Currency.EUR);
        when(price2.getCurrency()).thenReturn(Currency.USD);

        when(price1.getValue()).thenReturn(10.0);

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new Sale(
                        _saleIdDouble,
                        _buyerIdDouble,
                        List.of(saleLine1, saleLine2),
                        LocalDateTime.now(),
                        null,
                        SaleSaleStatus.PENDING
                ));

        // Assert
        assertEquals(differentCurrenciesMessage, exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenBusinessAttributesAreEqual() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale1 = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        Sale sale2 = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertTrue(sale1.sameAs(sale2));
    }

    @Test
    void shouldReturnFalseWhenObjectIsNotSale() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertFalse(sale.sameAs("not a sale"));
    }

    @Test
    void shouldReturnTrueWhenComparingSameObject() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertEquals(sale, sale);
    }

    @Test
    void shouldReturnTrueWhenSaleIdIsEqual() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale1 = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        Sale sale2 = new Sale(
                _saleIdDouble,
                mock(UserId.class),
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertEquals(sale1, sale2);
    }

    @Test
    void shouldReturnFalseWhenSaleIdIsDifferent() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale1 = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        Sale sale2 = new Sale(
                mock(SaleId.class),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertNotEquals(sale1, sale2);
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNotSale() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertNotEquals(sale, "not a sale");
    }

    @Test
    void shouldReturnSameHashCodeWhenSaleIdIsEqual() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale1 = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        Sale sale2 = new Sale(
                _saleIdDouble,
                mock(UserId.class),
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertEquals(sale1.hashCode(), sale2.hashCode());
    }

    @Test
    void shouldReturnExpectedString() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        String expected = "\nSale Id: " + _saleIdDouble +
                "\nBuyer Id: " + _buyerIdDouble +
                "\nSale Status: " + SaleSaleStatus.PENDING +
                "\nTotal Amount: " + new Price(10.0, Currency.EUR) +
                "\nCreated At: " + createdAt +
                "\nCompleted At: " + null +
                "\nSale Lines: " + List.of(_saleLineDouble);

        // Act & Assert
        assertEquals(expected, sale.toString());
    }

    @Test
    void shouldReturnFalseWhenBuyerIdIsDifferent() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        Sale otherSale = new Sale(
                new SaleId(),
                mock(UserId.class),
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertFalse(sale.sameAs(otherSale));
    }

    @Test
    void shouldReturnFalseWhenSaleLinesAreDifferent() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        SaleLine anotherSaleLine = mock(SaleLine.class);
        Price anotherPrice = mock(Price.class);

        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        when(anotherSaleLine.get_priceAtSale()).thenReturn(anotherPrice);
        when(anotherPrice.getCurrency()).thenReturn(Currency.EUR);
        when(anotherPrice.getValue()).thenReturn(20.0);

        Sale sale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        Sale otherSale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(anotherSaleLine),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertFalse(sale.sameAs(otherSale));
    }

    @Test
    void shouldReturnFalseWhenCreatedAtIsDifferent() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.of(2026, 1, 1, 10, 0),
                null,
                SaleSaleStatus.PENDING
        );

        Sale otherSale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.of(2026, 1, 2, 10, 0),
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertFalse(sale.sameAs(otherSale));
    }

    @Test
    void shouldReturnFalseWhenCompletedAtIsDifferent() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        Sale otherSale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                LocalDateTime.now(),
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertFalse(sale.sameAs(otherSale));
    }

    @Test
    void shouldReturnFalseWhenSaleStatusIsDifferent() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.PENDING
        );

        Sale otherSale = new Sale(
                new SaleId(),
                _buyerIdDouble,
                List.of(_saleLineDouble),
                createdAt,
                null,
                SaleSaleStatus.COMPLETED
        );

        // Act & Assert
        assertFalse(sale.sameAs(otherSale));
    }

    @Test
    void sameAsShouldReturnTrueWhenComparingSameObject() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        Sale sale = new Sale(
                _saleIdDouble,
                _buyerIdDouble,
                List.of(_saleLineDouble),
                LocalDateTime.now(),
                null,
                SaleSaleStatus.PENDING
        );

        // Act & Assert
        assertTrue(sale.sameAs(sale));
    }

    @Test
    void shouldThrowExceptionWhenCreatedAtIsNull() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Sale(
                        _saleIdDouble,
                        _buyerIdDouble,
                        List.of(_saleLineDouble),
                        null,
                        null,
                        SaleSaleStatus.PENDING
                ));

        // Assert
        assertEquals(createdAtNullMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaleIdIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Sale(
                        null,
                        _buyerIdDouble,
                        List.of(_saleLineDouble),
                        LocalDateTime.now(),
                        null,
                        SaleSaleStatus.PENDING
                ));

        // Assert
        assertEquals(saleIdNullMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBuyerIdIsNull() {
        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Sale(
                        _saleIdDouble,
                        null,
                        List.of(_saleLineDouble),
                        LocalDateTime.now(),
                        null,
                        SaleSaleStatus.PENDING
                ));

        // Assert
        assertEquals(buyerIdNullMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaleLinesIsEmpty() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Sale(
                        _saleIdDouble,
                        _buyerIdDouble,
                        List.of(),
                        LocalDateTime.now(),
                        null,
                        SaleSaleStatus.PENDING
                ));

        // Assert
        assertEquals(saleLinesEmptyMessage, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenSaleStatusIsNull() {
        // Arrange
        when(_saleLineDouble.get_priceAtSale()).thenReturn(_priceDouble);
        when(_priceDouble.getCurrency()).thenReturn(Currency.EUR);
        when(_priceDouble.getValue()).thenReturn(10.0);

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Sale(
                        _saleIdDouble,
                        _buyerIdDouble,
                        List.of(_saleLineDouble),
                        LocalDateTime.now(),
                        null,
                        null
                ));

        // Assert
        assertEquals(saleStatusNullMessage, exception.getMessage());
    }
}