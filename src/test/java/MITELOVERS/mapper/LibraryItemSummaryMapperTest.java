package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.response.LibraryItemSummaryDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryItemSummaryMapperTest {

    @Test
    void toDTOWithCompleteDetailsReturnsCorrectDTO() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        Title titleDouble = mock(Title.class);
        Picture pictureDouble = mock(Picture.class);
        Name authorNameDouble = mock(Name.class);
        Identifier identifierDouble = mock(Identifier.class);

        Item itemDouble = mock(Item.class);
        Publication publicationDouble = mock(Publication.class);
        Edition editionDouble = mock(Edition.class);
        Author authorDouble = mock(Author.class);
        PublicationType publicationTypeDouble = mock(PublicationType.class);

        when(itemIdDouble.toString()).thenReturn("3C5D126F8B");
        when(titleDouble.toString()).thenReturn("1984");
        when(pictureDouble.toString()).thenReturn("https://example.com/1984.jpg");
        when(authorNameDouble.toString()).thenReturn("George Orwell");
        when(identifierDouble.toString()).thenReturn("9780451524935");
        when(publicationTypeDouble.toString()).thenReturn("Book");

        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getPicture()).thenReturn(pictureDouble);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);
        when(editionDouble.getIdentifier()).thenReturn(identifierDouble);
        when(authorDouble.getName()).thenReturn(authorNameDouble);

        // Act
        LibraryItemSummaryMapper mapper = new LibraryItemSummaryMapper();

        LibraryItemSummaryDTO dto = mapper.toDTO(
                itemDouble,
                publicationDouble,
                editionDouble,
                authorDouble,
                publicationTypeDouble
        );

        // Assert
        assertEquals("3C5D126F8B", dto.getItemId());
        assertEquals("1984", dto.getTitle());
        assertEquals("George Orwell", dto.getAuthorName());
        assertEquals("Book", dto.getPublicationType());
        assertEquals("9780451524935", dto.getIdentifier());
        assertEquals("https://example.com/1984.jpg", dto.getPicture());
        assertFalse(dto.hasLinks());
    }

    @Test
    void toDTOWithPictureReturnsCorrectDTO() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("3C5D126F8B");

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("1984");

        Picture pictureDouble = mock(Picture.class);
        when(pictureDouble.toString()).thenReturn("https://example.com/1984.jpg");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getPicture()).thenReturn(pictureDouble);

        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);

        // Act
        LibraryItemSummaryMapper mapper = new LibraryItemSummaryMapper();
        LibraryItemSummaryDTO dto = mapper.toDTO(itemDouble, publicationDouble);

        // Assert
        assertEquals("3C5D126F8B", dto.getItemId());
        assertEquals("1984", dto.getTitle());
        assertEquals("https://example.com/1984.jpg", dto.getPicture());
        assertFalse(dto.hasLinks());
    }

    @Test
    void toDTOWithNullPictureReturnsNullPicture() {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.toString()).thenReturn("3F9F4BFAB2");

        Title titleDouble = mock(Title.class);
        when(titleDouble.toString()).thenReturn("1984");

        Item itemDouble = mock(Item.class);
        when(itemDouble.identity()).thenReturn(itemIdDouble);
        when(itemDouble.getPicture()).thenReturn(null);

        Publication publicationDouble = mock(Publication.class);
        when(publicationDouble.getTitle()).thenReturn(titleDouble);

        // Act
        LibraryItemSummaryMapper mapper = new LibraryItemSummaryMapper();
        LibraryItemSummaryDTO dto = mapper.toDTO(itemDouble, publicationDouble);

        // Assert
        assertEquals("1984", dto.getTitle());
        assertNull(dto.getPicture());
        assertFalse(dto.hasLinks());
    }
}