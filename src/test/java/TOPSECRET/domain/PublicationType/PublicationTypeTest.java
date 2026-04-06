package TOPSECRET.domain.PublicationType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeTest {

    private PublicationType _publicationType1;
    private PublicationType _publicationType2;
    private PublicationType _publicationType3;

    @BeforeEach
    void setUp() {

        _publicationType1 = new PublicationType("BOOK");
        _publicationType2 = new PublicationType("book");
        _publicationType3 = new PublicationType("MAGAZINE");

    }

    @Test
    void constructorShouldBuildPublicationType() {

        //Arrange and Act
        new PublicationType("Pokemon Card");

    }

    @Test
    void givenValidTypeNameShouldSavesData() {

        //Act + Assert
        assertEquals("BOOK", _publicationType1.getPublicationType());

    }

    @Test
    void givenNullTypeNameShouldThrowIllegalArgumentException() {
        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType(null));

        //Assert
        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void givenBlankTypeNameShouldThrowIllegalArgumentException() {
        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType("   "));

        //Assert
        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void givenEmptyTypeNameShouldThrowIllegalArgumentException() {
        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType(""));

        //Assert
        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void isSamePublicationTypeShouldReturnTrueIfSame() {

        //Arrange
        String book = "book";

        //Act
        boolean result = _publicationType1.isSamePublicationType(book);

        //Assert
        assertTrue(result);

    }

    @Test
    void isSamePublicationTypeShouldReturnFalseIfNotSame() {

        //Arrange
        String book = "pokemon card";

        //Act
        boolean result = _publicationType1.isSamePublicationType(book);

        //Assert
        assertFalse(result);

    }

    @Test
    void isSamePublicationTypeShouldReturnFalseIfNull() {

        //Arrange
        String book = null;

        //Act
        boolean result = _publicationType1.isSamePublicationType(book);

        //Assert
        assertFalse(result);

    }

    @Test
    void isSamePublicationTypeShouldReturnFalseIfBlank() {

        //Arrange
        String book = "   ";

        //Act
        boolean result = _publicationType1.isSamePublicationType(book);

        //Assert
        assertFalse(result);

    }

    @Test
    void isSamePublicationTypeShouldReturnFalseIfEmpty() {

        //Arrange
        String book = "";

        //Act
        boolean result = _publicationType1.isSamePublicationType(book);

        //Assert
        assertFalse(result);

    }

    @Test
    void toStringReturnsTypeName() {

        //Act + Assert
        assertEquals("BOOK", _publicationType2.toString());

    }

    @Test
    void sameObjectShouldAssertEquals() {

        //Assert
        assertEquals(_publicationType1, _publicationType1);

    }

    @Test
    void differentObjectsWithSameNameShouldReturnEquals() {

        //Assert
        assertEquals(_publicationType1, _publicationType2);

    }

    @Test
    void differentObjectsWithDifferentNamesShouldAssertNotEquals() {

        //Assert
        assertNotEquals(_publicationType1, _publicationType3);

    }

    @Test
    void objectsWithNullObjectShouldReturnNotEquals() {

        assertNotEquals(null, _publicationType1);

    }

    @Test
    void objectsWithDifferentTypesShouldReturnNotEquals() {

        //Arrange
        String otherType = "publicationType";

        //Assert
        assertFalse(_publicationType1.equals(otherType));

    }


    @Test
    void sameNamesShouldHaveSameHashCode() {

        //Act + Assert
        assertEquals(_publicationType1.hashCode(), _publicationType2.hashCode());

    }

    @Test
    void differentNamesShouldHaveDifferentHashCode() {

        //Act + Assert
        assertNotEquals(_publicationType1.hashCode(), _publicationType3.hashCode());

    }

}