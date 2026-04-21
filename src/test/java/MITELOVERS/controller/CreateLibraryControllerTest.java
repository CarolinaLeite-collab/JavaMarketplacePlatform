package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateLibraryControllerTest {


    private UserId _userIdDouble;
    private ILibraryRepo _iLibraryRepoDouble;

    @BeforeEach
    void setUp() {

        _userIdDouble = mock(UserId.class);
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
    }

    @Test
    void testCreateLibraryController(){

        // SUT
        new CreateLibraryController(_iLibraryRepoDouble, _userIdDouble);
    }


    @Test
    void createLibraryShouldReturnLibrary() {

        // Arrange
        Library libraryDouble = mock(Library.class);
        when(_iLibraryRepoDouble.addLibrary(_userIdDouble)).thenReturn(libraryDouble);

        // SUT
        CreateLibraryController createLibraryController = new CreateLibraryController(_iLibraryRepoDouble, _userIdDouble);

        //Act
        boolean result = createLibraryController.createLibrary(_userIdDouble);

        // Assert
        assertTrue(result);

    }

    @Test
    void createLibraryShouldThrowWhenLibraryAlreadyExist(){

        // Arrange
       when(_iLibraryRepoDouble.addLibrary(_userIdDouble)).thenThrow(new IllegalStateException());

       // SUT
       CreateLibraryController createLibraryController = new CreateLibraryController(_iLibraryRepoDouble, _userIdDouble);


        // Act & Assert
        assertThrows(IllegalStateException.class,
                ()-> createLibraryController.createLibrary(_userIdDouble));
        }

    }
