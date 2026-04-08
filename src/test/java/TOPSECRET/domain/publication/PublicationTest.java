package TOPSECRET.domain.publication;

import TOPSECRET.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PublicationTest {

    private Title _titleDouble;
    private AuthorId _authorIdDouble;
    private Year _yearDouble;
    private PublicationTypeId _publicationTypeIdDouble;
    private GenreId _genreIdDouble;

    @BeforeEach
    void setUp() {

        _titleDouble = mock(Title.class);
        _authorIdDouble = mock(AuthorId.class);
        _yearDouble = mock(Year.class);
        _publicationTypeIdDouble = mock(PublicationTypeId.class);
        _genreIdDouble = mock(GenreId.class);
    }

    @Test
    void constructorAllFieldsCreatesPublication() {
        // Act & SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Assert
        assertNotNull(p);
    }

    @Test
    void constructorNullYearThrowsNullPointerException() {
        // SUT + Assert
        assertThrows(NullPointerException.class, () ->
                new Publication(_titleDouble, _authorIdDouble, null,
                        _publicationTypeIdDouble, _genreIdDouble));
    }

    @Test
    void constructionCreationGeneratesNonNullPublicationId() {
        // Act
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble); // SUT

        // Assert
        assertNotNull(p.getPublicationId());
    }

    @Test
    void constructorCreationSameFieldsGenerateEqualIds() {
        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);
        Publication p2 = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Assert
        assertEquals(p1.getPublicationId(), p2.getPublicationId());
    }

    @Test
    void constructionReconstitutionAllFieldsCreatesPublication() {
        // Arrange
        PublicationId publicationIdDouble = mock(PublicationId.class);

        // Act
        Publication p = new Publication(publicationIdDouble, _titleDouble, _authorIdDouble,
                _yearDouble, _publicationTypeIdDouble, _genreIdDouble); // SUT

        // Assert
        assertNotNull(p);
    }

    @Test
    void constructionReconstitutionNullPublicationIdThrowsNullPointerException() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new Publication(null, _titleDouble, _authorIdDouble,
                        _yearDouble, _publicationTypeIdDouble, _genreIdDouble)); // SUT
    }

    @Test
    void constructionReconstitutionNullYearThrowsNullPointerException() {
        // Arrange
        PublicationId publicationIdDouble = mock(PublicationId.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new Publication(publicationIdDouble, _titleDouble, _authorIdDouble,
                        null, _publicationTypeIdDouble, _genreIdDouble)); // SUT
    }

    @Test
    void constructionReconstitutionRestoresPublicationId() {
        // Arrange
        PublicationId publicationIdDouble = mock(PublicationId.class);

        // Act
        Publication p = new Publication(publicationIdDouble, _titleDouble, _authorIdDouble,
                _yearDouble, _publicationTypeIdDouble, _genreIdDouble); // SUT

        // Assert
        assertSame(publicationIdDouble, p.getPublicationId());
    }

    @Test
    void identityReturnsNonNullPublicationId() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        PublicationId id = p.identity();

        // Assert
        assertNotNull(id);
    }

    @Test
    void equalsSameFieldsReturnsTrue() {

        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);
        Publication p2 = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Assert
        assertEquals(p1, p2);
    }

    @Test
    void equalsDifferentTitleReturnsFalse() {
        // Arrange
        Title _otherTitleDouble = mock(Title.class);
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        Publication p2 = new Publication(_otherTitleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble); // SUT

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsDifferentAuthorReturnsFalse() {
        // Arrange
        AuthorId _otherAuthorIdDouble = mock(AuthorId.class);
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        Publication p2 = new Publication(_titleDouble, _otherAuthorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble); // SUT

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsDifferentYearReturnsFalse() {
        // Arrange
        Year _otherYearDouble = mock(Year.class);
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        Publication p2 = new Publication(_titleDouble, _authorIdDouble, _otherYearDouble,
                _publicationTypeIdDouble, _genreIdDouble); // SUT

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void sameAsSameInstanceReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.sameAs(p);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsDifferentPublicationIdReturnsFalse() {
        // Arrange
        Title _otherTitleDouble = mock(Title.class);

        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);
        Publication p2 = new Publication(_otherTitleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Assert
        assertFalse(p1.sameAs(p2));
    }

    @Test
    void sameAsNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.sameAs("not a publication");

        // Assert
        assertFalse(result);
    }

    @Test
    void isByAuthorSameAuthorReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.isByAuthor(_authorIdDouble);

        // Assert
        assertTrue(result);
    }
    @Test
    void isByAuthorDifferentAuthorReturnsFalse() {
        // Arrange
        AuthorId _otherAuthorIdDouble = mock(AuthorId.class);

        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.isByAuthor(_otherAuthorIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByAuthorNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.isByAuthor(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByGenreSameGenreReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.isByGenre(_genreIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void isByGenreDifferentGenreReturnsFalse() {
        // Arrange
        GenreId _otherGenreIdDouble = mock(GenreId.class);

        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.isByGenre(_otherGenreIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByGenreNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble,
                _publicationTypeIdDouble, _genreIdDouble);

        // Act
        boolean result = p.isByGenre(null); // SUT

        // Assert
        assertFalse(result);
    }

}



