package TOPSECRET.domain.publicationtype;

import TOPSECRET.domain.valueobject.PublicationTypeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PublicationTypeTest {

    @Test
    void constructorShouldBuildPublicationTypeFromString() {

        //Arrange and Act
        new PublicationType("Book");

    }

    @Test
    void constructorShouldBuildPublicationTypeFromId() {

        //Arrange
        PublicationTypeId idDouble = mock(PublicationTypeId.class);

        //Act
        new PublicationType(idDouble);

    }

    @Test
    void constructorWithNullPublicationTypeIdShouldThrowException() {

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new PublicationType((PublicationTypeId) null));
    }

    @Test
    void constructorWithNullPublicationTypeIdExpectedMessage() {

        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new PublicationType((PublicationTypeId) null));

        //Assert
        assertEquals("PublicationTypeId is required.", exception.getMessage());

    }

    @Test
    void identityShouldReturnUnderlyingId() {

        //Arrange
        PublicationTypeId idDouble = mock(PublicationTypeId.class);
        PublicationType publicationType = new PublicationType(idDouble);

        // Act
        PublicationTypeId pubTypeId = publicationType.identity();

        //Assert
        assertSame(idDouble, pubTypeId);

    }

    @Test
    void sameAsShouldReturnTrueForEqualPublicationType() {

        //Arrange
        PublicationType pubType1 = new PublicationType("Magazine");
        PublicationType pubType2 = new PublicationType("MAGAZINE");

        //Act + Assert
        assertTrue(pubType1.sameAs(pubType2));
    }

    @Test
    void sameAsShouldReturnFalseForDifferentPublicationType() {

        //Arrange
        PublicationType pubType1 = new PublicationType("Pokemon Card");
        PublicationType pubType2 = new PublicationType("MAGAZINE");

        //Act + Assert
        assertFalse(pubType1.sameAs(pubType2));
    }

    @Test
    void samePublicationTypesAreEqual() {

        //Arrange
        PublicationType pubType1 = new PublicationType("Book");
        PublicationType pubType2 = new PublicationType("book");

        //Assert
        assertEquals(pubType1, pubType2);

    }

    @Test
    void differentPublicationTypesAreNotEqual() {

        //Arrange
        PublicationType pubType1 = new PublicationType("book");
        PublicationType pubType2 = new PublicationType("magazine");

        //Assert
        assertNotEquals(pubType1, pubType2);

    }

    @Test
    void toStringReturnsTypeName() {

        //Arrange
        PublicationType pubType = new PublicationType("book");

        //Act + Assert
        assertEquals("BOOK", pubType.toString());

    }

    @Test
    void sameObjectShouldAssertEquals() {

        //Arrange
        PublicationType pubType1 = new PublicationType("Pokemon Card");

        //Assert
        assertEquals(pubType1, pubType1);

    }

    @Test
    void objectsWithNullObjectShouldReturnNotEquals() {

        //Arrange
        PublicationType pubType1 = new PublicationType("book");

        //Assert
        assertNotEquals(null, pubType1);

    }

    @Test
    void objectsWithDifferentTypesShouldReturnNotEquals() {

        //Arrange
        PublicationType pubType1 = new PublicationType("book");
        String otherType = "book";

        //Assert
        assertFalse(pubType1.equals(otherType));

    }


    @Test
    void sameNamesShouldHaveSameHashCode() {

        //Arrange
        PublicationType pubType1 = new PublicationType("book");
        PublicationType pubType2 = new PublicationType("BOOK");

        //Act + Assert
        assertEquals(pubType1.hashCode(), pubType2.hashCode());

    }

    @Test
    void differentNamesShouldHaveDifferentHashCode() {

        //Arrange
        PublicationType pubType1 = new PublicationType("magazine");
        PublicationType pubType2 = new PublicationType("BOOK");

        //Act + Assert
        assertNotEquals(pubType1.hashCode(), pubType2.hashCode());

    }

}