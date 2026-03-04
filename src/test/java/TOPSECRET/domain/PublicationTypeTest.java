package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeTest {

    private PublicationType _pt1;
    private PublicationType _pt2;
    private PublicationType _pt3;

    @BeforeEach
    void setUp() {

        _pt1 = new PublicationType("BOOK");
        _pt2 = new PublicationType("book");
        _pt3 = new PublicationType("MAGAZINE");

    }

    @Test
    void givenValidTypeName_SavesData() {

        //Assert
        assertEquals("BOOK", _pt1.getPublicationType());

    }

    @Test
    void givenNullTypeName_ThrowsIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType(null));

        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void givenBlankTypeName_ThrowsIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType("   "));

        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void givenEmptyTypeName_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new PublicationType(""));

        assertEquals("Publication type name is required!", exception.getMessage());
    }

    @Test
    void IsSamePublicationTypeShouldReturnTrueIfSame() {

        //Arrange
        String book = "book";

        //Act
        boolean result = _pt1.isSamePublicationType(book);

        //Assert
        assertTrue(result);

    }

    @Test
    void IsSamePublicationTypeShouldReturnFalseIfNotSame() {

        //Arrange
        String book = "pokemon card";

        //Act
        boolean result = _pt1.isSamePublicationType(book);

        //Assert
        assertFalse(result);

    }

    @Test
    void IsSamePublicationTypeShouldReturnFalseIfNull() {

        //Arrange
        String book = null;

        //Act
        boolean result = _pt1.isSamePublicationType(book);

        //Assert
        assertFalse(result);

    }

    @Test
    void IsSamePublicationTypeShouldReturnFalseIfBlank() {

        //Arrange
        String book = "   ";

        //Act
        boolean result = _pt1.isSamePublicationType(book);

        //Assert
        assertFalse(result);

    }

    @Test
    void IsSamePublicationTypeShouldReturnFalseIfEmpty() {

        //Arrange
        String book = "";

        //Act
        boolean result = _pt1.isSamePublicationType(book);

        //Assert
        assertFalse(result);

    }

    @Test
    void toStringReturnsTypeName() {

        //Assert
        assertEquals("BOOK", _pt2.toString());

    }

    @Test
    void SameObjectShouldAssertEquals() {

        //Assert
        assertEquals(_pt1, _pt1);

    }

    @Test
    void DifferentObjectsWithSameNameShouldReturnEquals() {

        //Assert
        assertEquals(_pt1, _pt2);

    }

    @Test
    void DifferentObjectsWithDifferentNamesShouldAssertNotEquals() {

        //Assert
        assertNotEquals(_pt1, _pt3);

    }

    @Test
    void ObjectsWithNullObjectShouldReturnNotEquals() {

        assertNotEquals(null, _pt1);

    }

    @Test
    void ObjectsWithDifferentTypesShouldReturnNotEquals() {

        //Arrange
        String otherType = "publicationType";

        //Assert
        assertNotEquals(otherType, _pt1);

    }


    @Test
    void SameNamesShouldHaveSameHashCode() {

        //Assert
        assertEquals(_pt1.hashCode(), _pt2.hashCode());

    }

    @Test
    void DifferentNamesShouldHaveDifferentHashCode() {

        //Assert
        assertNotEquals(_pt1.hashCode(), _pt3.hashCode());

    }

}