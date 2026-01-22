package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryRepoTest {

    private User _user;
    private Library _library;
    private LibraryRepo _libraryRepo;

    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé Isep"),
                new Email ("test@isep.ipp.pt")
        );

        _library = new Library(_user);
        _libraryRepo = new LibraryRepo();

    }

    @Test
    void testCreatingANewLibrary(){

        //act
        Library newLibrary = _libraryRepo.create(_user);

        //assert
        assertNotNull(_libraryRepo.findByUser(newLibrary.getUser()));
    }

    @Test
    void testAddingASecondLibraryShouldThrowAnException(){

        //act
        Library newLibrary = _libraryRepo.create(_user);

        //
        assertThrows(IllegalStateException.class, () -> _libraryRepo.create(_user));


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
