package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaleLineIdTest {

    private static final String NullMessage = "SaleLineId cannot be null";
    private static final String InvalidFormatMessage = "Invalid SaleLineId format";


    @Test
    void shouldCreateSaleLineIdWithValidGeneratedFormat() {

        //Act
        SaleLineId saleLineId = new SaleLineId();

        //Assert
        assertTrue(saleLineId.toString().matches("SL-[A-Z0-9]{8}"));
    }

    @Test
    void shouldCreateSaleLineIdFromValidString() {

        //Act
        SaleLineId saleLineId = new SaleLineId("SL-ABC12345");

        //Assert
        assertEquals("SL-ABC12345", saleLineId.toString());
    }

    @Test
    void shouldThrowExceptionWhenSaleLineIdIsNull() {

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new SaleLineId(null)
        );

        //Assert
        assertEquals(NullMessage, exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenSaleLineIdsHaveSameValue() {

        //Arrange
        SaleLineId saleLineId1 = new SaleLineId("SL-ABC12345");
        SaleLineId saleLineId2 = new SaleLineId("SL-ABC12345");

        //Assert
        assertEquals(saleLineId1, saleLineId2);
    }

    @Test
    void shouldHaveSameHashCodeWhenSaleLineIdsHaveSameValue() {

        //Arrange
        SaleLineId saleLineId1 = new SaleLineId("SL-ABC12345");
        SaleLineId saleLineId2 = new SaleLineId("SL-ABC12345");

        //Assert
        assertEquals(saleLineId1.hashCode(), saleLineId2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenSaleLineIdsHaveDifferentValues() {

        //Arrange
        SaleLineId saleLineId1 = new SaleLineId("SL-ABC12345");
        SaleLineId saleLineId2 = new SaleLineId("SL-XYZ98765");

        //Assert
        assertNotEquals(saleLineId1, saleLineId2);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {

        //Arrange
        Object other = "SL-ABC12345";

        //Act
        SaleLineId saleLineId = new SaleLineId("SL-ABC12345");

        //Assert
        assertFalse(saleLineId.equals(other));
    }

    @Test
    void shouldThrowExceptionWhenSaleLineIdHasInvalidFormat() {

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SaleLineId("SA-ABC12345")
        );

        //Assert
        assertEquals(InvalidFormatMessage, exception.getMessage());
    }

    @Test
    void shouldBeEqualToItself() {

        //Arrange
        SaleLineId saleLineId = new SaleLineId("SL-ABC12345");

        //Act
        boolean result = saleLineId.equals(saleLineId);

        //Assert
        assertTrue(result);
    }
}