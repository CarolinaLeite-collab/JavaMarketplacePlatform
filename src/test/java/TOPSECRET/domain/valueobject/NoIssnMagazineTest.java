package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoIssnMagazineTest {

    @Test
    public void shouldCreateValidInstance() {
        //Arrange
        String id = "123456";

        //Act
        //SUT
        NoIssnMagazine magazineInternalId = new NoIssnMagazine(id);

        //Assert
        assertEquals(id, magazineInternalId.getIdentifier());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new NoIssnMagazine(null));

        // Assert
        assertEquals("Internal id cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenIdIsBlank() {
        //Arrange
        String id = " ";

        //Act
        //SUT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new NoIssnMagazine(id));

        // Assert
        assertEquals("Internal id cannot be null", exception.getMessage());
    }

    @Test
    void shouldGenerateNonNullId() {
        //Act
        //SUT
        NoIssnMagazine generatedId = NoIssnMagazine.generate();

        //Assert
        assertNotNull(generatedId);
    }

    @Test
    void generatedIdsShouldBeDifferent() {
        //Arrange
        //SUT
        NoIssnMagazine generatedId1 = NoIssnMagazine.generate();
        NoIssnMagazine generatedId2 = NoIssnMagazine.generate();

        //Assert
        assertNotEquals(generatedId1,generatedId2);
    }

    @Test
    void shouldBeEqualWhenSameInternalId() {
        //Arrange
        String id = "123456";

        //Act
        //SUT
        NoIssnMagazine magazineInternalId1 = new NoIssnMagazine(id);
        NoIssnMagazine magazineInternalId2 = new NoIssnMagazine(id);

        //Assert
        assertEquals(magazineInternalId1,magazineInternalId2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentInternalId() {
        //Arrange
        String id1 = "123456";
        String id2 = "12345";


        //Act
        //SUT
        NoIssnMagazine magazineInternalId1 = new NoIssnMagazine(id1);
        NoIssnMagazine magazineInternalId2 = new NoIssnMagazine(id2);

        //Assert
        assertNotEquals(magazineInternalId1,magazineInternalId2);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithNull() {
        //Arrange
        String id = "12345";
        NoIssnMagazine magazineInternalId = new NoIssnMagazine(id);

        //Act
        //SUT
        boolean result = magazineInternalId.equals(null);

        //Assert
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        //Arrange
        String id = "12345";
        NoIssnMagazine magazineInternalId = new NoIssnMagazine(id);

        //Act
        //SUT
        boolean result = magazineInternalId.equals("12345");

        //Assert
        assertFalse(result);
    }

    @Test
    void shouldBeEqualToItself() {
        // Arrange
        //SUT
        NoIssnMagazine id = new NoIssnMagazine("12345");

        //Assert
        assertEquals(id, id);
    }

    @Test
    void shouldBeImmutable() {
        ///Arrange
        String id = "123456";

        //Act
        //SUT
        NoIssnMagazine magazineInternalId = new NoIssnMagazine(id);

        //Assert
        assertEquals("123456", magazineInternalId.getIdentifier());
    }

}