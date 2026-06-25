package MITELOVERS.domain.sale;

import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SaleLineTest {

    @Mock
    private SaleLineId _saleLineIdDouble;

    @Mock
    private UserId _sellerIdDouble;

    @Mock
    private Price _priceAtSaleDouble;

    @Mock
    private DirectSaleId _directSaleIdDouble;

    @Test
    void testConstructor() {
        //Act & SUT
        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble);

        //Assert
        assertNotNull(saleLine);
    }

    @Test
    void shouldCreateSaleLineWithGeneratedId() {
        //Act & SUT
        SaleLine saleLine = new SaleLine(
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble);

        //Assert
        assertNotNull(saleLine);
    }

    @Test
    void shouldReturnSaleLineIdentity() {
        //Arrange
        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        //Act
        SaleLineId result = saleLine.identity();

        //Assert
        assertEquals(_saleLineIdDouble, result);
    }

    @Test
    void shouldReturnTrueWhenBusinessAttributesAreEqual() {
        // Arrange
        SaleLine saleLine1 = new SaleLine(
                new SaleLineId(),
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        SaleLine saleLine2 = new SaleLine(
                new SaleLineId(), // different identity
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        //Act & Assert
        assertTrue(saleLine1.sameAs(saleLine2));
    }

    @Test
    void shouldReturnFalseWhenPriceAtSaleIsDifferent() {
        //Arrange
        Price anotherPrice = mock(Price.class);

        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        SaleLine otherSaleLine = new SaleLine(
                mock(SaleLineId.class),
                _sellerIdDouble,
                anotherPrice,
                _directSaleIdDouble
        );

        //Act & Assert
        assertFalse(saleLine.sameAs(otherSaleLine));
    }

    @Test
    void shouldReturnFalseWhenDirectSaleIdIsDifferent() {
        //Arrange
        DirectSaleId anotherDirectSaleId = mock(DirectSaleId.class);

        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        SaleLine otherSaleLine = new SaleLine(
                mock(SaleLineId.class),
                _sellerIdDouble,
                _priceAtSaleDouble,
                anotherDirectSaleId
        );

        //Act & Assert
        assertFalse(saleLine.sameAs(otherSaleLine));
    }

    @Test
    void shouldReturnFalseWhenObjectIsNotSaleLine() {
        //Arrange
        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        //Act & Assert
        assertFalse(saleLine.sameAs("not a sale line"));
    }

    @Test
    void shouldReturnFalseWhenSellerIdIsDifferent() {
        //Arrange
        UserId anotherSellerId = mock(UserId.class);

        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        SaleLine otherSaleLine = new SaleLine(
                mock(SaleLineId.class),
                anotherSellerId,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        //Act & Assert
        assertFalse(saleLine.sameAs(otherSaleLine));
    }

    @Test
    void shouldReturnTrueWhenComparingSameObject() {
        //Arrange
        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        //Act & Assert
        assertEquals(saleLine, saleLine);
    }

    @Test
    void shouldReturnTrueWhenSaleLineIdIsEqual() {
        //Arrange
        SaleLine saleLine1 = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        SaleLine saleLine2 = new SaleLine(
                _saleLineIdDouble,
                mock(UserId.class),
                mock(Price.class),
                mock(DirectSaleId.class)
        );

        //Act & Assert
        assertEquals(saleLine1, saleLine2);
    }

    @Test
    void shouldReturnFalseWhenSaleLineIdIsDifferent() {
        //Arrange
        SaleLine saleLine1 = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        SaleLine saleLine2 = new SaleLine(
                mock(SaleLineId.class),
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        assertNotEquals(saleLine1, saleLine2);
    }

    @Test
    void equalsShouldReturnFalseWhenObjectIsNotSaleLine() {
        //Arrange
        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        //Act & Assert
        assertNotEquals(saleLine, "not a sale line");
    }

    @Test
    void shouldReturnSameHashCodeWhenSaleLineIdIsEqual() {
        //Arrange
        SaleLine saleLine1 = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        SaleLine saleLine2 = new SaleLine(
                _saleLineIdDouble,
                mock(UserId.class),
                mock(Price.class),
                mock(DirectSaleId.class)
        );

        //Act & Assert
        assertEquals(saleLine1.hashCode(), saleLine2.hashCode());
    }

    @Test
    void shouldReturnExpectedString() {
        //Arrange
        SaleLine saleLine = new SaleLine(
                _saleLineIdDouble,
                _sellerIdDouble,
                _priceAtSaleDouble,
                _directSaleIdDouble
        );

        String expected = "\nSale Line Id: " + _saleLineIdDouble +
                "\nDirect Sale Id: " + _directSaleIdDouble +
                "\nSeller Id: " + _sellerIdDouble +
                "\nPrice At Sale: " + _priceAtSaleDouble;

        //Act & Assert
        assertEquals(expected, saleLine.toString());
    }
}