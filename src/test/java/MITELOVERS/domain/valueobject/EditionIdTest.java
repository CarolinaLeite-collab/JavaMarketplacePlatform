package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditionIdTest {

    @Test
    void testConstructorCreatesInstance() {
        //Act + SUT
        EditionId editionId = new EditionId();

        //Assert
        assertNotNull(editionId);
    }

    @Test
    void shouldCreateEditionIdWithValidId() {

        // Arrange
        String id = "edition-123";

        // Act
        // SUT
        EditionId editionId = new EditionId(id);

        // Assert
        assertEquals(id, editionId.toString());
    }

    @Test
    void constructorNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new EditionId((String)null));
    }

    @Test
    void editionIdIsEqualToItself() {
        //Act + SUT
        EditionId editionId = new EditionId();

        //Assert
        assertEquals(editionId, editionId);
    }

    @Test
    void editionIdIsNotEqualToNull() {
        //Act + SUT
        EditionId editionId = new EditionId();

        //Assert
        assertNotEquals(null, editionId);
    }

    @Test
    void editionIdIsNotEqualToDifferentObjectType() {
        //Arrange
        String differentType = "test";

        //Act + SUT
        EditionId editionId = new EditionId();

        //Assert
        assertFalse(editionId.equals(differentType));
    }

    @Test
    void editionIdIsNotEqualToAnotherEditionIdOfDifferentValues() {
        //Act + SUT
        EditionId editionId1 = new EditionId();
        EditionId editionId2 = new EditionId();

        //Assert
        assertNotEquals(editionId1, editionId2);
    }

    @Test
    void equalsReturnsFalseForDifferentInstancesOfDifferentValues() {
        //Act + SUT
        EditionId editionId1 = new EditionId();
        EditionId editionId2 = new EditionId();

        //Assert
        assertFalse(editionId1.equals(editionId2));
    }

    @Test
    void equalsShouldReturnTrueForSameReference() {
        //Act + SUT
        EditionId editionId = new EditionId();

        //Assert
        assertEquals(editionId, editionId);
    }

    @Test
    void equalsReturnsTrueForEditionIdsWithSameStringValue() {
        // SUT
        EditionId editionId1 = new EditionId("E-TEST1234");
        EditionId editionId2 = new EditionId("E-TEST1234");

        // Act
        boolean result = editionId1.equals(editionId2);

        // Assert
        assertTrue(result);
        assertEquals(editionId1, editionId2);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        //Act + SUT
        EditionId editionId = new EditionId();

        //Assert
        assertFalse(editionId.equals(null));
    }

    @Test
    void getValueReturnsUnderlyingValue() {
        // SUT
        EditionId editionId = new EditionId("E-TEST1234");

        // Act
        String result = editionId.getValue();

        // Assert
        assertEquals("E-TEST1234", result);
    }

    @Test
    void toStringReturnsUnderlyingValue() {
        // SUT
        EditionId editionId = new EditionId("E-TEST1234");

        // Act
        String result = editionId.toString();

        // Assert
        assertEquals("E-TEST1234", result);
    }

    @Test
    void hashCodeIsConsistentForSameInstance() {
        // SUT
        EditionId editionId = new EditionId("E-TEST1234");

        // Act
        int firstHash = editionId.hashCode();
        int secondHash = editionId.hashCode();

        // Assert
        assertEquals(firstHash, secondHash);
    }

    @Test
    void hashCodeIsEqualForEqualEditionIds() {
        // SUT
        EditionId editionId1 = new EditionId("E-TEST1234");
        EditionId editionId2 = new EditionId("E-TEST1234");

        // Act
        int hash1 = editionId1.hashCode();
        int hash2 = editionId2.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void hashCodeIsNotEqualForDifferentEditionIds() {
        //SUT
        EditionId editionId1 = new EditionId("E-TEST1234");
        EditionId editionId2 = new EditionId("E-TEST5678");

        // Act
        int hash1 = editionId1.hashCode();
        int hash2 = editionId2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);

    }


}
