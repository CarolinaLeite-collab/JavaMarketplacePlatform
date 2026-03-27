package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.IGenreRepo;
import TOPSECRET.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetListOfOfficialGenresControllerTest {

    private User _userDouble;
    private IGenreRepo _iGenreRepoDouble;

    @Test
    void testConstructorGetOfficialGenresController() {
        //arrange
        _userDouble = mock(User.class);
        _iGenreRepoDouble = mock(IGenreRepo.class);
        //act
        //SUT
        new GetListOfOfficialGenresController(_iGenreRepoDouble, _userDouble);
    }

    @Test
    void testGetListOfOfficialGenresShouldReturnListWithGenres() {
        //arrange
        _userDouble = mock(User.class);
        _iGenreRepoDouble = mock(IGenreRepo.class);
        //SUT
        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble, _userDouble);

        Genre genre1 = mock(Genre.class);
        when(genre1.getGenre()).thenReturn("fiction");
        Genre genre2 = mock(Genre.class);
        when(genre2.getGenre()).thenReturn("romance");

        when(_iGenreRepoDouble.getListOfOfficialGenres()).thenReturn(List.of(genre1,genre2));

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
        _iGenreRepoDouble = mock(IGenreRepo.class);
        //SUT
        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble, _userDouble);

        //act
        List<Genre> listOfOfficialGenres = controller.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);
        assertTrue(listOfOfficialGenres.isEmpty());

    }
}