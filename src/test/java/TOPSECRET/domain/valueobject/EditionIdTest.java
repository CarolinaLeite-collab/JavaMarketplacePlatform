package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class EditionIdTest {

    @Test
    void testConstructorCreatesInstance() {
        //Act
        //SUT
        EditionId editionId = new EditionId();

        //Assert
        assertNotNull(editionId);
    }

    @Test
    void editionIdIsEqualToItself() {
        //Act
        //SUT
        EditionId editionId = new EditionId();

        //Assert
        assertEquals(editionId, editionId);
    }

    @Test
    void editionIdIsNotEqualToNull() {
        //Act
        //SUT
        EditionId editionId = new EditionId();

        //Assert
        assertNotEquals(null, editionId);
    }

    @Test
    void editionIdIsNotEqualToDifferentObjectType() {

        //Arrange
        String differentType = "test";

        //SUT
        EditionId editionId = new EditionId();

        //Assert
        assertFalse(editionId.equals(differentType));
    }

    @Test
    void editionIdIsNotEqualToAnotherEditionId() {
        //Act
        //SUT
        EditionId editionId1 = new EditionId();
        EditionId editionId2 = new EditionId();

        //Assert
        assertNotEquals(editionId1, editionId2);
    }

    @Test
    void equalsReturnsFalseForDifferentInstances() {
        //Act
        //SUT
        EditionId editionId1 = new EditionId();
        EditionId editionId2 = new EditionId();

        //Assert
        assertFalse(editionId1.equals(editionId2));
    }

    @Test
    void equalsShouldReturnTrueForSameReference() {
        //Act
        //SUT
        EditionId editionId = new EditionId();

        //Assert
        assertEquals(editionId, editionId);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        //Act
        //SUT
        EditionId editionId = new EditionId();

        //Assert
        assertFalse(editionId.equals(null));
    }

    @Test
    void equalsShouldReturnTrueForSameReferenceId() {
        //Arrange
        //SUT
        EditionId id = new EditionId();

        //Assert
        assertEquals(id, id);
    }
}