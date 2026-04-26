package MITELOVERS.persistence.mem;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.valueobject.GenreId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemGenreRepoTest {

    @Test
    @Tag("unit")
    void constructorOfGenreRepoShouldCreateGenreRepo() {

        // SUT
        new MemGenreRepo();
    }

    @Test
    @Tag("unit")
    void classShouldBeAnnotatedWithRepositoryAndMemProfile() {
        // Arrange + Act
        Repository repository = MemGenreRepo.class.getAnnotation(Repository.class);
        Profile profile = MemGenreRepo.class.getAnnotation(Profile.class);

        // Assert
        assertNotNull(repository);
        assertNotNull(profile);
        assertArrayEquals(new String[]{"mem"}, profile.value());
    }

    @Test
    @Tag("unit")
    void saveValidGenreReturnsGenre() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(genreIdDouble);

        // SUT
        MemGenreRepo repo = new MemGenreRepo();

        // Act
        Genre result = repo.save(genreDouble);

        // Assert
        assertSame(genreDouble, result);
    }

    @Test
    @Tag("unit")
    void findAllReturnsAllStoredGenres() {
        // Arrange
        GenreId id1Double = mock(GenreId.class);
        GenreId id2Double = mock(GenreId.class);
        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);
        when(genre1Double.identity()).thenReturn(id1Double);
        when(genre2Double.identity()).thenReturn(id2Double);

        // SUT
        MemGenreRepo repo = new MemGenreRepo();
        repo.save(genre1Double);
        repo.save(genre2Double);

        // Act
        Iterable<Genre> result = repo.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    @Tag("unit")
    void findAllEmptyRepoReturnsEmptyIterable() {
        // Arrange & SUT
        MemGenreRepo repo = new MemGenreRepo();

        // Act
        Iterable<Genre> result = repo.findAll();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    @Tag("unit")
    void findAllKeysReturnsAllStoredKeys() {
        // Arrange
        GenreId id1Double = mock(GenreId.class);
        GenreId id2Double = mock(GenreId.class);
        Genre genre1Double = mock(Genre.class);
        Genre genre2Double = mock(Genre.class);
        when(genre1Double.identity()).thenReturn(id1Double);
        when(genre2Double.identity()).thenReturn(id2Double);

        // SUT
        MemGenreRepo repo = new MemGenreRepo();

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
    @Tag("unit")
    void findAllKeysEmptyRepoReturnsEmptyList() {
        // Arrange & SUT
        MemGenreRepo repo = new MemGenreRepo();

        // Act
        List<GenreId> result = repo.findAllKeys();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @Tag("unit")
    void ofIdentityExistingGenreIdReturnsGenre() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(genreIdDouble);

        // SUT
        MemGenreRepo repo = new MemGenreRepo();
        repo.save(genreDouble);

        // Act
        Optional<Genre> result = repo.ofIdentity(genreIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(genreDouble, result.get());
    }

    @Test
    @Tag("unit")
    void ofIdentityNonExistingGenreIdReturnsEmpty() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        // SUT
        MemGenreRepo repo = new MemGenreRepo();

        // Act
        Optional<Genre> result = repo.ofIdentity(genreIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @Tag("unit")
    void containsOfIdentityExistingGenreIdReturnsTrue() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(genreIdDouble);

        // SUT
        MemGenreRepo repo = new MemGenreRepo();
        repo.save(genreDouble);

        // Act
        boolean result = repo.containsOfIdentity(genreIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    @Tag("unit")
    void containsOfIdentityNonExistingGenreIdReturnsFalse() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        // SUT
        MemGenreRepo repo = new MemGenreRepo();

        // Act
        boolean result = repo.containsOfIdentity(genreIdDouble);

        // Assert
        assertFalse(result);
    }
}
