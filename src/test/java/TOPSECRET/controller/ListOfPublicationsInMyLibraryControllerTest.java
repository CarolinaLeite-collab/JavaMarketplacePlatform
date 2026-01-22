package TOPSECRET.controller;
import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsInMyLibraryControllerTest {

    private User _user;
    private LibraryRepo libraryRepo;
    private ListOfPublicationsInMyLibraryController controller;

    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé Isep"),
                new Email("test@isep.com")
        );

        libraryRepo = new LibraryRepo();
        controller = new ListOfPublicationsInMyLibraryController(libraryRepo, _user);
    }


    @Test
    void shouldReturnEmptyList_whenLibraryExistsButEmpty() {
        // Arrange
        Library mylibrary = libraryRepo.create(_user);

        // Act
        List<PublicationDetails> result = controller.getListOfPublications(_user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void shouldThrowException_whenLibraryNotFound() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> libraryRepo.findByUser(_user)
        );
        assertEquals("Library not found for user: Zé Isep", exception.getMessage());
    }

}
