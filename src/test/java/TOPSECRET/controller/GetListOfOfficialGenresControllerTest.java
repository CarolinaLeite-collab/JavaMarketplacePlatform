package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetListOfOfficialGenresControllerTest {

    private User _userDouble;
    private GenreRepo _genreRepoDouble;

    @Test
    void testConstructorGetOfficialGenresController() {
        //arrange
        _userDouble = mock(User.class);
        _genreRepoDouble = mock(GenreRepo.class);
        //act
        //SUT
        new GetListOfOfficialGenresController(_genreRepoDouble, _userDouble);
    }

    @Test
    void testGetListOfOfficialGenresShouldReturnListWithGenres() {
        //arrange
        _userDouble = mock(User.class);
        _genreRepoDouble = mock(GenreRepo.class);
        //SUT
        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_genreRepoDouble, _userDouble);

        Genre genre1 = mock(Genre.class);
        when(genre1.getGenre()).thenReturn("fiction");
        Genre genre2 = mock(Genre.class);
        when(genre2.getGenre()).thenReturn("romance");

        when(_genreRepoDouble.getListOfOfficialGenres()).thenReturn(List.of(genre1,genre2));

        //act
        List<Genre> listOfOfficialGenres = controller.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);
        assertFalse(listOfOfficialGenres.isEmpty());

    }

    @Test
    void testGetListOfOfficialGenresShouldReturnEmptyListIfNoGenresWereAdded() {
        //arrange
        _userDouble = mock(User.class);
        _genreRepoDouble = mock(GenreRepo.class);
        //SUT
        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_genreRepoDouble, _userDouble);

        //act
        List<Genre> listOfOfficialGenres = controller.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);
        assertTrue(listOfOfficialGenres.isEmpty());

    }
}