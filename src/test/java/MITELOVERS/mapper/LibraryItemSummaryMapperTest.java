package MITELOVERS.mapper;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Picture;
import MITELOVERS.domain.valueobject.Title;
import MITELOVERS.dto.LibraryItemSummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryItemSummaryMapperTest {

    @Test
    void toDTOWithPictureReturnsCorrectDTO() {
        // Arrange
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

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
        assertEquals("1984", dto.getTitle());
        assertEquals("https://example.com/1984.jpg", dto.getPicture());
        assertTrue(dto.hasLinks());
    }

    @Test
    void toDTOWithNullPictureReturnsNullPicture() {
        // Arrange
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));

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
        assertTrue(dto.hasLinks());
    }
}