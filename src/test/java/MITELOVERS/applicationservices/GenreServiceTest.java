package MITELOVERS.applicationservices;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @InjectMocks
    private GenreService _genreService;

    @Mock
    private IGenreRepo _iGenreRepoDouble;

    @Mock
    private GenreFactory _genreFactoryDouble;

    @Test
    void registerGenreReturnsRawDomainGenre() {
        // Arrange
        String genreName = "Sample";
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(false);
        when(_iGenreRepoDouble.save(genreDouble)).thenReturn(genreDouble);

        // Act
        Genre result = _genreService.registerGenre(genreName);

        // Assert
        assertSame(genreDouble, result);
    }

    @Test
    void registerGenreThrowsWhenDuplicate() {
        // Arrange
        String genreName = "Sample";
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> _genreService.registerGenre(genreName));

        assertEquals("Genre already exists in the repository", exception.getMessage());
    }

    @Test
    void getAllGenresReturnsRawDomainEntities() {
        // Arrange
        Genre genreOne = mock(Genre.class);
        Genre genreTwo = mock(Genre.class);
        List<Genre> genreList = List.of(genreOne, genreTwo);

        when(_iGenreRepoDouble.findAll()).thenReturn(genreList);

        // Act
        Iterable<Genre> result = _genreService.getAllGenres();

        // Assert
        assertSame(genreList, result);
    }

    @Test
    void getGenreByIdReturnsOptionalWithGenreWhenFound() {
        // Arrange
        String idString = "GEN-1";
        Genre genreDouble = mock(Genre.class);

        when(_iGenreRepoDouble.ofIdentity(new GenreId(idString))).thenReturn(Optional.of(genreDouble));

        // Act
        Optional<Genre> result = _genreService.getGenreById(idString);

        // Assert
        assertTrue(result.isPresent());
        assertSame(genreDouble, result.get());
    }

    @Test
    void getGenreByIdReturnsEmptyOptionalWhenNotFound() {
        // Arrange
        String idString = "NON-EXISTENT";

        when(_iGenreRepoDouble.ofIdentity(new GenreId(idString))).thenReturn(Optional.empty());

        // Act
        Optional<Genre> result = _genreService.getGenreById(idString);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllGenreIds() {
        // Arrange
        GenreId genreIdDouble = mock(GenreId.class);
        when(_iGenreRepoDouble.findAllKeys()).thenReturn(List.of(genreIdDouble));

        // Act
        Iterable<GenreId> result = _genreService.getGenresId();

        List<GenreId> ids = new ArrayList<>();
        result.forEach(ids::add);

        // Assert
        assertEquals(1, ids.size());
        assertEquals(genreIdDouble, ids.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenNoGenreIdsExist() {
        // Arrange
        when(_iGenreRepoDouble.findAllKeys()).thenReturn(List.of());

        // Act
        Iterable<GenreId> result = _genreService.getGenresId();

        List<GenreId> ids = new ArrayList<>();
        result.forEach(ids::add);

        // Assert
        assertEquals(0, ids.size());
    }
}