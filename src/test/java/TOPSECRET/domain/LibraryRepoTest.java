package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryRepoTest {

    private User _userDouble;
    private LibraryFactory _libraryFactoryDouble;
    private LibraryRepo _libraryRepo;


    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _libraryFactoryDouble = mock(LibraryFactory.class);
        _libraryRepo = new LibraryRepo(_libraryFactoryDouble);

    }

    @Test
    void testCreatingANewLibrary(){

        //arrange
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        //act
        Library mylibrary = _libraryRepo.addLibrary(_userDouble);

        //assert
        assertEquals(libraryDouble, mylibrary);
    }

    @Test
    void testAddingASecondLibraryShouldThrowAnException(){

        //Arrange
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        //act
        Library newLibrary = _libraryRepo.addLibrary(_userDouble);

        //
        assertThrows(IllegalStateException.class, () -> _libraryRepo.addLibrary(_userDouble));


    }



    @Test
    void findByUserShouldReturnCorrectLibraryWhenExists() {
        // Arrange
        Library libraryDouble = mock(Library.class);
        when(libraryDouble.belongsTo(_userDouble)).thenReturn(true);
        when(_libraryFactoryDouble.createLibrary(_userDouble)).thenReturn(libraryDouble);

        _libraryRepo.addLibrary(_userDouble);

        // Act
        Library actualLibrary = _libraryRepo.findLibraryByUser(_userDouble);

        // Assert
        assertEquals(libraryDouble, actualLibrary);
    }


    @Test
    void findByUser_shouldThrowExceptionWhenLibraryDoesNotExist() {


        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> _libraryRepo.findLibraryByUser(_userDouble));
    }

}
