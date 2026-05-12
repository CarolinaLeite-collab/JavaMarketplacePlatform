package MITELOVERS.controller;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AddGenreControllerTest {

    @InjectMocks
    private AddGenreController _sut;

    @Mock
    private IGenreRepo _iGenreRepoDouble;

    @Mock
    private GenreFactory _genreFactoryDouble;


    @Test
    void addGenreShouldReturnGenreFromRepo() {
        // Arrange
        String genreName = "Action";
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(false);
        when(_iGenreRepoDouble.save(genreDouble)).thenReturn(genreDouble);

        // Act
        Genre genreAdded = _sut.addGenre(genreName);

        // Assert
        assertNotNull(genreAdded);
        assertEquals(genreDouble, genreAdded);
        verify(_iGenreRepoDouble).save(genreDouble);
    }

    @Test
    void addGenreThrowsWhenAlreadyExistsInRepo() {
        // Arrange
        String genreName = "Action";
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(true);

        // Act // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> _sut.addGenre(genreName));

        assertEquals("Genre already exists in the repository", exception.getMessage());
        verify(_iGenreRepoDouble, never()).save(any());
    }
}