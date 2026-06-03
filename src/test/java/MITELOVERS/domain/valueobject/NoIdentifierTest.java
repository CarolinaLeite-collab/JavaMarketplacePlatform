package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoIdentifierTest {

    @Test
    void constructorShouldCreateNoIdentifierSuccessfully() {
        //Act
        //SUT
        NoIdentifier identifier = new NoIdentifier();
    }

    @Test
    void getIdentifierShouldReturnExpectedValue() {
        //Arrange
        String expected = "no identifier";
        //SUT
        NoIdentifier noIdentifier = new NoIdentifier();

        //Act
        String result = noIdentifier.getIdentifier();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void twoInstancesShouldReturnSameIdentifierValue() {
        //Arrange
        //SUT
        NoIdentifier noIdentifier1 = new NoIdentifier();
        NoIdentifier noIdentifier2 = new NoIdentifier();

        //Act
        Boolean result = noIdentifier1.getIdentifier().equals(noIdentifier2.getIdentifier());

        //Assert
        assertTrue(result);
    }

    @Test
    void toStringShouldReturnNoIdentifierValue() {
        // Arrange
        String expected = "no identifier";

        // SUT
        NoIdentifier noIdentifier = new NoIdentifier();

        // Act
        String result = noIdentifier.toString();

        // Assert
        assertEquals(expected, result);
    }
}