package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
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
    private LibraryFactory _libraryFactory;

    @BeforeEach
    void setUp() {

        _userIdDouble = mock(UserId.class);
        _iLibraryRepoDouble = mock(ILibraryRepo.class);
        _libraryFactory = mock(LibraryFactory.class);

    }

    @Test
    void testCreateLibraryController() {

        // SUT
        new CreateLibraryController(_iLibraryRepoDouble, _libraryFactory, _userIdDouble);

    }

    @Test
    void shouldCreateLibrarySuccessfully() {

        // Arrange
        Library libraryDouble = mock(Library.class);

        when(_libraryFactory.createLibrary(_userIdDouble))
                .thenReturn(libraryDouble);

        when(_iLibraryRepoDouble.containsOfIdentity(libraryDouble.identity()))
                .thenReturn(false);

        // SUT
        CreateLibraryController ctl =
                new CreateLibraryController(_iLibraryRepoDouble, _libraryFactory, _userIdDouble);

        // Act
        boolean result = ctl.createLibrary(_userIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldThrowWhenLibraryAlreadyExists() {

        // Arrange
        Library libraryDouble = mock(Library.class);

        when(_libraryFactory.createLibrary(_userIdDouble))
                .thenReturn(libraryDouble);

        when(_iLibraryRepoDouble.containsOfIdentity(libraryDouble.identity()))
                .thenReturn(true);

        // SUT
        CreateLibraryController ctl =
                new CreateLibraryController(_iLibraryRepoDouble, _libraryFactory, _userIdDouble);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> ctl.createLibrary(_userIdDouble));
    }
}

