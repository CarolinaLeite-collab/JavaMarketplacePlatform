package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryRepoTest {

    @Test
    void findByUserShouldReturnCorrectLibraryWhenExists() {
        // Arrange
        LibraryRepo libraryRepo = new LibraryRepo();
        Library mylibrary = libraryRepo.create("user123");

        // Act
        Library actualLibrary = libraryRepo.findByUser("user123");

        // Assert
        assertEquals(mylibrary.getUserID(), actualLibrary.getUserID());
    }


    @Test
    void findByUserShouldThrowExceptionWhenLibraryDoesNotExist() {
        // Arrange

        LibraryRepo libraryRepo = new LibraryRepo();

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> libraryRepo.findByUser("inexistentUser"));
    }

}
