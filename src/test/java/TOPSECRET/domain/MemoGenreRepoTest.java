package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoGenreRepoTest {

    private GenreFactory _genreFactoryDouble;

    @BeforeEach
    void setUp() {
        _genreFactoryDouble = mock(GenreFactory.class);
    }

    @Test
    void constructorOfGenreRepoShouldCreateGenreRepo() {

        //SUT
        new MemoGenreRepo(_genreFactoryDouble);

    }

    @Test
    void addNewGenreToRepoShouldSucceed() {

        // Arrange
        String genreName = "New Genre";

        Genre genreDouble = mock(Genre.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Genre addedGenre = repo.addGenre(genreName);

        // Assert
        assertEquals(genreDouble, addedGenre);
        assertEquals(1, repo.getListOfOfficialGenres().size());
        assertEquals(addedGenre, repo.getListOfOfficialGenres().get(0));

    }

    @Test
    void addMultipleNewGenresToRepoShouldSucceed() {

        // Arrange
        String genreName = "New Genre";
        String genre2Name = "Another Genre";

        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble1);
        when(_genreFactoryDouble.createGenre(genre2Name)).thenReturn(genreDouble2);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Genre addedGenre = repo.addGenre(genreName);
        Genre addedGenre2 = repo.addGenre(genre2Name);

        // Assert
        assertEquals(genreDouble1, addedGenre);
        assertEquals(genreDouble2, addedGenre2);
        assertEquals(2, repo.getListOfOfficialGenres().size());
        assertEquals(addedGenre, repo.getListOfOfficialGenres().get(0));
        assertEquals(addedGenre2, repo.getListOfOfficialGenres().get(1));
    }

    @Test
    void addExistingGenreToRepoShouldFail() {

        // Arrange
        String genreName = "Another Genre";

        Genre genreDouble = mock(Genre.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        repo.addGenre(genreName);
        // Attempting to add genreName again
        //Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.addGenre(genreName));
        assertEquals("This genre already exists", exception.getMessage());
    }

    @Test
    void getListOfGenresShouldReturnListOfGenres() {

        // Arrange
        String genreName = "New Genre";
        String genre2Name = "Another Genre";

        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble1);
        when(_genreFactoryDouble.createGenre(genre2Name)).thenReturn(genreDouble2);

        when(genreDouble1.getGenre()).thenReturn(genreName);
        when(genreDouble2.getGenre()).thenReturn(genre2Name);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        repo.addGenre(genreName);
        repo.addGenre(genre2Name);

        List<Genre> listOfOfficialGenres = repo.getListOfOfficialGenres();

        // Assert
        assertNotNull(listOfOfficialGenres);
        assertEquals(2, listOfOfficialGenres.size());
        assertEquals(genreName, listOfOfficialGenres.get(0).getGenre());
        assertEquals(genre2Name, listOfOfficialGenres.get(1).getGenre());
    }

    @Test
    void getListOfNoGenresShouldReturnIsEmpty() {

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        List<Genre> listOfOfficialGenres = repo.getListOfOfficialGenres();

        // Assert
        assertNotNull(listOfOfficialGenres);
        assertTrue(listOfOfficialGenres.isEmpty());

    }
}