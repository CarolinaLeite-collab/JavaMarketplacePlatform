

package TOPSECRET.controller;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.LibraryRepo;
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

/*
   @Test
   void shouldReturnPublicationsListWhenUserHasLibrary() {
       // Arrange
       Library mylibrary = libraryRepo.create("user123");

        // Act
       List<String> result = controller.getListOfPublications("user123");

        // Assert
       assertNotNull(result);
       assertFalse(result.isEmpty());
    }
*/
    @Test
    void shouldReturnEmptyList_whenLibraryExistsButEmpty() {
        libraryRepo.create("user123");
        List<String> result = controller.getListOfPublications("user123");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /*
    @Test
    void shouldThrowExceptionWhenUserHasNoLibrary() {
        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> controller.getListOfPublications("user123")
        );
        assertEquals("User does not have a library!", exception.getMessage());
    }
*/
}


