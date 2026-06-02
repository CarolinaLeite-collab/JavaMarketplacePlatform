package MITELOVERS.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationIdTest {

    @Test
    void createsPublicationId() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId publicationId = new PublicationId(titleDouble, authorIdDouble, releaseYear);

    }

    @Test
    void constructorNullTitleThrowsException() {

        //Arrange
        AuthorId authorId = mock(AuthorId.class);

        //SUT & Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationId(null, authorId, Year.of(2020)));

    }

    @Test
    void constructorNullAuthorIdThrowsException() {

        //Arrange
        Title title = mock(Title.class);

        //SUT & Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationId(title, null, Year.of(2020)));

    }

    @Test
    void constructorNullReleaseYearThrowsException() {

        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        // SUT + Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationId(titleDouble, authorIdDouble, null));

    }

    @Test
    void equalsSameReturnsTrue() {

        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        when(titleDouble.toString()).thenReturn("Harry Potter");
        when(authorIdDouble.toString()).thenReturn("Rowling JK-A1B2C3");

        // SUT
        PublicationId id1 = new PublicationId(titleDouble, authorIdDouble, releaseYear);
        PublicationId id2 = new PublicationId(titleDouble, authorIdDouble, releaseYear);

        // Assert
        assertEquals(id1, id2);

    }

    @Test
    void equalsDifferentTitleReturnsFalse() {

        // Arrange
        Title title1Double = mock(Title.class);
        Title title2Double = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId id1 = new PublicationId(title1Double, authorIdDouble, releaseYear);
        PublicationId id2 = new PublicationId(title2Double, authorIdDouble, releaseYear);

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsDifferentAuthorIdReturnsFalse() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorId1Double = mock(AuthorId.class);
        AuthorId authorId2Double = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId id1 = new PublicationId(titleDouble, authorId1Double, releaseYear);
        PublicationId id2 = new PublicationId(titleDouble, authorId2Double, releaseYear);

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsDifferentYearReturnsFalse() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        // SUT
        PublicationId id1 = new PublicationId(titleDouble, authorIdDouble, Year.of(2020));
        PublicationId id2 = new PublicationId(titleDouble, authorIdDouble, Year.of(2021));

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        PublicationId id = new PublicationId(titleDouble, authorIdDouble, Year.of(2020));

        // SUT
        boolean result = id.equals(id);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);

        // SUT
        PublicationId id = new PublicationId(titleDouble, authorIdDouble, Year.of(2020));

        // Act
        boolean result = id.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameArgumentsReturnsSameHash() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId id1 = new PublicationId(titleDouble, authorIdDouble, releaseYear);
        PublicationId id2 = new PublicationId(titleDouble, authorIdDouble, releaseYear);

        // Act & Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsSameStringReturnsTrue() {
        // Arrange
        PublicationId id1 = new PublicationId("Harry Potter - Rowling JK-A1B2C3 (1997)");
        PublicationId id2 = new PublicationId("Harry Potter - Rowling JK-A1B2C3 (1997)");

        // Act
        boolean result = id1.equals(id2);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsDifferentStringReturnsFalse() {
        // Arrange
        PublicationId id1 = new PublicationId("Harry Potter - Rowling JK-A1B2C3 (1997)");
        PublicationId id2 = new PublicationId("The Hobbit - Tolkien JRR-B2C3D4 (1937)");

        // Act
        boolean result = id1.equals(id2);

        // Assert
        assertFalse(result);
    }

    @Test
    void bothConstructorsProduceSameId() {
        // Arrange
        Title title = mock(Title.class);
        AuthorId authorId = mock(AuthorId.class);
        when(title.toString()).thenReturn("Harry-Potter");
        when(authorId.toString()).thenReturn("Rowling-JK-A1B2C3");

        // Act
        PublicationId fromComponents = new PublicationId(title, authorId, Year.of(1997));
        PublicationId fromString = new PublicationId("Harry-Potter-Rowling-JK-A1B2C3(1997)");

        // Assert
        assertEquals(fromComponents, fromString);
    }

    @Test
    void toStringReturnsPublicationId() {
        // Arrange
        PublicationId id = new PublicationId("Harry-Potter-Rowling.JK-A1B2C3(1997)");

        // Act
        String result = id.toString();

        // Assert
        assertEquals("Harry-Potter-Rowling.JK-A1B2C3(1997)", result);
    }
}
