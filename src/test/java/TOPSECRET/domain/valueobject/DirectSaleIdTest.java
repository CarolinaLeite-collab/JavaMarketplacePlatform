package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleIdTest {

    @Test
    void testConstructot() {

        //Act
        DirectSaleId id = new DirectSaleId();

    }
    @Test
    void shouldReturnNonNullId() {
        //act
        DirectSaleId id = new DirectSaleId();

        //assert
        assertNotNull(id);
    }

    @Test
    void shouldCreateDifferentIdsEachTime() {
        //act
        DirectSaleId id1 = new DirectSaleId();
        DirectSaleId id2 = new DirectSaleId();

        // assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsAndHashCodeShouldWorkForSameUnderlyingValue() {
        //act
        DirectSaleId id1 = new DirectSaleId();
        DirectSaleId id2 = new DirectSaleId();

        //assert
        assertNotEquals(id1, id2);
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentTypeOrNull() {
        //act
        DirectSaleId id = new DirectSaleId();

        //assert
        assertNotEquals(id, null);
        assertNotEquals(id, "DS-ABC12345");
    }
}