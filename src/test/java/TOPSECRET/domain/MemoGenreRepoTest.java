package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.GenreId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        Genre _genreDouble = mock(Genre.class);
        when(_genreDouble.identity()).thenReturn(new GenreId("Fiction"));
        when(_genreFactoryDouble.createGenre(any(GenreId.class), eq("Fiction")))
                .thenReturn(_genreDouble);
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Genre result = repo.addGenre("Fiction"); // SUT

        // Assert
        assertSame(_genreDouble, result);

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

        when(_genreFactoryDouble.createGenre(any(GenreId.class), eq("Fiction"))).thenReturn(genre1Double);
        when(_genreFactoryDouble.createGenre(any(GenreId.class), eq("Romance"))).thenReturn(genre2Double);

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
        Genre _genreDouble = mock(Genre.class);
        when(_genreDouble.identity()).thenReturn(new GenreId("Fiction"));
        when(_genreFactoryDouble.createGenre(any(GenreId.class), eq("Fiction")))
                .thenReturn(_genreDouble);
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);
        repo.addGenre("Fiction");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.addGenre("Fiction")); // SUT
}

    @Test
    void saveValidGenreReturnsGenre() {
        // Arrange
        Genre _genreDouble = mock(Genre.class);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Genre result = repo.save(_genreDouble);

        // Assert
        assertSame(_genreDouble, result);
    }

    @Test
    void findAllReturnsAllStoredGenres() {
        // Arrange
        Genre _genreDouble1 = mock(Genre.class);
        Genre _genreDouble2 = mock(Genre.class);
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);
        repo.save(_genreDouble1);
        repo.save(_genreDouble2);

        // Act
        Iterable<Genre> result = repo.findAll(); // SUT

        // Assert
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void findAllEmptyRepoReturnsEmptyIterable() {
        // Arrange
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Iterable<Genre> result = repo.findAll(); // SUT

        // Assert
        assertFalse(result.iterator().hasNext());
    }
    @Test
    void ofIdentityExistingGenreIdReturnsGenre() {
        // Arrange
        GenreId _genreIdDouble = mock(GenreId.class);
        Genre _genreDouble = mock(Genre.class);
        when(_genreDouble.identity()).thenReturn(_genreIdDouble);
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);
        repo.save(_genreDouble);

        // Act
        Optional<Genre> result = repo.ofIdentity(_genreIdDouble); // SUT

        // Assert
        assertTrue(result.isPresent());
        assertSame(_genreDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingGenreIdReturnsEmpty() {
        // Arrange
        GenreId _genreIdDouble = mock(GenreId.class);
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Optional<Genre> result = repo.ofIdentity(_genreIdDouble); // SUT

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void containsOfIdentityExistingGenreIdReturnsTrue() {
        // Arrange
        GenreId _genreIdDouble = mock(GenreId.class);
        Genre _genreDouble = mock(Genre.class);
        when(_genreDouble.identity()).thenReturn(_genreIdDouble);
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);
        repo.save(_genreDouble);

        // Act
        boolean result = repo.containsOfIdentity(_genreIdDouble); // SUT

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingGenreIdReturnsFalse() {
        // Arrange
        GenreId _genreIdDouble = mock(GenreId.class);
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        boolean result = repo.containsOfIdentity(_genreIdDouble); // SUT

        // Assert
        assertFalse(result);
    }
}