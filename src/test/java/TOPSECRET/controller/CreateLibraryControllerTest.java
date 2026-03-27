package TOPSECRET.controller;

import TOPSECRET.domain.ILibraryRepo;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateLibraryControllerTest {


    private User _userDouble;
    private User _adminDouble;
    private ILibraryRepo _iLibraryRepoDouble;

    @BeforeEach
    void setUp() {

        _adminDouble = mock(User.class);
        _userDouble = mock(User.class);
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
    }

    @Test
    void testCreateLibraryController(){

        // SUT
        new CreateLibraryController(_iLibraryRepoDouble, _adminDouble);
    }


    @Test
    void createLibraryShouldReturnLibrary() {

        // Arrange
        Library libraryDouble = mock(Library.class);
        when(_iLibraryRepoDouble.addLibrary(_userDouble)).thenReturn(libraryDouble);

        // SUT
        CreateLibraryController createLibraryController = new CreateLibraryController(_iLibraryRepoDouble, _adminDouble);

        // Act
        Library myLibrary = createLibraryController.createLibrary(_userDouble);

        // Assert
        assertEquals(libraryDouble, myLibrary);
        verify(_iLibraryRepoDouble).addLibrary(_userDouble);

    }

    @Test
    void createLibraryShouldThrowWhenLibraryAlreadyExist(){

        // Arrange
       when(_iLibraryRepoDouble.addLibrary(_userDouble)).thenThrow(new IllegalStateException());

       // SUT
       CreateLibraryController createLibraryController = new CreateLibraryController(_iLibraryRepoDouble, _adminDouble);


        // Act & Assert
        assertThrows(IllegalStateException.class,
                ()-> createLibraryController.createLibrary(_userDouble));
        }

    }
