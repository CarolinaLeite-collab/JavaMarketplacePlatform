package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.LibraryItemResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryItemResponseDTOMapperTest {

    @Test
    void testLibraryItemResponseDTOMapperConstructor() {
        // SUT
        new LibraryItemResponseDTOMapper();
    }

    @Test
    void testToDTOReturnsDTOWithISBN() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("itemIdDouble");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getPicture()).thenReturn(null);

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("titleDouble");
        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);

        ISBN isbnDouble = mock(ISBN.class);
        when(isbnDouble.toString()).thenReturn("978-1-4028-9462-6");
        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(isbnDouble);

        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("nameDouble");
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("publicationTypeDouble");

        // Act + SUT
        LibraryItemResponseDTOMapper mapper = new LibraryItemResponseDTOMapper();
        LibraryItemResponseDTO dto = mapper.toDTO(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble);

        // Assert
        assertEquals("itemIdDouble", dto.getItemId());
        assertEquals("titleDouble", dto.getTitle());
        assertEquals("nameDouble", dto.getAuthorName());
        assertEquals("publicationTypeDouble", dto.getPublicationType());
        assertEquals("978-1-4028-9462-6", dto.getIdentifier());
        assertNull(dto.getPicture());
    }

    @Test
    void testToDTOReturnsDTOWithISSN() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("itemIdDouble");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getPicture()).thenReturn(null);

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("titleDouble");
        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);

        ISSN issnDouble = mock(ISSN.class);
        when(issnDouble.toString()).thenReturn("2049-3630");
        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(issnDouble);

        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("nameDouble");
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("publicationTypeDouble");

        // Act + SUT
        LibraryItemResponseDTOMapper mapper = new LibraryItemResponseDTOMapper();
        LibraryItemResponseDTO dto = mapper.toDTO(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble);

        // Assert
        assertEquals("itemIdDouble", dto.getItemId());
        assertEquals("titleDouble", dto.getTitle());
        assertEquals("nameDouble", dto.getAuthorName());
        assertEquals("publicationTypeDouble", dto.getPublicationType());
        assertEquals("2049-3630", dto.getIdentifier());
        assertNull(dto.getPicture());
    }

    @Test
    void testToDTOReturnsDTOWithNoIdentifier() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("itemIdDouble");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getPicture()).thenReturn(null);

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("titleDouble");
        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);

        NoIdentifier noIdentifierDouble = mock(NoIdentifier.class);
        when(noIdentifierDouble.toString()).thenReturn("no identifier");
        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(noIdentifierDouble);

        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("nameDouble");
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("publicationTypeDouble");

        // Act + SUT
        LibraryItemResponseDTOMapper mapper = new LibraryItemResponseDTOMapper();
        LibraryItemResponseDTO dto = mapper.toDTO(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble);

        // Assert
        assertEquals("itemIdDouble", dto.getItemId());
        assertEquals("titleDouble", dto.getTitle());
        assertEquals("nameDouble", dto.getAuthorName());
        assertEquals("publicationTypeDouble", dto.getPublicationType());
        assertEquals("no identifier", dto.getIdentifier());
        assertNull(dto.getPicture());
    }

    @Test
    void testToDTOReturnsDTOWithPicture() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("itemIdDouble");

        Picture pictureDouble = mock(Picture.class);
        when(pictureDouble.toString()).thenReturn("https://example.com/cover.jpg");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getPicture()).thenReturn(pictureDouble);

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("titleDouble");
        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);

        ISBN isbnDouble = mock(ISBN.class);
        when(isbnDouble.toString()).thenReturn("978-1-4028-9462-6");
        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getIdentifier()).thenReturn(isbnDouble);

        Name nameDouble = mock(Name.class);
        when(nameDouble.toString()).thenReturn("nameDouble");
        Author authorDouble = mock(Author.class);
        when(authorDouble.getName()).thenReturn(nameDouble);

        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.toString()).thenReturn("publicationTypeDouble");

        // Act + SUT
        LibraryItemResponseDTOMapper mapper = new LibraryItemResponseDTOMapper();
        LibraryItemResponseDTO dto = mapper.toDTO(
                itemDouble, publicationDouble, editionDouble, authorDouble, publicationTypeDouble);

        // Assert
        assertEquals("https://example.com/cover.jpg", dto.getPicture());
    }
}