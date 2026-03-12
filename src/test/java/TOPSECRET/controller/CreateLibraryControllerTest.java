package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateLibraryControllerTest {


    private User _userDouble;
    private LibraryRepo _repoDouble;

    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _repoDouble = mock(LibraryRepo.class);
    }

    @Test
    void testCreateLibraryController(){

        // SUT
        new CreateLibraryController(_repoDouble, _userDouble);
    }


    @Test
    void createLibraryShouldReturnLibrary() {

        // Arrange
        Library libraryDouble = mock(Library.class);
        when(_repoDouble.addLibrary(_userDouble)).thenReturn(libraryDouble);

        // SUT
        CreateLibraryController createLibraryController = new CreateLibraryController(_repoDouble, _userDouble);

        // Act
        Library myLibrary = createLibraryController.createLibrary(_userDouble);

        // Assert
        assertEquals(libraryDouble, myLibrary);
        verify(_repoDouble).addLibrary(_userDouble);

    }

    @Test
    void createLibraryShouldThrowWhenLibraryAlreadyExist(){

        // Arrange
       when(_repoDouble.addLibrary(_userDouble)).thenThrow(new IllegalStateException());

       // SUT
       CreateLibraryController createLibraryController = new CreateLibraryController(_repoDouble, _userDouble);


        // Act & Assert
        assertThrows(IllegalStateException.class,
                ()-> createLibraryController.createLibrary(_userDouble));
        }

    }
