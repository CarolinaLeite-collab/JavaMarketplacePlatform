package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleIdTest {

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

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSaleId("invalid"));

        // Assert
        assertEquals("Invalid DirectSaleId format: invalid", ex.getMessage());
    }

    @Test
    void shouldThrowWhenIdNullString() {

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSaleId(null));

        // Assert
        assertEquals("DirectSaleId cannot be null or empty", ex.getMessage());
    }

    @Test
    void shouldThrowWhenIdEmptyString() {

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new DirectSaleId(""));

        // Assert
        assertEquals("DirectSaleId cannot be null or empty", ex.getMessage());
    }
}
