package MITELOVERS.controllers.cli;

import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.dto.response.GenreResponseDTO;
import MITELOVERS.mapper.GenreResponseDTOMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AddGenreControllerTest {

    @InjectMocks
    private AddGenreController _controller;

    @Mock
    private GenreService _genreService;

    @Mock
    private GenreResponseDTOMapper _genreResponseDTOMapper;

    @Test
    void addGenreReturnsResponseDTOFromService() {
        // Arrange
        String genreName = "Sample";
        Genre mockGenre = mock(Genre.class);
        GenreResponseDTO responseDTODouble = new GenreResponseDTO("SAMPLE", "Sample");

        when(_genreService.registerGenre(genreName)).thenReturn(mockGenre);
        when(_genreResponseDTOMapper.toModel(mockGenre)).thenReturn(responseDTODouble);

        // Act
        GenreResponseDTO result = _controller.addGenre(genreName);

        // Assert
        assertSame(responseDTODouble, result);
        verify(_genreService).registerGenre(genreName);
        verify(_genreResponseDTOMapper).toModel(mockGenre);
    }

    @Test
    void addGenreThrowsWhenServiceFails() {
        // Arrange
        String genreName = "Sample";

        when(_genreService.registerGenre(genreName))
                .thenThrow(new IllegalStateException("Genre already exists in the repository"));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> _controller.addGenre(genreName));

        assertEquals("Genre already exists in the repository", exception.getMessage());
        verify(_genreService).registerGenre(genreName);
    }
}