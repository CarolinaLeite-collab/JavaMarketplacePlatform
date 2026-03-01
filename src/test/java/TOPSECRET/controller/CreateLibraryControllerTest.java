package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateLibraryControllerTest {

    //Set up test objects
    private User _userDouble;
    private User _userDouble2;
    private LibraryRepo _repoDouble;
    private CreateLibraryController _createLibraryController;

    @BeforeEach
    void setUp() {

        _userDouble = mock(User.class);
        _repoDouble = mock(LibraryRepo.class);
        _createLibraryController = new CreateLibraryController(_repoDouble, _userDouble);
    }


    @Test
    //test create library
    void test_create_a_library_for_1_user() {

        //Arrange
        Library libraryDouble = mock(Library.class);
        when(_repoDouble.createMyLibrary(_userDouble)).thenReturn(libraryDouble);

        //act
        Library myLibrary = _createLibraryController.createMyLibrary(_userDouble);

        //assert
        assertEquals(libraryDouble, myLibrary);

    }

    @Test
    void create_a_second_library_for_same_user_should_return_exception(){

        //arrange
       when(_repoDouble.createMyLibrary(_userDouble)).thenThrow(new IllegalStateException());

        //tries to add another library
        //assert
        assertThrows(IllegalStateException.class,()->{
            _createLibraryController.createMyLibrary(_userDouble);
        });

    }

    @Test
    void createMyLibrary_should_delegate_to_repo(){
        // Arrange
        Library libraryDouble = mock(Library.class);
        when(_repoDouble.createMyLibrary(_userDouble)).thenReturn(libraryDouble);

        // Act
        _createLibraryController.createMyLibrary(_userDouble);

        // Assert
        verify(_repoDouble).createMyLibrary(_userDouble);
    }

//    @Test
//    void create_2_libraries_for_2_users_should_return_not_null(){
//
//        //arrange and act
//        Library newLibrary = _createLibraryController.createMyLibrary(_userDouble);
//        Library newLibrary2 = _createLibraryController.createMyLibrary(_userDouble2);
//
//
//        //assert
//        assertNotNull(newLibrary);
//        assertNotNull(newLibrary2);
//
//    }



}