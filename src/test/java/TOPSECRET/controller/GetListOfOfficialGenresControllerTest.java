package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetListOfOfficialGenresControllerTest {

    private User _user;
    private GenreRepo _genreRepo;
    private GetListOfOfficialGenresController _getListOfOfficialGenresController;

    @BeforeEach
    void setUp() {

        _user = new User(
                new Name("Zé Isep"),
                new Email("ze@isep.pt")
        );
        _genreRepo = new GenreRepo();
        _getListOfOfficialGenresController = new GetListOfOfficialGenresController(_genreRepo, _user);

    }

    @Test
    void test_a_constructor_get_official_genres_controller() {

        //act
        new GetListOfOfficialGenresController(_genreRepo, _user);

    }

    @Test
    void test_get_list_of_official_genres_should_return_list_with_genres() {

        //arrange
        _genreRepo.create("fiction");
        _genreRepo.create("romance");

        //act
        List<Genre> listOfOfficialGenres = _getListOfOfficialGenresController.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);

    }

    @Test
    void test_get_list_of_official_genres_should_return_empty_list_if_no_genres_were_added() {

        //act
        List<Genre> listOfOfficialGenres = _getListOfOfficialGenresController.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);
        assertTrue(listOfOfficialGenres.isEmpty());

    }

}