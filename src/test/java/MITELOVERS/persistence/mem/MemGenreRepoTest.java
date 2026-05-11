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

@Tag("unit")
class MemGenreRepoTest {

    @Test
    void constructorOfGenreRepoShouldCreateGenreRepo() {
        // Arrange
        MemGenreRepo _sut;

        // SUT
        _sut = new MemGenreRepo();

        // Act
        MemGenreRepo result = _sut;

        // Assert
        assertNotNull(result);
    }

    @Test
    void classShouldBeAnnotatedWithRepositoryAndMemProfile() {
        // Arrange
        Class<MemGenreRepo> repoClass = MemGenreRepo.class;

        // SUT
        Class<MemGenreRepo> _sut = repoClass;

        // Act
        Repository repository = _sut.getAnnotation(Repository.class);
        Profile profile = _sut.getAnnotation(Profile.class);

        // Assert
        assertNotNull(repository);
        assertNotNull(profile);
        assertArrayEquals(new String[]{"mem"}, profile.value());
    }

    @Test
    void saveValidGenreReturnsGenre() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        Genre genreDouble = mock(Genre.class);
        when(genreDouble.identity()).thenReturn(genreIdDouble);

        // SUT
        MemGenreRepo _sut = new MemGenreRepo();

        // Act
        Genre result = _sut.save(genreDouble);

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

        // SUT
        MemGenreRepo _sut = new MemGenreRepo();
        _sut.save(genre1Double);
        _sut.save(genre2Double);

        // Act
        Iterable<Genre> result = _sut.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void findAllEmptyRepoReturnsEmptyIterable() {
        // Arrange
        MemGenreRepo repo = new MemGenreRepo();

        // SUT
        MemGenreRepo _sut = repo;

        // Act
        Iterable<Genre> result = _sut.findAll();

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
        MemGenreRepo _sut = new MemGenreRepo();

        // Act
        _sut.save(genre1Double);
        _sut.save(genre2Double);

        List<GenreId> result = _sut.findAllKeys();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(id1Double));
        assertTrue(result.contains(id2Double));
    }

    @Test
    void findAllKeysEmptyRepoReturnsEmptyList() {
        // Arrange
        MemGenreRepo repo = new MemGenreRepo();

        // SUT
        MemGenreRepo _sut = repo;

        // Act
        List<GenreId> result = _sut.findAllKeys();

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
        MemGenreRepo _sut = new MemGenreRepo();
        _sut.save(genreDouble);

        // Act
        Optional<Genre> result = _sut.ofIdentity(genreIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(genreDouble, result.get());
    }

    @Test
    void ofIdentityNonExistingGenreIdReturnsEmpty() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        // SUT
        MemGenreRepo _sut = new MemGenreRepo();

        // Act
        Optional<Genre> result = _sut.ofIdentity(genreIdDouble);

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
        MemGenreRepo _sut = new MemGenreRepo();
        _sut.save(genreDouble);

        // Act
        boolean result = _sut.containsOfIdentity(genreIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void containsOfIdentityNonExistingGenreIdReturnsFalse() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        // SUT
        MemGenreRepo _sut = new MemGenreRepo();

        // Act
        boolean result = _sut.containsOfIdentity(genreIdDouble);

        // Assert
        assertFalse(result);
    }
}