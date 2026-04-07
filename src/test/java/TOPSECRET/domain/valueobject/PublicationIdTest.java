package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.time.Year;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PublicationIdTest {

    @Test
    void createsPublicationId() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId publicationId = new PublicationId(_titleDouble, _authorIdDouble, releaseYear);

        // Assert
        assertNotNull(publicationId);
    }

    @Test
    void constructorNullReleaseYearThrowsException() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);

        // SUT + Assert
        assertThrows(NullPointerException.class, () ->
                new PublicationId(_titleDouble, _authorIdDouble, null));
    }

    @Test
    void equalsSameTitleAuthorYearReturnsTrue() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId id1 = new PublicationId(_titleDouble, _authorIdDouble, releaseYear);
        PublicationId id2 = new PublicationId(_titleDouble, _authorIdDouble, releaseYear);

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void equalsDifferentTitleReturnsFalse() {
        // Arrange
        Title _title1Double = mock(Title.class);
        Title _title2Double = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId id1 = new PublicationId(_title1Double, _authorIdDouble, releaseYear);
        PublicationId id2 = new PublicationId(_title2Double, _authorIdDouble, releaseYear);

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsDifferentAuthorIdReturnsFalse() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorId1Double = mock(AuthorId.class);
        AuthorId _authorId2Double = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId id1 = new PublicationId(_titleDouble, _authorId1Double, releaseYear);
        PublicationId id2 = new PublicationId(_titleDouble, _authorId2Double, releaseYear);

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsDifferentYearReturnsFalse() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);

        // SUT
        PublicationId id1 = new PublicationId(_titleDouble, _authorIdDouble, Year.of(2020));
        PublicationId id2 = new PublicationId(_titleDouble, _authorIdDouble, Year.of(2021));

        // Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        PublicationId id = new PublicationId(_titleDouble, _authorIdDouble, Year.of(2020));

        // SUT
        boolean result = id.equals(id);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsNullReturnsFalse() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        PublicationId id = new PublicationId(_titleDouble, _authorIdDouble, Year.of(2020));

        // SUT
        boolean result = id.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameArgumentsReturnsSameHash() {
        // Arrange
        Title _titleDouble = mock(Title.class);
        AuthorId _authorIdDouble = mock(AuthorId.class);
        Year releaseYear = Year.of(2020);

        // SUT
        PublicationId id1 = new PublicationId(_titleDouble, _authorIdDouble, releaseYear);
        PublicationId id2 = new PublicationId(_titleDouble, _authorIdDouble, releaseYear);

        // Assert
        assertEquals(id1.hashCode(), id2.hashCode());
    }

}