package MITELOVERS.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class SaleIdTest {

    @Test
    void shouldCreateSaleIdWithValidGeneratedFormat() {

        //Act
        SaleId saleId = new SaleId();

        //Assert
        assertNotNull(saleId);
        assertTrue(saleId.toString().matches("SA-[A-Z0-9]{8}"));
    }

    @Test
    void shouldCreateSaleIdFromValidString() {

        //Act
        SaleId saleId = new SaleId("SA-ABC12345");

        //Assert
        assertEquals("SA-ABC12345", saleId.toString());
    }

    @Test
    void shouldThrowExceptionWhenSaleIdIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new SaleId(null)
        );

        assertEquals("SaleId cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSaleIdHasInvalidPrefix() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SaleId("SC-ABC12345")
        );
    }

    @Test
    void shouldThrowExceptionWhenSaleIdHasLessThanEightCharactersAfterPrefix() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SaleId("SA-ABC1234")
        );
    }

    @Test
    void shouldThrowExceptionWhenSaleIdHasMoreThanEightCharactersAfterPrefix() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SaleId("SA-ABC123456")
        );
    }

    @Test
    void shouldThrowExceptionWhenSaleIdContainsLowercaseLetters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SaleId("SA-abc12345")
        );
    }

    @Test
    void shouldThrowExceptionWhenSaleIdContainsSpecialCharacters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new SaleId("SA-ABC12@45")
        );
    }

    @Test
    void shouldBeEqualWhenSaleIdsHaveSameValue() {

        SaleId saleId1 = new SaleId("SA-ABC12345");
        SaleId saleId2 = new SaleId("SA-ABC12345");

        //Assert
        assertEquals(saleId1, saleId2);
    }

    @Test
    void shouldHaveSameHashCodeWhenSaleIdsHaveSameValue() {
        SaleId saleId1 = new SaleId("SA-ABC12345");
        SaleId saleId2 = new SaleId("SA-ABC12345");

        //Assert
        assertEquals(saleId1.hashCode(), saleId2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenSaleIdsHaveDifferentValues() {
        SaleId saleId1 = new SaleId("SA-ABC12345");
        SaleId saleId2 = new SaleId("SA-XYZ98765");

        //Assert
        assertNotEquals(saleId1, saleId2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        SaleId saleId = new SaleId("SA-ABC12345");

        //Assert
        assertNotEquals(null, saleId);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        //Arrange
        Object other = "SA-ABC12345";

        //Act
        SaleId saleId = new SaleId("SA-ABC12345");

        //Assert
        assertFalse(saleId.equals(other));
    }

    @Test
    void shouldBeEqualToItself() {
        SaleId saleId = new SaleId("SA-ABC12345");

        //Assert
        assertEquals(saleId, saleId);
    }

    @Test
    void shouldReturnFalseWhenComparedWithObjectOfDifferentType() {
        SaleId saleId = new SaleId("SA-ABC12345");

        Object other = new Object();

        assertNotEquals(saleId,other);
    }

}