package TOPSECRET.controller;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.LibraryRepo;
import TOPSECRET.domain.PublicationDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsInMyLibraryControllerTest {

    private LibraryRepo libraryRepo;
    private ListOfPublicationsInMyLibraryController controller;

    @BeforeEach
    void setUp() {
        libraryRepo = new LibraryRepo();
        controller = new ListOfPublicationsInMyLibraryController(libraryRepo);
    }


    @Test
    void shouldReturnEmptyList_whenLibraryExistsButEmpty() {
        // Arrange
        Library mylibrary = libraryRepo.create("user123");

        // Act
        List<PublicationDetails> result = controller.getListOfPublications("user123");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void shouldThrowException_whenLibraryNotFound() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> libraryRepo.findByUser("user123")
        );
        assertEquals("Library not found for user: user123", exception.getMessage());
    }

}
