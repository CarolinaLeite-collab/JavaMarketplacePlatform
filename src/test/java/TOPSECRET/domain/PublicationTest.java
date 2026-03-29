package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.valueobject.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationTest {

    private PublicationType _publicationTypeDouble;
    private Identifier _identifierDouble;
    private Year _yearDouble;
    private Title _titleDouble;
    private Author _authorDouble;
    private PublishingCompany _publishingCompanyDouble;
    private EditionBook _editionBookDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {

        _publicationTypeDouble = mock(PublicationType.class);
        _identifierDouble = mock(Identifier.class);
        _yearDouble = mock(Year.class);
        _titleDouble = mock(Title.class);
        _authorDouble = mock(Author.class);
        _publishingCompanyDouble = mock(PublishingCompany.class);
        _editionBookDouble = mock(EditionBook.class);
        _genreDouble = mock(Genre.class);

        when(_titleDouble.getTitle()).thenReturn("Title");
        when(_authorDouble.getName()).thenReturn("Author");
        when(_publishingCompanyDouble.getName()).thenReturn("Publishing Company");
        when(_editionBookDouble.getEditionNumber()).thenReturn(Integer.valueOf("1"));
        when(_genreDouble.getGenre()).thenReturn("Genre");
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
    }

    // --------------
    // Success tests
    // --------------
    @Test
    void buildBookWithAllFieldsSucceeds() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

       //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .genre(_genreDouble)
                .build();

        //Act + Assert
        assertNotNull(p);
        assertSame(_publicationTypeDouble, p.getPublicationType());
        assertSame(_identifierDouble, p.getIdentifier());
        assertSame(_yearDouble, p.getPublicationYear());
        assertSame(_titleDouble, p.getTitle());
        assertSame(_authorDouble, p.getAuthor());
        assertSame(_publishingCompanyDouble, p.getPublisher());
        assertSame(_editionBookDouble, p.getEdition());
        assertSame(_genreDouble, p.getGenre());
    }

    @Test
    void buildBookWithOnlyTheMandatoryFieldsSucceeds() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Act + Assert
        assertNotNull(p);
        assertSame(_publicationTypeDouble, p.getPublicationType());
        assertSame(_identifierDouble, p.getIdentifier());
        assertSame(_yearDouble, p.getPublicationYear());
        assertSame(_titleDouble, p.getTitle());
        assertSame(_authorDouble, p.getAuthor());
        assertSame(_publishingCompanyDouble, p.getPublisher());
    }

    @Test
    void buildMagazineWithoutAuthorSucceeds() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //Act + Assert
        assertNotNull(p);
        assertSame(_publicationTypeDouble, p.getPublicationType());
        assertSame(_identifierDouble, p.getIdentifier());
        assertSame(_yearDouble, p.getPublicationYear());
        assertSame(_titleDouble, p.getTitle());
        assertSame(_publishingCompanyDouble, p.getPublisher());
    }

    @Test
    void matchGenreWithSameGenreReturnsTrue() {
        //Arrange
        when(_genreDouble.getGenre()).thenReturn("Action");

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .genre(_genreDouble)
                .build();
        //Act
        boolean result = p.isByGenre(_genreDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void matchGenreWithDifferentGenreReturnsFalse() {
        //Arrange
        Genre _genreDouble2 = mock(Genre.class);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .genre(_genreDouble)
                .build();

        //Act + Assert
        assertFalse(p.isByGenre(_genreDouble2));
    }

    @Test
    void matchGenreWithNullGenreReturnsFalse() {
        //Arrange
        Publication p = Publication.builder() //SUT
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .genre(_genreDouble)
                .build();

        //Act + Assert
        assertFalse(p.isByGenre(null));
    }

    // --------------------
    // Negative path tests
    // --------------------
    @Test
    void buildThrowsWhenMissingPublicationType() {
        //Arrange
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .identifier(_identifierDouble)
                        .year(_yearDouble)
                        .title(_titleDouble)
                        .author(_authorDouble)
                        .publisher(_publishingCompanyDouble)
                        .build()
        );
    }

    @Test
    void buildThrowsWhenMissingIdentifier() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(_publicationTypeDouble)
                        .year(_yearDouble)
                        .title(_titleDouble)
                        .author(_authorDouble)
                        .publisher(_publishingCompanyDouble)
                        .build()
        );
    }

    @Test
    void buildThrowsWhenMissingYear() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(_publicationTypeDouble)
                        .identifier(_identifierDouble)
                        .title(_titleDouble)
                        .author(_authorDouble)
                        .publisher(_publishingCompanyDouble)
                        .build()
        );
    }

    @Test
    void buildThrowsWhenMissingTitle() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(_publicationTypeDouble)
                        .identifier(_identifierDouble)
                        .year(_yearDouble)
                        .author(_authorDouble)
                        .publisher(_publishingCompanyDouble)
                        .build()
        );
    }

    @Test
    void buildBookThrowsWhenMissingAuthor() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(_publicationTypeDouble)
                        .identifier(_identifierDouble)
                        .year(_yearDouble)
                        .title(_titleDouble)
                        .publisher(_publishingCompanyDouble)
                        .build()
        );
    }

    @Test
    void buildBookThrowsWhenMissingPublisher() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(_publicationTypeDouble)
                        .identifier(_identifierDouble)
                        .year(_yearDouble)
                        .title(_titleDouble)
                        .author(_authorDouble)
                        .build()
        );
    }

    @Test
    void buildMagazineThrowsWhenMissingPublisher() {
        //Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(_publicationTypeDouble)
                        .identifier(_identifierDouble)
                        .year(_yearDouble)
                        .title(_titleDouble)
                        .build()
        );
    }

    @Test
    void booksFrom1970OrBeforeAreComparedByTitleAndYear() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_yearDouble.getValue()).thenReturn(1970);

        // Two different identifiers
        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ID-A");
        when(idB.getIdentifier()).thenReturn("ID-B");

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertEquals(a, b);
    }

    @Test
    void booksFrom1970ReturnFalseWhenTitleDiffers() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(1970);

        // Two different titles
        Title titleA = mock(Title.class);
        Title titleB = mock(Title.class);
        when(titleA.getTitle()).thenReturn("Title A");
        when(titleB.getTitle()).thenReturn("Title B");

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble) // ignored for <= 1970
                .year(_yearDouble)
                .title(titleA)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble) // ignored for <= 1970
                .year(_yearDouble)
                .title(titleB)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void booksFromEarlierThan1970AreComparedByTheIdentifier() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_yearDouble.getValue()).thenReturn(1971);

        // Different identifiers
        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ID-A");
        when(idB.getIdentifier()).thenReturn("ID-B");

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void magazinesFrom1976AndBeforeAreComparedByTitleAndYear() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_yearDouble.getValue()).thenReturn(1976);

        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ISSN-A");
        when(idB.getIdentifier()).thenReturn("ISSN-B");

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Act + Assert
        assertEquals(a, b);
    }

    @Test
    void magazineFrom1976OrBeforeReturnsFalseWhenTitleDiffers() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(1976);

        // Two different titles
        Title titleA = mock(Title.class);
        Title titleB = mock(Title.class);
        when(titleA.getTitle()).thenReturn("Magazine A");
        when(titleB.getTitle()).thenReturn("Magazine B");

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble) // ignored for <= 1976
                .year(_yearDouble)
                .title(titleA)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble) // ignored for <= 1976
                .year(_yearDouble)
                .title(titleB)
                .publisher(_publishingCompanyDouble)
                .build();

        //Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void magazinesOlderThan1976AreComparedUsingTheIdentifier() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_yearDouble.getValue()).thenReturn(1977);

        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ISSN-A");
        when(idB.getIdentifier()).thenReturn("ISSN-B");

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void equalsReturnsFalseWhenNull() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Act + Assert
        assertFalse(p.equals(null));
    }

    @Test
    void equalsReturnsFalseWhenDifferentClass() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Act + Assert
        assertFalse(p.equals("not a publication"));
    }

    @Test
    void defaultPublicationTypeComparesTitleAndYear() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("NEWSPAPER");
        when(_yearDouble.getValue()).thenReturn(1900);
        when(_titleDouble.getTitle()).thenReturn("Same");

        // Two different identifiers (ignored for default types)
        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ID-A");
        when(idB.getIdentifier()).thenReturn("ID-B");

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Act + Assert
        assertEquals(a, b);
    }

    @Test
    void defaultPublicationTypeReturnsFalseWhenTitleDiffers() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("NEWSPAPER");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(1900);

        // Two different titles
        Title titleA = mock(Title.class);
        Title titleB = mock(Title.class);
        when(titleA.getTitle()).thenReturn("A");
        when(titleB.getTitle()).thenReturn("B");

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(titleA)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(titleB)
                .publisher(_publishingCompanyDouble)
                .build();

        //Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void defaultPublicationTypeReturnsFalseWhenYearDiffers() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("NEWSPAPER");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");

        // Two different years
        Year yearA = mock(Year.class);
        Year yearB = mock(Year.class);
        when(yearA.getValue()).thenReturn(1900);
        when(yearB.getValue()).thenReturn(1901);

        //SUT
        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(yearA)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //SUT
        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(yearB)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void equalsIsReflectiveReturnsTrueForSameInstance() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        //Act + Assert
        assertTrue(p.equals(p));
    }

    @Test
    void gettersReturnPublisherGenreTitleID() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        // SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .genre(_genreDouble)
                .build();

        // Assert: same instances, not copies
        assertSame(_publicationTypeDouble, p.getPublicationType());
        assertSame(_identifierDouble, p.getIdentifier());
        assertSame(_yearDouble, p.getPublicationYear());
        assertSame(_titleDouble, p.getTitle());
        assertSame(_authorDouble, p.getAuthor());
        assertSame(_publishingCompanyDouble, p.getPublisher());
        assertSame(_genreDouble, p.getGenre());
    }

    // ----------------------
    // Tests for specific UC
    // ----------------------

    // Isolated test of isByAuthor method
    @Test
    void isByAuthorShouldReturnTrueWhenAuthorMatches() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble) // same instance passed to isByAuthor
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .build();

        // Act
        boolean result = p.isByAuthor(_authorDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void isByAuthorShouldReturnFalseWhenAuthorIsDifferent() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        Author otherAuthor = mock(Author.class);

        // SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .build();

        // Act
        boolean result = p.isByAuthor(otherAuthor);

        // Assert
        assertFalse(result);
    }

    @Test
    void isByGenreShouldReturnTrueWhenGenreMatches() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .genre(_genreDouble) // same instance passed to isByGenre
                .build();

        // Act
        boolean result = p.isByGenre(_genreDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void isByGenreShouldReturnFalseWhenGenreIsDifferent() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        Genre otherGenre = mock(Genre.class);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .build();

        // Act
        boolean result = p.isByGenre(otherGenre);

        // Assert
        assertFalse(result);
    }

    // Isolated test of isByPublishingCompany method
    @Test
    void isByPublishingCompanyShouldReturnTrueWhenPublishingCompanyMatches() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble) // same instance passed to isByAuthor
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .build();

        // Act
        boolean result = p.isByPublishingCompany(_publishingCompanyDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void isByPublishingCompanyShouldReturnFalseWhenPublishingCompanyIsDifferent() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        PublishingCompany otherPublishingCompany = mock(PublishingCompany.class);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionBookDouble)
                .build();

        // Act
        boolean result = p.isByPublishingCompany(otherPublishingCompany);

        // Assert
        assertFalse(result);
    }


}

