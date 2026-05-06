package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleIdTest {

    String exceptionMessageDirectSaleIdNull = "DirectSaleId cannot be null or blank";
    String exceptionMessageDirectSaleIdInvalidFormat = "Invalid DirectSaleId format: ";

    @Test
    void testConstructor() {

        //Act
        DirectSaleId id = new DirectSaleId(); //SUT

    }
    @Test
    void shouldReturnNonNullId() {
        //act
        DirectSaleId id = new DirectSaleId(); //SUT

        //assert
        assertNotNull(id);
    }

    @Test
    void shouldCreateDifferentIdsEachTime() {
        //act
        DirectSaleId id1 = new DirectSaleId(); //SUT
        DirectSaleId id2 = new DirectSaleId(); //SUT

        // assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsAndHashCodeShouldWorkForSameUnderlyingValue() {
        //act
        DirectSaleId id1 = new DirectSaleId(); //SUT
        DirectSaleId id2 = new DirectSaleId(); //SUT

        //assert
        assertFalse(id1.equals(id2));
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentTypeOrNull() {
        //act
        DirectSaleId id = new DirectSaleId(); //SUT

        //assert
        assertFalse(id.equals(null));
        assertFalse((id.toString()).equals("DS-ABC12345"));
    }

    @Test
    void shouldCreateIdFromValidString() {

        //Act
        DirectSaleId id = new DirectSaleId("DS-ABCDEF12"); //SUT

        //Assert
        assertEquals("DS-ABCDEF12", id.toString());
    }

    @Test
    void shouldThrowWhenInvalidString() {

        //Act
        //SUT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSaleId("invalid"));
        //Assert
        assertEquals(exceptionMessageDirectSaleIdInvalidFormat + "invalid", ex.getMessage());
    }

    @Test
    void shouldThrowWhenIdNullString() {

        //Act
        //SUT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSaleId(null));
        //Assert
        assertEquals(exceptionMessageDirectSaleIdNull, ex.getMessage());
    }

    @Test
    void shouldThrowWhenIdEmptyString() {

        //Act
        //SUT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSaleId(""));
        //Assert
        assertEquals(exceptionMessageDirectSaleIdNull, ex.getMessage());
    }
}
