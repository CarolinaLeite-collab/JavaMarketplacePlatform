package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetListOfOfficialGenresControllerTest {

    private User _user;
    private GenreRepo _genreRepo;
    private GetListOfOfficialGenresController _getListOfOfficialGenresController;

    @Test
    void test_a_constructor_get_official_genres_controller() {
        _user = mock(User.class);
        _genreRepo = mock(GenreRepo.class);
        _getListOfOfficialGenresController = new GetListOfOfficialGenresController(_genreRepo, _user);
        //act
        new GetListOfOfficialGenresController(_genreRepo, _user);

    }

    @Test
    void test_get_list_of_official_genres_should_return_list_with_genres() {
        //arrange
        _user = mock(User.class);
        _genreRepo = mock(GenreRepo.class);
        _getListOfOfficialGenresController = new GetListOfOfficialGenresController(_genreRepo, _user);

        Genre genre1 = mock(Genre.class);
        when(genre1.getGenre()).thenReturn("fiction");
        Genre genre2 = mock(Genre.class);
        when(genre2.getGenre()).thenReturn("romance");

        when(_genreRepo.getListOfOfficialGenres()).thenReturn(List.of(genre1,genre2));

        //act
        List<Genre> listOfOfficialGenres = _genreRepo.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);
        assertFalse(listOfOfficialGenres.isEmpty());

    }

    @Test
    void test_get_list_of_official_genres_should_return_empty_list_if_no_genres_were_added() {
        //arrange
        _user = mock(User.class);
        _genreRepo = mock(GenreRepo.class);
        _getListOfOfficialGenresController = new GetListOfOfficialGenresController(_genreRepo, _user);

        //act
        List<Genre> listOfOfficialGenres = _getListOfOfficialGenresController.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);
        assertTrue(listOfOfficialGenres.isEmpty());

    }

}