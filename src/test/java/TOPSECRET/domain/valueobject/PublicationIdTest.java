package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.time.Year;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PublicationIdTest {

    @Test
    void createsPublicationId() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId publicationId = new PublicationId(titleDouble, authorIdDouble, releaseYear);

        // Assert
        assertNotNull(publicationId);
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
    void equalsSameTitleAuthorYearReturnsTrue() {
        // Arrange
        Title titleDouble = mock(Title.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

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
        PublicationId id = new PublicationId(titleDouble, authorIdDouble, Year.of(2020));

        // SUT
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

        // Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

}