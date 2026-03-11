package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationFactoryTest {

    private PublicationFactory factory;

    private PublicationType _typeDouble;
    private Identifier _identifierDouble;
    private Year _yearDouble;
    private Title _titleDouble;
    private Author _authorDouble;
    private PublishingCompany _publishingCompanyDouble;
    private Edition _editionDouble;
    private Genre _genreDouble;

    @BeforeEach
    void setUp() {
        factory = new PublicationFactory();

        _typeDouble = mock(PublicationType.class);
        _identifierDouble = mock(Identifier.class);
        _yearDouble = mock(Year.class);
        _titleDouble = mock(Title.class);
        _authorDouble = mock(Author.class);
        _publishingCompanyDouble = mock(PublishingCompany.class);
        _editionDouble = mock(Edition.class);
        _genreDouble = mock(Genre.class);

        when(_typeDouble.getPublicationType()).thenReturn("BOOK");
        when(_identifierDouble.getIdentifier()).thenReturn("ID");
        when(_yearDouble.getValue()).thenReturn(2024);
        when(_titleDouble.getTitle()).thenReturn("T");
        when(_authorDouble.getName()).thenReturn("A");
        when(_publishingCompanyDouble.getName()).thenReturn("P");
        when(_genreDouble.getGenre()).thenReturn("G");
    }

    @Test
    void createPublication_returnsNonNullPublication() {
        Publication p = factory.createPublication(
                _typeDouble,
                _identifierDouble,
                _yearDouble,
                _titleDouble,
                _authorDouble,
                _publishingCompanyDouble,
                _editionDouble,
                _genreDouble
        );

        assertNotNull(p);
    }

    @Test
    void createPublication_storesExactInstances() {
        Publication p = factory.createPublication(
                _typeDouble,
                _identifierDouble,
                _yearDouble,
                _titleDouble,
                _authorDouble,
                _publishingCompanyDouble,
                _editionDouble,
                _genreDouble
        );

        assertSame(_typeDouble, p.getPublicationType());
        assertSame(_identifierDouble, p.getIdentifier());
        assertSame(_yearDouble, p.getPublicationYear());
        assertSame(_titleDouble, p.getTitle());
        assertSame(_authorDouble, p.getAuthor());
        assertSame(_publishingCompanyDouble, p.getPublisher());
        assertSame(_editionDouble, p.getEdition());
        assertSame(_genreDouble, p.getGenre());
    }

    @Test
    void createPublication_usesProvidedPublicationType() {
        when(_typeDouble.getPublicationType()).thenReturn("MAGAZINE");

        Publication p = factory.createPublication(
                _typeDouble,
                _identifierDouble,
                _yearDouble,
                _titleDouble,
                _authorDouble,
                _publishingCompanyDouble,
                _editionDouble,
                _genreDouble
        );

        assertEquals("MAGAZINE", p.getPublicationType().getPublicationType());
    }

    @Test
    void createPublication_allowsNullOptionalFields() {
        Publication p = factory.createPublication(
                _typeDouble,
                _identifierDouble,
                _yearDouble,
                _titleDouble,
                _authorDouble,
                _publishingCompanyDouble,
                null,   // edition
                null    // genre
        );

        assertNotNull(p);
        assertNull(p.getEdition());
        assertNull(p.getGenre());
    }
}
