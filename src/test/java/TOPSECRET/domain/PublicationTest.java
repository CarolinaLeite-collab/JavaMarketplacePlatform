package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
    private Edition _editionDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {

        _publicationTypeDouble = mock(PublicationType.class);
        _identifierDouble = mock(Identifier.class);
        _yearDouble = mock(Year.class);
        _titleDouble = mock(Title.class);
        _authorDouble = mock(Author.class);
        _publishingCompanyDouble = mock(PublishingCompany.class);
        _editionDouble = mock(Edition.class);
        _genreDouble = mock(Genre.class);

        when(_titleDouble.getTitle()).thenReturn("Title");
        when(_authorDouble.getName()).thenReturn("Author");
        when(_publishingCompanyDouble.getName()).thenReturn("Publishing Company");
        when(_editionDouble.getEditionNumber()).thenReturn(Integer.valueOf("1"));
        when(_genreDouble.getGenre()).thenReturn("Genre");
    }

    // --------------
    // Success tests
    // --------------
    @Test
    void Build_Book_with_all_fields_succeeds() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        // Act
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
                .genre(_genreDouble)
                .build();

        // Assert
        assertNotNull(p);
        assertSame(_publicationTypeDouble, p.getPublicationType());
        assertSame(_identifierDouble, p.getIdentifier());
        assertSame(_yearDouble, p.getPublicationYear());
        assertSame(_titleDouble, p.getTitle());
        assertSame(_authorDouble, p.getAuthor());
        assertSame(_publishingCompanyDouble, p.getPublisher());
        assertSame(_editionDouble, p.getEdition());
        assertSame(_genreDouble, p.getGenre());
    }

    @Test
    void Build_Book_with_only_the_mandatory_fields_succeeds() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        // Act
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertNotNull(p);
        assertSame(_publicationTypeDouble, p.getPublicationType());
        assertSame(_identifierDouble, p.getIdentifier());
        assertSame(_yearDouble, p.getPublicationYear());
        assertSame(_titleDouble, p.getTitle());
        assertSame(_authorDouble, p.getAuthor());
        assertSame(_publishingCompanyDouble, p.getPublisher());
    }

    @Test
    void Build_Magazine_without_Author_succeeds() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

        // Act
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertNotNull(p);
        assertSame(_publicationTypeDouble, p.getPublicationType());
        assertSame(_identifierDouble, p.getIdentifier());
        assertSame(_yearDouble, p.getPublicationYear());
        assertSame(_titleDouble, p.getTitle());
        assertSame(_publishingCompanyDouble, p.getPublisher());
    }

//    @Nested
//    class MatchGenreTests {
//
//        private Publication publication;
//
//        @BeforeEach
//        void setUp() {
//            publication = Publication.builder()
//                    .type(new PublicationType("BOOK"))
//                    .identifier(new ISBN("9780691181950"))
//                    .year(Year.of(2019))
//                    .title(new Title("How to Keep Your Cool"))
//                    .author(new Author("Seneca"))
//                    .publisher(new PublishingCompany("Penguin"))
//                    .genre(new Genre("History"))
//                    .build();
//        }
//
//        @Test
//        void matchGenre_withSameGenre_returnsTrue() {
//            assertTrue(publication.matchGenre(new Genre("History")));
//        }
//
//        @Test
//        void matchGenre_withDifferentGenre_returnsFalse() {
//            assertFalse(publication.matchGenre(new Genre("Fantasy")));
//        }
//
//        @Test
//        void matchGenre_withNullGenre_returnsFalse() {
//            assertFalse(publication.matchGenre(null));
//        }
//    }

    // --------------------
    // Negative path tests
    // --------------------
    @Test
    void Build_Throws_when_missing_PublicationType() {
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

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
    void Build_Throws_when_missing_Identifier() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

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
    void Build_Throws_when_missing_Year() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");

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
    void Build_Throws_when_missing_Title() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

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
    void Build_Book_throws_when_missing_Author() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

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
    void Build_Book_throws_when_missing_Publisher() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

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
    void Build_Magazine_throws_when_missing_Publisher() {
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(Integer.valueOf("2026"));

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
    void Books_from_1970_or_before_are_compared_by_Title_and_Year() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_yearDouble.getValue()).thenReturn(1970);

        // Two different identifiers
        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ID-A");
        when(idB.getIdentifier()).thenReturn("ID-B");

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

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
    void Books_from_1970_return_false_when_Title_Differs() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(1970);

        // Two different titles
        Title titleA = mock(Title.class);
        Title titleB = mock(Title.class);
        when(titleA.getTitle()).thenReturn("Title A");
        when(titleB.getTitle()).thenReturn("Title B");

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble) // ignored for <= 1970
                .year(_yearDouble)
                .title(titleA)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble) // ignored for <= 1970
                .year(_yearDouble)
                .title(titleB)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertNotEquals(a, b);
    }

    @Test
    void Books_from_earlier_than_1970_are_compared_by_the_Identifier() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_yearDouble.getValue()).thenReturn(1971);

        // Different identifiers
        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ID-A");
        when(idB.getIdentifier()).thenReturn("ID-B");

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertNotEquals(a, b);
    }

    @Test
    void Magazines_from_1976_and_before_are_compared_by_title_and_year() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_yearDouble.getValue()).thenReturn(1976);

        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ISSN-A");
        when(idB.getIdentifier()).thenReturn("ISSN-B");

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertEquals(a, b);
    }

    @Test
    void Magazine_from_1976_or_before_returns_false_when_title_differs() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(1976);

        // Two different titles
        Title titleA = mock(Title.class);
        Title titleB = mock(Title.class);
        when(titleA.getTitle()).thenReturn("Magazine A");
        when(titleB.getTitle()).thenReturn("Magazine B");

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble) // ignored for <= 1976
                .year(_yearDouble)
                .title(titleA)
                .publisher(_publishingCompanyDouble)
                .build();

        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble) // ignored for <= 1976
                .year(_yearDouble)
                .title(titleB)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertNotEquals(a, b);
    }

    @Test
    void Magazines_older_than_1976_are_compared_using_the_identifier() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("MAGAZINE");
        when(_yearDouble.getValue()).thenReturn(1977);

        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ISSN-A");
        when(idB.getIdentifier()).thenReturn("ISSN-B");

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertNotEquals(a, b);
    }

    @Test
    void equals_returnsFalse_whenNull() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

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
    void equals_returnsFalse_whenDifferentClass() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);


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
    void Default_publicationType_compares_Title_And_Year() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("NEWSPAPER");
        when(_yearDouble.getValue()).thenReturn(1900);
        when(_titleDouble.getTitle()).thenReturn("Same");

        // Two different identifiers (ignored for default types)
        Identifier idA = mock(Identifier.class);
        Identifier idB = mock(Identifier.class);
        when(idA.getIdentifier()).thenReturn("ID-A");
        when(idB.getIdentifier()).thenReturn("ID-B");

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idA)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(idB)
                .year(_yearDouble)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertEquals(a, b);
    }

    @Test
    void Default_publicationType_returns_false_when_title_differs() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("NEWSPAPER");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(1900);

        // Two different titles
        Title titleA = mock(Title.class);
        Title titleB = mock(Title.class);
        when(titleA.getTitle()).thenReturn("A");
        when(titleB.getTitle()).thenReturn("B");

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(titleA)
                .publisher(_publishingCompanyDouble)
                .build();

        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(titleB)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertNotEquals(a, b);
    }

    @Test
    void Default_publicationType_returns_false_when_year_differs() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("NEWSPAPER");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");

        // Two different years
        Year yearA = mock(Year.class);
        Year yearB = mock(Year.class);
        when(yearA.getValue()).thenReturn(1900);
        when(yearB.getValue()).thenReturn(1901);

        Publication a = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(yearA)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        Publication b = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(yearB)
                .title(_titleDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertNotEquals(a, b);
    }

    @Test
    void Equals_is_reflective_returns_true_for_same_instance() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);


        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .build();

        // Assert
        assertTrue(p.equals(p));
    }

    @Test
    void getters_returnPublisherGenreTitleID() {
        // Arrange
        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2019);

        // Act
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

        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble) // same instance passed to isByAuthor
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
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

        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
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

        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
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

        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
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

        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble) // same instance passed to isByAuthor
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
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

        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
                .build();

        // Act
        boolean result = p.isByPublishingCompany(otherPublishingCompany);

        // Assert
        assertFalse(result);
    }


}

