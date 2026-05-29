package MITELOVERS.applicationservices;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.dto.GenreResponseDTO;
import MITELOVERS.mapper.GenreResponseDTOMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @InjectMocks
    private GenreService _genreService;

    @Mock
    private IGenreRepo _iGenreRepo;

    @Mock
    private GenreFactory _genreFactory;

    @Mock
    private GenreResponseDTOMapper _genreResponseDTOMapper;

    @Test
    void registerGenreReturnsResponseDTO() {
        // Arrange
        String genreName = "Sample";
        Genre genreDouble = mock(Genre.class);
        GenreId genreIdDouble = mock(GenreId.class);
        GenreResponseDTO responseDTODouble = mock(GenreResponseDTO.class);

        when(_genreFactory.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepo.containsOfIdentity(genreIdDouble)).thenReturn(false);
        when(_iGenreRepo.save(genreDouble)).thenReturn(genreDouble);
        when(_genreResponseDTOMapper.toResponseDTO(genreDouble)).thenReturn(responseDTODouble);

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

        when(_genreFactory.createGenre(genreName)).thenReturn(genreDouble);
        when(genreDouble.identity()).thenReturn(genreIdDouble);
        when(_iGenreRepo.containsOfIdentity(genreIdDouble)).thenReturn(true);

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

        when(_iGenreRepo.findAll()).thenReturn(List.of(genreOne, genreTwo));
        when(_genreResponseDTOMapper.toResponseDTO(genreOne)).thenReturn(dtoOne);
        when(_genreResponseDTOMapper.toResponseDTO(genreTwo)).thenReturn(dtoTwo);

        // Act
        List<GenreResponseDTO> result = _genreService.getAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertSame(dtoOne, result.get(0));
        assertSame(dtoTwo, result.get(1));
    }
}
