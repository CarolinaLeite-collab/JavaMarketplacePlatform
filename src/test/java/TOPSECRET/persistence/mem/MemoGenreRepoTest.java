package TOPSECRET.persistence.mem;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.genre.GenreFactory;
import TOPSECRET.domain.valueobject.GenreId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(new GenreId("Fiction"));
        when(_genreFactoryDouble.createGenre(eq("Fiction")))
                .thenReturn(genreDouble);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Genre result = repo.addGenre("Fiction");

        // Assert
        assertSame(genreDouble, result);

    }

    @Test
    void addMultipleNewGenresToRepoShouldSucceed() {

        // Arrange
        String genreName = "Fiction";
        String genre2Name = "Romance";

        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);
        when(genre1Double.identity()).thenReturn(new GenreId("Fiction"));
        when(genre2Double.identity()).thenReturn(new GenreId("Romance"));

        when(_genreFactoryDouble.createGenre(eq("Fiction"))).thenReturn(genre1Double);
        when(_genreFactoryDouble.createGenre(eq("Romance"))).thenReturn(genre2Double);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Genre addedGenre = repo.addGenre(genreName);
        Genre addedGenre2 = repo.addGenre(genre2Name);

        // Assert
        assertEquals(genre1Double, addedGenre);
        assertEquals(genre2Double, addedGenre2);
    }

    @Test
    void addExistingGenreToRepoShouldFail() {

        // Arrange
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(new GenreId("Fiction"));
        when(_genreFactoryDouble.createGenre(eq("Fiction")))
                .thenReturn(genreDouble);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        repo.addGenre("Fiction");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addGenre("Fiction"));
}

    @Test
    void saveValidGenreReturnsGenre() {
        // Arrange
        Genre genreDouble = mock(Genre.class);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Genre result = repo.save(genreDouble);

        // Assert
        assertSame(genreDouble, result);
    }

    @Test
    void findAllReturnsAllStoredGenres() {
        // Arrange
        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);
        repo.save(genreDouble1);
        repo.save(genreDouble2);

        // Act
        Iterable<Genre> result = repo.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void findAllEmptyRepoReturnsEmptyIterable() {
        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Iterable<Genre> result = repo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }
    @Test
    void ofIdentityExistingGenreIdReturnsGenre() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(genreIdDouble);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);
        repo.save(genreDouble);

        // Act
        Optional<Genre> result = repo.ofIdentity(genreIdDouble); // SUT

        // Assert
        assertTrue(result.isPresent());
        assertSame(genreDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingGenreIdReturnsEmpty() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Optional<Genre> result = repo.ofIdentity(genreIdDouble); // SUT

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityExistingGenreIdReturnsTrue() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(genreIdDouble);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);
        repo.save(genreDouble);

        // Act
        boolean result = repo.containsOfIdentity(genreIdDouble); // SUT

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingGenreIdReturnsFalse() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        boolean result = repo.containsOfIdentity(genreIdDouble); // SUT

        // Assert
        assertFalse(result);
    }
}