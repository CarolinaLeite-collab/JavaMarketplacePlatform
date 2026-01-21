package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryRepoTest {

    private User _user;

    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé Isep"),
                new Email ("test@isep.ipp.pt")
        );

    }


    @Test
    void findByUserShouldReturnCorrectLibraryWhenExists() {
        // Arrange
        LibraryRepo libraryRepo = new LibraryRepo();
        Library mylibrary = libraryRepo.create(_user);

        // Act
        Library actualLibrary = libraryRepo.findByUser(_user);

        // Assert
        assertEquals(mylibrary.getUser(), actualLibrary.getUser());
    }


    @Test
    void findByUserShouldThrowExceptionWhenLibraryDoesNotExist() {
        // Arrange

        LibraryRepo libraryRepo = new LibraryRepo();

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> libraryRepo.findByUser(_user));
    }

}
