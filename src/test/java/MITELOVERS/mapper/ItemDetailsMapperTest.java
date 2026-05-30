package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.ItemDetailsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemDetailsMapperTest {


    @Test
    void testItemDetailsMapperConstructor() {
        //SUT
        new ItemDetailsMapper();

    }

    @Test
    void testItemDetailsMapperReturnsDTOWithISBN() {
        //Arrange
        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("nameDouble");

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("titleDouble");

        ISBN isbnDouble = mock(ISBN.class);
        when(isbnDouble.toString()).thenReturn("978-1-4028-9462-6");

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(isbnDouble);
        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("publicationTypeDouble");
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        //Act + SUT
        ItemDetailsMapper mapper = new ItemDetailsMapper();
        ItemDetailsDTO dto = mapper.toDTO(editionDouble, publicationDouble, publicationTypeDouble, authorDouble);

        //Assert
        assertEquals("titleDouble", dto.getTitle());
        assertEquals("nameDouble", dto.getAuthorName());
        assertEquals("publicationTypeDouble", dto.getPublicationType());
        assertEquals("978-1-4028-9462-6", dto.getIdentifier());

    }

    @Test
    void testItemDetailsMapperReturnsDTOWithISSN() {
        //Arrange
        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("nameDouble");

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("titleDouble");

        ISSN issnDouble = mock(ISSN.class);
        when(issnDouble.toString()).thenReturn("2049-3630");

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(issnDouble);
        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("publicationTypeDouble");
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        //Act + SUT
        ItemDetailsMapper mapper = new ItemDetailsMapper();
        ItemDetailsDTO dto = mapper.toDTO(editionDouble, publicationDouble, publicationTypeDouble, authorDouble);

        //Assert
        assertEquals("titleDouble", dto.getTitle());
        assertEquals("nameDouble", dto.getAuthorName());
        assertEquals("publicationTypeDouble", dto.getPublicationType());
        assertEquals("2049-3630", dto.getIdentifier());

    }

    @Test
    void testItemDetailsMapperReturnsDTOWithNoIdentifier() {
        //Arrange
        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("nameDouble");

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("titleDouble");

        NoIdentifier noIdentifier = mock(NoIdentifier.class);
        when(noIdentifier.toString()).thenReturn("no identifier");

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(noIdentifier);
        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("publicationTypeDouble");
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        //Act + SUT
        ItemDetailsMapper mapper = new ItemDetailsMapper();
        ItemDetailsDTO dto = mapper.toDTO(editionDouble, publicationDouble, publicationTypeDouble, authorDouble);

        //Assert
        assertEquals("titleDouble", dto.getTitle());
        assertEquals("nameDouble", dto.getAuthorName());
        assertEquals("publicationTypeDouble", dto.getPublicationType());
        assertEquals("no identifier", dto.getIdentifier());

    }


}
