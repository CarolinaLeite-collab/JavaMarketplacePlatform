package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleIdTest {

    @Test
    void testConstructot() {

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

}
