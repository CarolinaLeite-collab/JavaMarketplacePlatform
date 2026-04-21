package MITELOVERS.persistence.mem;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.valueobject.GenreId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        GenreId genreIdDouble = mock(GenreId.class);
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(genreIdDouble);

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
        GenreId id1Double = mock(GenreId.class);
        GenreId id2Double = mock(GenreId.class);
        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);
        when(genre1Double.identity()).thenReturn(id1Double);
        when(genre2Double.identity()).thenReturn(id2Double);

        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);
        repo.save(genre1Double);
        repo.save(genre2Double);

        // Act
        Iterable<Genre> result = repo.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void findAllEmptyRepoReturnsEmptyIterable() {
        // Arrange & SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        Iterable<Genre> result = repo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void findAllKeysReturnsAllStoredKeys() {
        // Arrange
        GenreId id1Double = mock(GenreId.class);
        GenreId id2Double = mock(GenreId.class);
        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);
        when(genre1Double.identity()).thenReturn(id1Double);
        when(genre2Double.identity()).thenReturn(id2Double);

        // SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        // Act
        repo.save(genre1Double);
        repo.save(genre2Double);


        List<GenreId> result = repo.findAllKeys();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(id1Double));
        assertTrue(result.contains(id2Double));
    }

    @Test
    void findAllKeysEmptyRepoReturnsEmptyList() {
        // Arrange & SUT
        MemoGenreRepo repo = new MemoGenreRepo(_genreFactoryDouble);

        List<GenreId> result = repo.findAllKeys();

        // Assert
        assertTrue(result.isEmpty());
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
        Optional<Genre> result = repo.ofIdentity(genreIdDouble);

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
        Optional<Genre> result = repo.ofIdentity(genreIdDouble);

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
        boolean result = repo.containsOfIdentity(genreIdDouble);

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
        boolean result = repo.containsOfIdentity(genreIdDouble);

        // Assert
        assertFalse(result);
    }
}
