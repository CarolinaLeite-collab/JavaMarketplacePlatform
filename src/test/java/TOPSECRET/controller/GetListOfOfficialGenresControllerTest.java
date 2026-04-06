package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IGenreRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetListOfOfficialGenresControllerTest {

    private IGenreRepo _iGenreRepoDouble;

    @BeforeEach
    void setUp() {
        _iGenreRepoDouble = mock(IGenreRepo.class);
    }

    @Test
    void testConstructorGetOfficialGenresController() {

        //SUT
        GetListOfOfficialGenresController controller = new GetListOfOfficialGenresController(_iGenreRepoDouble);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void getListOfOfficialGenresShouldReturnListWithGenres() {

        // Arrange
        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(genre1Double, genre2Double));
        // SUT
        GetListOfOfficialGenresController controller =
                new GetListOfOfficialGenresController(_iGenreRepoDouble);

        // Act
        Iterable<Genre> result = controller.getListOfOfficialGenres();

        // Assert
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());

    }

    @Test
    void GetListOfOfficialGenresShouldReturnEmptyListIfNoGenresWereAdded() {
        // Arrange
        when(_iGenreRepoDouble.findAll()).thenReturn(List.of());
        GetListOfOfficialGenresController controller =
                new GetListOfOfficialGenresController(_iGenreRepoDouble);

        // Act
        Iterable<Genre> result = controller.getListOfOfficialGenres(); // SUT

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());

    }
}