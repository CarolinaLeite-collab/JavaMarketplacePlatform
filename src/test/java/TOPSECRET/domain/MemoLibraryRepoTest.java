package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class MemoLibraryRepoTest {

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
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        // SUT
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble);

        // Act
        Library mylibrary = libraryRepo.addLibrary(_userDouble);

        // Assert
        assertEquals(libraryDouble, mylibrary);
    }

    @Test
    void addLibraryShouldThrowAnExceptionWhenLibraryAlreadyExists() {

        // Arrange
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        // SUT
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble);

        // Act
        libraryRepo.addLibrary(_userDouble);

        // Assert
        assertThrows(IllegalStateException.class, () -> libraryRepo.addLibrary(_userDouble));
    }

    @Test
    void findLibraryByUserShouldReturnLibraryWhenExists() {

        // Arrange
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        // SUT
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble);

        libraryRepo.addLibrary(_userDouble);

        // Act
        Library actualLibrary = libraryRepo.findLibraryByUser(_userDouble);

        // Assert
        assertEquals(libraryDouble, actualLibrary);
    }

    @Test
    void addLibraryShouldSucceedWhenOtherUserAlreadyHasLibrary() {
        // Arrange
        User otherUserDouble = mock(User.class);
        Library libraryDouble = mock(Library.class);

        when(libraryDouble.belongsTo(otherUserDouble)).thenReturn(true);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(false);
        when(_libraryFactoryDouble.createLibrary(otherUserDouble)).thenReturn(libraryDouble);

        // SUT
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble);

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
        ILibraryRepo libraryRepo = new MemoLibraryRepo(_libraryFactoryDouble); // SUT

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> libraryRepo.findLibraryByUser(_userDouble));
    }
}