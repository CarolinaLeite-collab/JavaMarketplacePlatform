package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.configuration.IMockitoConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenreRepoTest {

    private GenreFactory _genreFactoryDouble;
    private Genre _genreDouble1;
    private Genre _genreDouble2;

    @BeforeEach
    void setUp() {
        _genreFactoryDouble = mock(GenreFactory.class);
        _genreDouble1 = mock(Genre.class);
        _genreDouble2 = mock(Genre.class);

        when(_genreFactoryDouble.createGenre("New Genre")).thenReturn(_genreDouble1);
        when(_genreFactoryDouble.createGenre("Another Genre")).thenReturn(_genreDouble2);

        when(_genreDouble1.getGenre()).thenReturn("New Genre");
        when(_genreDouble2.getGenre()).thenReturn("Another Genre");

    }

    @Test
    void addNewGenreToRepoShouldSucceed() {
        String genreName = "New Genre";

        GenreRepo repo = new GenreRepo(_genreFactoryDouble);
        Genre addedGenre = repo.addGenre(genreName);

        assertEquals(_genreDouble1, addedGenre);
        assertEquals(1, repo.getListOfOfficialGenres().size());
        assertEquals(genreName, repo.getListOfOfficialGenres().get(0).getGenre());

    }

    @Test
    void addMultipleNewGenresToRepoShouldSucceed() {
        String genreName = "New Genre";
        String genre2Name = "Another Genre";

        GenreRepo repo = new GenreRepo(_genreFactoryDouble);
        Genre addedGenre = repo.addGenre(genreName);
        Genre addedGenre2 = repo.addGenre(genre2Name);

        assertEquals(_genreDouble1, addedGenre);
        assertEquals(_genreDouble2, addedGenre2);
        assertEquals(2, repo.getListOfOfficialGenres().size());
        assertEquals(genreName, repo.getListOfOfficialGenres().get(0).getGenre());
        assertEquals(genre2Name, repo.getListOfOfficialGenres().get(1).getGenre());

    }

    @Test
    void addExistingGenreToRepoShouldFail() {
        String genreName = "Another Genre";

        GenreRepo repo = new GenreRepo(_genreFactoryDouble);

        repo.addGenre(genreName);

        // Attempting to add genreName again
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.addGenre(genreName));
        assertEquals("This genre already exists", exception.getMessage());

    }

    @Test
    void getListOfGenresShouldReturnListOfGenres() {
        String genreName = "New Genre";
        String genre2Name = "Another Genre";

        GenreRepo repo = new GenreRepo(_genreFactoryDouble);
        repo.addGenre(genreName);
        repo.addGenre(genre2Name);

        List<Genre> listOfOfficialGenres = repo.getListOfOfficialGenres();

        assertNotNull(listOfOfficialGenres);
        assertEquals(2, listOfOfficialGenres.size());
        assertEquals(genreName, listOfOfficialGenres.get(0).getGenre());
        assertEquals(genre2Name, listOfOfficialGenres.get(1).getGenre());

    }

    @Test
    void getListOfNoGenresShouldReturnIsEmpty() {
        GenreRepo repo = new GenreRepo(_genreFactoryDouble);

        List<Genre> listOfOfficialGenres = repo.getListOfOfficialGenres();

        assertNotNull(listOfOfficialGenres);
        assertTrue(listOfOfficialGenres.isEmpty());

    }
}