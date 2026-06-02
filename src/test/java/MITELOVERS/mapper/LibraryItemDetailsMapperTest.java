package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.ISBN;
import MITELOVERS.domain.valueobject.ISSN;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.NoIdentifier;
import MITELOVERS.dto.response.LibraryItemDetailsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryItemDetailsMapperTest {

    @Test
    void toDTOWithISBNReturnsCorrectDTO() {
        // Arrange
        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("George Orwell");

        ISBN isbnDouble = mock(ISBN.class);
        when(isbnDouble.toString()).thenReturn("978-0-451-52493-5");

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(isbnDouble);

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("BOOK");

        // Act
        LibraryItemDetailsMapper mapper = new LibraryItemDetailsMapper();
        LibraryItemDetailsDTO dto = mapper.toDTO(authorDouble, editionDouble,  publicationTypeDouble);

        // Assert
        assertEquals("George Orwell", dto.getAuthorName());
        assertEquals("BOOK", dto.getPublicationType());
        assertEquals("978-0-451-52493-5", dto.getIdentifier());
    }

    @Test
    void toDTOWithISSNReturnsCorrectDTO() {
        // Arrange
        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("Jane Doe");

        ISSN issnDouble = mock(ISSN.class);
        when(issnDouble.toString()).thenReturn("2049-3630");

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(issnDouble);

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("MAGAZINE");

        // Act
        LibraryItemDetailsMapper mapper = new LibraryItemDetailsMapper();
        LibraryItemDetailsDTO dto = mapper.toDTO(authorDouble, editionDouble,  publicationTypeDouble);

        // Assert
        assertEquals("Jane Doe", dto.getAuthorName());
        assertEquals("MAGAZINE", dto.getPublicationType());
        assertEquals("2049-3630", dto.getIdentifier());
    }

    @Test
    void toDTOWithNoIdentifierReturnsCorrectDTO() {
        // Arrange
        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("George Orwell");

        NoIdentifier noIdentifierDouble = mock(NoIdentifier.class);
        when(noIdentifierDouble.toString()).thenReturn("no identifier");

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(noIdentifierDouble);

        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("BOOK");

        // Act
        LibraryItemDetailsMapper mapper = new LibraryItemDetailsMapper();
        LibraryItemDetailsDTO dto = mapper.toDTO(authorDouble, editionDouble,  publicationTypeDouble);

        // Assert
        assertEquals("George Orwell", dto.getAuthorName());
        assertEquals("BOOK", dto.getPublicationType());
        assertEquals("no identifier", dto.getIdentifier());
    }
}