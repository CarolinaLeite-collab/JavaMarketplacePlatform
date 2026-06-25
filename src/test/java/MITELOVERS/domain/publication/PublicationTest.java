package MITELOVERS.domain.publication;

import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationTest {

    private Title _titleDouble;
    private AuthorId _authorIdDouble;
    private Year _yearDouble;
    private GenreId _genreIdDouble;
    private String _synopsis;

    @BeforeEach
    void setUp() {

        _titleDouble = mock(Title.class);
        _authorIdDouble = mock(AuthorId.class);
        _yearDouble = mock(Year.class);
        _genreIdDouble = mock(GenreId.class);
        _synopsis = "Synopsis";
    }

    @Test
    void constructorAllFieldsNoIdCreatesPublication() {
        // Act & SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Assert
        assertNotNull(p);
    }

    @Test
    void constructorAllFieldsWithIdCreatesPublication() {
        //arrange
        PublicationId pubId = mock(PublicationId.class);

        //act+SUT
        Publication p = new Publication(pubId, _titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        //assert
        assertNotNull(p);
    }

    @Test
    void constructorNullYearThrowsNullPointerException() {
        // SUT + Assert
        assertThrows(NullPointerException.class, () ->
                new Publication(_titleDouble, _authorIdDouble, null, _genreIdDouble, _synopsis));
    }

    @Test
    void constructionCreationGeneratesNonNullPublicationId() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Assert
        assertNotNull(p.identity());
    }

    @Test
    void constructorCreationSameFieldsGenerateEqualIds() {
        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);
        Publication p2 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Assert
        assertEquals(p1.identity(), p2.identity());
    }

    @Test
    void gettersReturnCorrectValues() {
        // Arrange
        String synopsis = "synopsis";

        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, synopsis);

        // Assert
        assertSame(_titleDouble, p.getTitle());
        assertSame(_authorIdDouble, p.getAuthorId());
        assertSame(_yearDouble, p.getReleaseYear());
        assertSame(_genreIdDouble, p.getGenreId());
        assertSame(synopsis, p.getSynopsis());
    }

    @Test
    void identityReturnsNonNullPublicationId() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        PublicationId id = p.identity();

        // Assert
        assertNotNull(id);
    }

    @Test
    void equalsSameFieldsReturnsTrue() {
        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);
        Publication p2 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Assert
        assertEquals(p1, p2);
    }

    @Test
    void equalsSameInstanceReturnsTrue() {
        // SUT
        Publication publication = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = publication.equals(publication);

        // Assert
        assertTrue(result);
    }

    @Test
    void equalsDifferentTitleReturnsFalse() {
        // Arrange
        Title otherTitleDouble = mock(Title.class);

        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);
        Publication p2 = new Publication(otherTitleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsDifferentAuthorReturnsFalse() {
        // Arrange
        AuthorId otherAuthorIdDouble = mock(AuthorId.class);

        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);
        Publication p2 = new Publication(_titleDouble, otherAuthorIdDouble, _yearDouble, _genreIdDouble,  _synopsis);

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsDifferentYearReturnsFalse() {
        // Arrange
        Year otherYearDouble = mock(Year.class);
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble,  _synopsis);


        // Act
        Publication p2 = new Publication(_titleDouble, _authorIdDouble, otherYearDouble, _genreIdDouble, _synopsis); // SUT

        // Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equalsDifferentTypeReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.equals("not a publication");

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsSameInstanceReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.sameAs(p);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAsDifferentPublicationIdReturnsFalse() {
        // Arrange
        Title otherTitleDouble = mock(Title.class);

        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);
        Publication p2 = new Publication(otherTitleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Assert
        assertFalse(p1.sameAs(p2));
    }

    @Test
    void sameAsNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsDifferentTypeReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.sameAs("not a publication");

        // Assert
        assertFalse(result);
    }

    @Test
    void isByAuthorSameAuthorReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.isByAuthorId(_authorIdDouble);

        // Assert
        assertTrue(result);
    }
    @Test
    void isByAuthorDifferentAuthorReturnsFalse() {
        // Arrange
        AuthorId otherAuthorIdDouble = mock(AuthorId.class);

        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.isByAuthorId(otherAuthorIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByAuthorNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.isByAuthorId(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByGenreSameGenreReturnsTrue() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.isByGenreId(_genreIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void isByGenreDifferentGenreReturnsFalse() {
        // Arrange
        GenreId otherGenreIdDouble = mock(GenreId.class);

        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.isByGenreId(otherGenreIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByGenreNullReturnsFalse() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        boolean result = p.isByGenreId(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void hashCodeSameFieldsReturnsSameHash() {
        // SUT
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);
        Publication p2 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        int hash1 = p1.hashCode();
        int hash2 = p2.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void hashCodeDifferentFieldsReturnsDifferentHash() {
        // Arrange
        Title otherTitleDouble = mock(Title.class);
        Publication p1 = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);
        Publication p2 = new Publication(otherTitleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // SUT
        int hash1 = p1.hashCode();
        int hash2 = p2.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
    }

    @Test
    void toStringReturnsNonNull() {
        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        // Act
        String result = p.toString();

        // Assert
        assertNotNull(result);
    }

    @Test
    void toStringContainsExpectedValues() {
        // Arrange
        when(_titleDouble.toString()).thenReturn("Dune");
        when(_authorIdDouble.toString()).thenReturn("Herbert F.-ABC123");
        when(_yearDouble.toString()).thenReturn("1965");
        when(_genreIdDouble.toString()).thenReturn("SCIENCE FICTION");

        // SUT
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, _synopsis);

        String result = p.toString();

        // Assert
        assertTrue(result.contains("Dune"));
        assertTrue(result.contains("Herbert F.-ABC123"));
        assertTrue(result.contains("1965"));
        assertTrue(result.contains("SCIENCE FICTION"));
    }

    @Test
    void constructorSynopsisExceeds300CharsThrowsException() {
        //Arrange
        String longSynopsis = "a".repeat(301);

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, longSynopsis));
    }

    @Test
    void constructorSynopsisExactly300CharsCreatesPublication() {
        //Arrange
        String synopsis = "a".repeat(300);

        //Act + Assert
        Publication p = new Publication(_titleDouble, _authorIdDouble, _yearDouble, _genreIdDouble, synopsis);
        assertEquals(synopsis, p.getSynopsis());
    }

}



