package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditionIdTest {

    @Test
    void testConstructorCreatesInstance() {

        // SUT
        EditionId editionId = new EditionId();

        // assert
        assertNotNull(editionId);
    }

    @Test
    void editionIdIsEqualToItself() {

        // SUT
        EditionId editionId = new EditionId();

        // assert
        assertEquals(editionId, editionId);
    }

    @Test
    void editionIdIsNotEqualToNull() {

        // SUT
        EditionId editionId = new EditionId();

        // assert
        assertNotEquals(null, editionId);
    }

    @Test
    void editionIdIsNotEqualToDifferentObjectType() {

        // arrange
        String differentType = "test";

        // SUT
        EditionId editionId = new EditionId();

        // assert
        assertFalse(editionId.equals(differentType));
    }

    @Test
    void editionIdIsNotEqualToAnotherEditionId() {

        // SUT
        EditionId editionId1 = new EditionId();
        EditionId editionId2 = new EditionId();

        // assert
        assertNotEquals(editionId1, editionId2);
    }

    @Test
    void equalsReturnsFalseForDifferentInstances() {

        // SUT
        EditionId editionId1 = new EditionId();
        EditionId editionId2 = new EditionId();

        // assert
        assertFalse(editionId1.equals(editionId2));
    }

}