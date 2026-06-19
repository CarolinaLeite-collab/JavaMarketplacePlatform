package MITELOVERS.dto;

import MITELOVERS.dto.response.LibraryItemResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LibraryItemResponseDTOTest {

    @Test
    void testLibraryItemResponseDTOConstructor() {
        // Arrange / Act (SUT)
        new LibraryItemResponseDTO(
                "itemId", "title", "author", "type", "identifier", "picture");
    }

    @Test
    void testGetItemId() {
        // Arrange
        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "itemId", "title", "author", "type", "identifier", "picture");

        // Act
        String itemId = dto.getItemId();

        // Assert
        assertEquals("itemId", itemId);
    }

    @Test
    void testGetTitle() {
        // Arrange
        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "itemId", "title", "author", "type", "identifier", "picture");

        // Act
        String title = dto.getTitle();

        // Assert
        assertEquals("title", title);
    }

    @Test
    void testGetAuthorName() {
        // Arrange
        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "itemId", "title", "author", "type", "identifier", "picture");

        // Act
        String authorName = dto.getAuthorName();

        // Assert
        assertEquals("author", authorName);
    }

    @Test
    void testGetPublicationType() {
        // Arrange
        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "itemId", "title", "author", "type", "identifier", "picture");

        // Act
        String publicationType = dto.getPublicationType();

        // Assert
        assertEquals("type", publicationType);
    }

    @Test
    void testGetIdentifier() {
        // Arrange
        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "itemId", "title", "author", "type", "identifier", "picture");

        // Act
        String identifier = dto.getIdentifier();

        // Assert
        assertEquals("identifier", identifier);
    }

    @Test
    void testGetPicture() {
        // Arrange
        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "itemId", "title", "author", "type", "identifier", "picture");

        // Act
        String picture = dto.getPicture();

        // Assert
        assertEquals("picture", picture);
    }

    @Test
    void testGetPictureWhenNull() {
        // Arrange
        LibraryItemResponseDTO dto = new LibraryItemResponseDTO(
                "itemId", "title", "author", "type", "identifier", null);

        // Act
        String picture = dto.getPicture();

        // Assert
        assertNull(picture);
    }
}