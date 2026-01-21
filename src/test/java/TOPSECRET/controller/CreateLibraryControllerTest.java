package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateLibraryControllerTest {

    //Set up test objects
    private User _user;
    private User _user2;
    private LibraryRepo _repo;
    private CreateLibraryController _createLibraryController;

    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé ISEP"),
                new Email ("testing@isep.ipp.pt")
        );

        _user2 = new User(
                new Name("Tó DEI"),
                new Email ("testing2@isep.ipp.pt")
                );

        _repo = new LibraryRepo();
        _createLibraryController = new CreateLibraryController(_repo, _user);
    }

    @Test
    //test constructor
    void test_a_constructor_controller_create_Library() {

        //arrange and act
        new CreateLibraryController(_repo, _user);

    }

    @Test
    //test create library
    void test_create_a_library_for_1_user() {

        //arrange and act
        Library newLibrary = _createLibraryController.createMyLibrary(_user);

        //assert
        assertNotNull(newLibrary);

    }

    @Test
    void create_a_second_library_for_same_user_should_return_exception(){

        //arrange and act
        Library newLibrary = _createLibraryController.createMyLibrary(_user);

        //tries to add another library
        //assert
        assertThrows(IllegalStateException.class,()->{
            _createLibraryController.createMyLibrary(_user);
        });

    }

    @Test
    void create_2_libraries_for_2_users_should_return_not_null(){

        //arrange and act
        Library newLibrary = _createLibraryController.createMyLibrary(_user);
        Library newLibrary2 = _createLibraryController.createMyLibrary(_user2);


        //assert
        assertNotNull(newLibrary);
        assertNotNull(newLibrary2);

    }



}