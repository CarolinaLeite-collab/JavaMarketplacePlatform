package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenreRepoTest {

    private GenreFactory _genreFactoryDouble;

    @BeforeEach
    void setUp() {
        _genreFactoryDouble = mock(GenreFactory.class);
    }

    @Test
    void constructorOfGenreRepoShouldCreateGenreRepo() {

        //SUT
        new GenreRepo(_genreFactoryDouble);

    }

    @Test
    void addNewGenreToRepoShouldSucceed() {

        // Arrange
        String genreName = "New Genre";

        Genre genreDouble = mock(Genre.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);

        // SUT
        GenreRepo repo = new GenreRepo(_genreFactoryDouble);

        // Act
        Genre addedGenre = repo.addGenre(genreName);

        // Assert
        assertEquals(genreDouble, addedGenre);
        assertEquals(1, repo.getListOfOfficialGenres().size());
        assertEquals(addedGenre, repo.getListOfOfficialGenres().get(0));

        // the below is called twice: once in genreExists method (not stored), once in addGenre (is stored)
        verify(_genreFactoryDouble, atLeastOnce()).createGenre(genreName);

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
        GenreRepo repo = new GenreRepo(_genreFactoryDouble);

        // Act
        Genre addedGenre = repo.addGenre(genreName);
        Genre addedGenre2 = repo.addGenre(genre2Name);

        // Assert
        assertEquals(genreDouble1, addedGenre);
        assertEquals(genreDouble2, addedGenre2);
        assertEquals(2, repo.getListOfOfficialGenres().size());
        assertEquals(addedGenre, repo.getListOfOfficialGenres().get(0));
        assertEquals(addedGenre2, repo.getListOfOfficialGenres().get(1));

        verify(_genreFactoryDouble, atLeastOnce()).createGenre(genreName);
        verify(_genreFactoryDouble, atLeastOnce()).createGenre(genre2Name);

    }

    @Test
    void addExistingGenreToRepoShouldFail() {

        // Arrange
        String genreName = "Another Genre";

        Genre genreDouble = mock(Genre.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);

        // SUT
        GenreRepo repo = new GenreRepo(_genreFactoryDouble);

        // Act
        repo.addGenre(genreName);
        // Attempting to add genreName again
        //Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> repo.addGenre(genreName));
        assertEquals("This genre already exists", exception.getMessage());

        verify(_genreFactoryDouble,atLeastOnce()).createGenre(genreName);

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
        GenreRepo repo = new GenreRepo(_genreFactoryDouble);

        // Act
        repo.addGenre(genreName);
        repo.addGenre(genre2Name);

        List<Genre> listOfOfficialGenres = repo.getListOfOfficialGenres();

        // Assert
        assertNotNull(listOfOfficialGenres);
        assertEquals(2, listOfOfficialGenres.size());
        assertEquals(genreName, listOfOfficialGenres.get(0).getGenre());
        assertEquals(genre2Name, listOfOfficialGenres.get(1).getGenre());

        verify(_genreFactoryDouble, atLeastOnce()).createGenre(genreName);
        verify(_genreFactoryDouble, atLeastOnce()).createGenre(genre2Name);

    }

    @Test
    void getListOfNoGenresShouldReturnIsEmpty() {

        // SUT
        GenreRepo repo = new GenreRepo(_genreFactoryDouble);

        // Act
        List<Genre> listOfOfficialGenres = repo.getListOfOfficialGenres();

        // Assert
        assertNotNull(listOfOfficialGenres);
        assertTrue(listOfOfficialGenres.isEmpty());

    }
}