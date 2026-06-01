package MITELOVERS.applicationservices;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.dto.response.GenreResponseDTO;
import MITELOVERS.mapper.GenreResponseDTOMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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

    @Mock
    private GenreResponseDTOMapper _genreResponseDTOMapperDouble;

    @Test
    void registerGenreReturnsResponseDTO() {
        // Arrange
        String genreName = "Sample";
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);
        GenreResponseDTO responseDTODouble = mock(GenreResponseDTO.class);

        when(_genreFactoryDouble.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepoDouble.containsOfIdentity(genreIdDouble)).thenReturn(false);
        when(_iGenreRepoDouble.save(genreDouble)).thenReturn(genreDouble);
        when(_genreResponseDTOMapperDouble.toModel(genreDouble)).thenReturn(responseDTODouble);

        // Act
        GenreResponseDTO result = _genreService.registerGenre(genreName);

        // Assert
        assertSame(responseDTODouble, result);
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
    void getAllGenresReturnsMappedList() {
        // Arrange
        Genre genreOne = mock(Genre.class);
        Genre genreTwo = mock(Genre.class);
        GenreResponseDTO dtoOne = mock(GenreResponseDTO.class);
        GenreResponseDTO dtoTwo = mock(GenreResponseDTO.class);

        when(_iGenreRepoDouble.findAll()).thenReturn(List.of(genreOne, genreTwo));
        when(_genreResponseDTOMapperDouble.toModel(genreOne)).thenReturn(dtoOne);
        when(_genreResponseDTOMapperDouble.toModel(genreTwo)).thenReturn(dtoTwo);

        // Act
        List<GenreResponseDTO> result = _genreService.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertSame(dtoOne, result.get(0));
        assertSame(dtoTwo, result.get(1));
    }

    @Test
    void shouldReturnAllGenreIds(){
        //Arrange
        GenreId genreIdDouble = mock(GenreId.class);

        when(_iGenreRepoDouble.findAllKeys()).thenReturn(List.of(genreIdDouble));

        //Act
        Iterable<GenreId> result = _genreService.getGenresId();

        List<GenreId> ids = new ArrayList<>();
        for (GenreId genreId : result) {
            ids.add(genreId);
        }

        //Assert
        assertEquals(genreIdDouble, ids.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenNoGenreIdsExist() {
        //Arrange
        when(_iGenreRepoDouble.findAllKeys()).thenReturn(List.of());

        //Act
        Iterable<GenreId> result = _genreService.getGenresId();

        List<GenreId> ids = new ArrayList<>();
        for (GenreId genreId : result) {
            ids.add(genreId);
        }

        //Assert
        assertEquals(0, ids.size());
    }
}
