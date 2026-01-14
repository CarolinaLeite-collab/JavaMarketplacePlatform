package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateLibraryControllerTest {

    //Set up test objects
    private User _user;
    private LibraryRepo _repo;
    private CreateLibraryController _createLibraryController;

    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé ISEP"),
                new Address ("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", Address.Country.PORTUGAL, "4300-111", null ),
                new Email ("testing@isep.ipp.pt"),
                new Phone( new PhonePrefix("+351"),"911234567")
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

        //arrange
        String userID = "123456";

        //act
        Library newLibrary = _createLibraryController.createMyLibrary(userID);

        //assert
        assertNotNull(newLibrary);

    }

    @Test
    void create_a_second_library_for_same_user_should_return_exception(){

        //arrange
        String userID = "123456";

        //act
        Library newLibrary = _createLibraryController.createMyLibrary(userID);

        //tries to add another library
        //assert
        assertThrows(IllegalStateException.class,()->{
            _createLibraryController.createMyLibrary(userID);
        });

    }

    @Test
    void create_2_libraries_for_2_users_should_return_not_null(){

        //arrange
        String userID = "123456";
        String userID2 = "234567";

        //act
        Library newLibrary = _createLibraryController.createMyLibrary(userID);
        Library newLibrary2 = _createLibraryController.createMyLibrary(userID2);


        //assert
        assertNotNull(newLibrary);
        assertNotNull(newLibrary2);

    }



}