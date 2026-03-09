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
    //Happy path

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

        when(_publicationTypeDouble.getPublicationType()).thenReturn("BOOK");

    }

    @Test
    void buildBook_withAllMandatoryFields_succeeds() {
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new PublishingCompany("Penguin"))
                .build();

        assertNotNull(p);
        assertEquals("BOOK", p.getPublicationType().getPublicationType());
        assertEquals(Year.of(2019), p.getPublicationYear());
    }

    @Test
    void buildMagazine_withoutAuthor_succeeds() {
        Publication p = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(2022))
                .title(new Title("Science Weekly"))
                .publisher(new PublishingCompany("Nature"))
                .build();

        assertNotNull(p);
        assertNull(p.getAuthor());
    }

    @Nested
    class MatchGenreTests {

        private Publication publication;

        @BeforeEach
        void setUp() {
            publication = Publication.builder()
                    .type(new PublicationType("BOOK"))
                    .identifier(new ISBN("9780691181950"))
                    .year(Year.of(2019))
                    .title(new Title("How to Keep Your Cool"))
                    .author(new Author("Seneca"))
                    .publisher(new PublishingCompany("Penguin"))
                    .genre(new Genre("History"))
                    .build();
        }

        @Test
        void matchGenre_withSameGenre_returnsTrue() {
            assertTrue(publication.matchGenre(new Genre("History")));
        }

        @Test
        void matchGenre_withDifferentGenre_returnsFalse() {
            assertFalse(publication.matchGenre(new Genre("Fantasy")));
        }

        @Test
        void matchGenre_withNullGenre_returnsFalse() {
            assertFalse(publication.matchGenre(null));
        }
    }

    @Test
    void build_throws_whenMissingPublicationType() {
        var ex = assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .identifier(new ISBN("9780618260300"))
                        .year(Year.of(2010))
                        .title(new Title("T"))
                        .author(new Author("A"))
                        .publisher(new PublishingCompany("P"))
                        .build()
        );
        assertTrue(ex.getMessage().contains("publicationType"));
    }

    @Test
    void build_throws_whenMissingIdentifier() {
        var ex = assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(new PublicationType("BOOK"))
                        .year(Year.of(2010))
                        .title(new Title("T"))
                        .author(new Author("A"))
                        .publisher(new PublishingCompany("P"))
                        .build()
        );
        assertTrue(ex.getMessage().contains("identifier"));
    }

    @Test
    void build_throws_whenMissingYear() {
        var ex = assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(new PublicationType("BOOK"))
                        .identifier(new ISBN("9780618260300"))
                        .title(new Title("T"))
                        .author(new Author("A"))
                        .publisher(new PublishingCompany("P"))
                        .build()
        );
        assertTrue(ex.getMessage().contains("publicationYear"));
    }

    @Test
    void build_throws_whenMissingTitle() {
        var ex = assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(new PublicationType("BOOK"))
                        .identifier(new ISBN("9780618260300"))
                        .year(Year.of(2010))
                        .author(new Author("A"))
                        .publisher(new PublishingCompany("P"))
                        .build()
        );
        assertTrue(ex.getMessage().contains("title"));
    }

    @Test
    void buildBook_throws_whenMissingAuthor() {
        var ex = assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(new PublicationType("BOOK"))
                        .identifier(new ISBN("9780618260300"))
                        .year(Year.of(2010))
                        .title(new Title("T"))
                        .publisher(new PublishingCompany("P"))
                        .build()
        );
        assertTrue(ex.getMessage().contains("author"));
    }

    @Test
    void buildBook_throws_whenMissingPublisher() {
        var ex = assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(new PublicationType("BOOK"))
                        .identifier(new ISBN("9780618260300"))
                        .year(Year.of(2010))
                        .title(new Title("T"))
                        .author(new Author("A"))
                        .build()
        );
        assertTrue(ex.getMessage().contains("publisher"));
    }

    @Test
    void buildMagazine_throws_whenMissingPublisher() {
        var ex = assertThrows(IllegalArgumentException.class, () ->
                Publication.builder()
                        .type(new PublicationType("MAGAZINE"))
                        .identifier(new ISSN("1234-5679")) // or whatever your ISSN type is
                        .year(Year.of(2010))
                        .title(new Title("T"))
                        .build()
        );
        assertTrue(ex.getMessage().contains("publisher"));
    }

    @Test
    void equals_bookYear1970_usesTitleAndYear_notIdentifier() {
        Publication a = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780618260300"))
                .year(Year.of(1970))
                .title(new Title("Same"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780136091813")) // different id
                .year(Year.of(1970))
                .title(new Title("Same"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();

        assertEquals(a, b);
    }

    @Test
    void equals_bookYear1971_usesIdentifier() {
        Publication a = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780618260300"))
                .year(Year.of(1971))
                .title(new Title("Same"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780136091813")) // different id
                .year(Year.of(1971))
                .title(new Title("Same"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();

        assertNotEquals(a, b);
    }

    @Test
    void equals_magazineYear1976_usesTitleAndYear_notIdentifier() {
        Publication a = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(1976))
                .title(new Title("Same"))
                .publisher(new PublishingCompany("P"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("9999-9999")) // different ISSN
                .year(Year.of(1976))
                .title(new Title("Same"))
                .publisher(new PublishingCompany("P"))
                .build();

        assertEquals(a, b);
    }

    @Test
    void equals_magazineYear1977_usesIdentifier() {
        Publication a = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(1977))
                .title(new Title("Same"))
                .publisher(new PublishingCompany("P"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("9999-9999")) // different ISSN
                .year(Year.of(1977))
                .title(new Title("Same"))
                .publisher(new PublishingCompany("P"))
                .build();

        assertNotEquals(a, b);
    }

    @Test
    void equals_returnsFalse_whenNull() {
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("T"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();

        assertFalse(p.equals(null));
    }

    @Test
    void equals_returnsFalse_whenDifferentClass() {
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("T"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();
        assertFalse(p.equals("not a publication"));
    }

    @Test
    void equals_defaultType_comparesTitleAndYear() {
        Publication a = Publication.builder()
                .type(new PublicationType("NEWSPAPER")) // triggers default
                .identifier(new NoIdentifier())
                .year(Year.of(1900))
                .title(new Title("Same"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("NEWSPAPER"))
                .identifier(new NoIdentifier())
                .year(Year.of(1900))
                .title(new Title("Same"))
                .build();

        assertEquals(a, b);
    }
    @Test
    void equals_isReflexive_returnsTrueForSameInstance() {
        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("T"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();

        assertTrue(p.equals(p));
    }
    @Test
    void equals_bookYear1970_returnsFalse_whenTitleDiffers() {
        Publication a = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780618260300"))
                .year(Year.of(1970))                 // <= 1970 forces title+year comparison
                .title(new Title("Title A"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780136091813")) // different id (ignored for <= 1970)
                .year(Year.of(1970))
                .title(new Title("Title B"))          // different title -> Objects.equals(...) becomes false
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .build();

        assertNotEquals(a, b);
    }
    @Test
    void equals_defaultType_returnsFalse_whenTitleDiffersButYearSame() {
        Publication a = Publication.builder()
                .type(new PublicationType("NEWSPAPER"))
                .identifier(new NoIdentifier())
                .year(Year.of(1900))
                .title(new Title("A"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("NEWSPAPER"))
                .identifier(new NoIdentifier())
                .year(Year.of(1900)) // same year
                .title(new Title("B")) // different title
                .build();

        assertNotEquals(a, b);
    }
    @Test
    void equals_defaultType_returnsFalse_whenTitleOrYearDiffers() {
        Publication a = Publication.builder()
                .type(new PublicationType("NEWSPAPER")) // triggers default
                .identifier(new NoIdentifier())
                .year(Year.of(1900))
                .title(new Title("Same"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("NEWSPAPER"))
                .identifier(new NoIdentifier())
                .year(Year.of(1901))              // different year (or change title)
                .title(new Title("Same"))
                .build();

        assertNotEquals(a, b);
    }
    @Test
    void equals_magazineYear1976_returnsFalse_whenTitleDiffers() {
        Publication a = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("1234-5678"))
                .year(Year.of(1976))               // <= 1976 → title + year comparison
                .title(new Title("Magazine A"))
                .publisher(new PublishingCompany("P"))
                .build();

        Publication b = Publication.builder()
                .type(new PublicationType("MAGAZINE"))
                .identifier(new ISSN("9999-9999")) // different ISSN (ignored in this branch)
                .year(Year.of(1976))
                .title(new Title("Magazine B"))    // different title → equals(...) must be false
                .publisher(new PublishingCompany("P"))
                .build();

        assertNotEquals(a, b);
    }


    @Test
    void getters_returnPublisherGenreTitleID() {
        PublishingCompany pub = new PublishingCompany("Penguin");
        Genre gen = new Genre("Science Fiction");
        Title tl = new Title("T");
        Identifier id = new ISBN("9780691181950");

        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(id)          // use same instance
                .year(Year.of(2019))
                .title(tl)               // use same instance
                .author(new Author("A"))
                .publisher(pub)          // use same instance
                .genre(gen)              // use same instance
                .build();

        assertSame(pub, p.getPublisher());
        assertSame(gen, p.getGenre());
        assertSame(tl, p.getTitle());
        assertSame(id, p.getIdentifier());
    }

    //needs to be altered when class edition is correct
    @Test
    void getters_returnEdition_whenSet() {
        Edition ed = new Edition(
                new NumberOfPages(30),
                null,
                LocalDate.of(1940, 2, 3),
                Binding.SADDLE_STITCH,
                new Description("Old book"),
                new Dimension(21, 29.7, 1, DimensionUnit.CENTIMETERS),
                new Weight(224.7, Weight.WeightUnit.GRAMS),
                Language.of("pt", "Portuguese", "Português"));


        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("T"))
                .author(new Author("A"))
                .publisher(new PublishingCompany("P"))
                .edition(ed)
                .build();

        assertEquals(ed, p.getEdition());
    }

    // Isolated test of isByAuthor method
    @Test
    void isByAuthorShouldReturnTrueWhenAuthorMatches() {

        //SUT / Arrange
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
                .build();

        //Act

        boolean result = p.isByAuthor(_authorDouble);

        //Assert
        assertTrue(result);

    }

    @Test
    void isByAuthorShouldReturnFalseWhenAuthorIsDifferent() {

        //Arrange

        Author _author2 = mock(Author.class);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
                .build();

        //Act

        boolean result = p.isByAuthor(_author2);

        //Assert
        assertFalse(result);

    }

    @Test
    void isByGenreShouldReturnTrueWhenGenreMatches() {

        //SUT / Arrange
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

        //Act
        boolean result = p.isByGenre(_genreDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void isByGenreShouldReturnFalseWhenGenreIsDifferent() {

        //Arrange
        Genre _genre2 = mock(Genre.class);

        //SUT
        Publication p = Publication.builder()
                .type(_publicationTypeDouble)
                .identifier(_identifierDouble)
                .year(_yearDouble)
                .title(_titleDouble)
                .author(_authorDouble)
                .publisher(_publishingCompanyDouble)
                .edition(_editionDouble)
                .build();

        //Act
        boolean result = p.isByGenre(_genre2);

        //Assert
        assertFalse(result);
    }
}

