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
    void createLibraryShouldReturnLibrary() {

        // Arrange
        Library libraryDouble = mock(Library.class); //stub
        when(_repoDouble.addLibrary(_userDouble)).thenReturn(libraryDouble);
        CreateLibraryController createLibraryController = new CreateLibraryController(_repoDouble);

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
       CreateLibraryController createLibraryController = new CreateLibraryController(_repoDouble);


        // Act & Assert
        assertThrows(IllegalStateException.class,
                ()-> createLibraryController.createLibrary(_userDouble));
        }

    }
