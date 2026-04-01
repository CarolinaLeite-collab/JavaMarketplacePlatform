package TOPSECRET.domain;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.PublicationId;
import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PublicationTest {

    private Title _titleDouble;
    private Author _authorDouble;
    private Year _yearDouble;
    private PublicationType _publicationTypeDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {

        _titleDouble = mock(Title.class);
        _authorDouble = mock(Author.class);
        _yearDouble = mock(Year.class);
        _publicationTypeDouble = mock(PublicationType.class);
        _genreDouble = mock(Genre.class);
    }

    @Test
    void constructorAllFieldsCreatesPublication() {
        // Act & SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Assert
        assertNotNull(p);
    }

    @Test
    void constructorNullYearThrowsNullPointerException() {
        // SUT + Assert
        assertThrows(NullPointerException.class, () ->
                new Publication(_titleDouble, _authorDouble, null,
                        _publicationTypeDouble, _genreDouble));
    }

    @Test
    void constructionCreationGeneratesNonNullPublicationId() {
        // Act
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble); // SUT

        // Assert
        assertNotNull(p.getPublicationId());
    }

    @Test
    void constructionCreationTwoInstancesGenerateDifferentIds() {
        // Arrange
        Publication p1 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        Publication p2 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble); // SUT

        // Assert
        assertNotEquals(p1.getPublicationId(), p2.getPublicationId());
    }

    @Test
    void constructionReconstitutionAllFieldsCreatesPublication() {
        // Arrange
        PublicationId publicationIdDouble = mock(PublicationId.class);

        // Act
        Publication p = new Publication(publicationIdDouble, _titleDouble, _authorDouble,
                _yearDouble, _publicationTypeDouble, _genreDouble); // SUT

        // Assert
        assertNotNull(p);
    }

    @Test
    void constructionReconstitutionNullPublicationIdThrowsNullPointerException() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new Publication(null, _titleDouble, _authorDouble,
                        _yearDouble, _publicationTypeDouble, _genreDouble)); // SUT
    }

    @Test
    void constructionReconstitutionNullYearThrowsNullPointerException() {
        // Arrange
        PublicationId publicationIdDouble = mock(PublicationId.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new Publication(publicationIdDouble, _titleDouble, _authorDouble,
                        null, _publicationTypeDouble, _genreDouble)); // SUT
    }

    @Test
    void constructionReconstitutionRestoresPublicationId() {
        // Arrange
        PublicationId publicationIdDouble = mock(PublicationId.class);

        // Act
        Publication p = new Publication(publicationIdDouble, _titleDouble, _authorDouble,
                _yearDouble, _publicationTypeDouble, _genreDouble); // SUT

        // Assert
        assertSame(publicationIdDouble, p.getPublicationId());
    }

    @Test
    void identityReturnsNonNullPublicationId() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        PublicationId id = p.identity();

        // Assert
        assertNotNull(id);
    }

    @Test
    void equalsSameFieldsReturnsTrue() {

        // SUT
        Publication p1 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);
        Publication p2 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Assert
        assertEquals(p1, p2);
    }

    @Test
    void equalsDifferentTitleReturnsFalse() {
        // Arrange
        Title _otherTitleDouble = mock(Title.class);
        Publication p1 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        Publication p2 = new Publication(_otherTitleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble); // SUT

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsDifferentAuthorReturnsFalse() {
        // Arrange
        Author _otherAuthorDouble = mock(Author.class);
        Publication p1 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        Publication p2 = new Publication(_titleDouble, _otherAuthorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble); // SUT

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsDifferentYearReturnsFalse() {
        // Arrange
        Year _otherYearDouble = mock(Year.class);
        Publication p1 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        Publication p2 = new Publication(_titleDouble, _authorDouble, _otherYearDouble,
                _publicationTypeDouble, _genreDouble); // SUT

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void sameAsSameInstanceReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.sameAs(p);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsDifferentInstanceReturnsFalse() {
        // SUT
        Publication p1 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);
        Publication p2 = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p1.sameAs(p2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.sameAs("not a publication");

        // Assert
        assertFalse(result);
    }

    @Test
    void isByAuthorSameAuthorReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.isByAuthor(_authorDouble);

        // Assert
        assertTrue(result);
    }
    @Test
    void isByAuthorDifferentAuthorReturnsFalse() {
        // Arrange
        Author _otherAuthorDouble = mock(Author.class);

        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.isByAuthor(_otherAuthorDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByAuthorNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.isByAuthor(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByGenreSameGenreReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.isByGenre(_genreDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void isByGenreDifferentGenre_ReturnsFalse() {
        // Arrange
        Genre _otherGenreDouble = mock(Genre.class);

        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.isByGenre(_otherGenreDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByGenreNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorDouble, _yearDouble,
                _publicationTypeDouble, _genreDouble);

        // Act
        boolean result = p.isByGenre(null); // SUT

        // Assert
        assertFalse(result);
    }

}



