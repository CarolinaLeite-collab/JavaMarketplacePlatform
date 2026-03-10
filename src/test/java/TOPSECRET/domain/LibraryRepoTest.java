package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryRepoTest {

    private User _userDouble;
    private LibraryFactory _libraryFactoryDouble;

    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _libraryFactoryDouble = mock(LibraryFactory.class);

    }

    @Test
    void addLibraryShouldReturnLibrary() {

        // Arrange
        LibraryRepo libraryRepo = new LibraryRepo(_libraryFactoryDouble); // SUT
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        // Act
        Library mylibrary = libraryRepo.addLibrary(_userDouble);

        // Assert
        assertEquals(libraryDouble, mylibrary);
    }

    @Test
    void addLibraryShouldThrowAnExceptionWhenLibraryAlreadyExists() {

        // Arrange
        LibraryRepo libraryRepo = new LibraryRepo(_libraryFactoryDouble); // SUT
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        // Act
        Library newLibrary = libraryRepo.addLibrary(_userDouble);

        // Assert
        assertThrows(IllegalStateException.class, () -> libraryRepo.addLibrary(_userDouble));
    }

    @Test
    void findLibraryByUserShouldReturnLibraryWhenExists() {

        // Arrange
        LibraryRepo libraryRepo = new LibraryRepo(_libraryFactoryDouble); // SUT
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        libraryRepo.addLibrary(_userDouble);

        // Act
        Library actualLibrary = libraryRepo.findLibraryByUser(_userDouble);

        // Assert
        assertEquals(libraryDouble, actualLibrary);
    }

    @Test
    void addLibraryShouldSucceedWhenOtherUserAlreadyHasLibrary() {
        // Arrange
        LibraryRepo libraryRepo = new LibraryRepo(_libraryFactoryDouble); // SUT
        User otherUserDouble = mock(User.class);
        Library libraryDouble = mock(Library.class);

        when(libraryDouble.belongsTo(otherUserDouble)).thenReturn(true);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(false);
        when(_libraryFactoryDouble.createLibrary(otherUserDouble)).thenReturn(libraryDouble);

        libraryRepo.addLibrary(otherUserDouble); // state setup

        Library libraryDouble2 = mock(Library.class);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble2);

        // Act
        Library result = libraryRepo.addLibrary(_userDouble);

        // Assert
        assertEquals(libraryDouble2, result);
    }

    @Test
    void findLibraryByUserShouldThrowExceptionWhenLibraryDoesNotExist() {

        // Arrange
        LibraryRepo libraryRepo = new LibraryRepo(_libraryFactoryDouble); // SUT

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> libraryRepo.findLibraryByUser(_userDouble));
    }
}