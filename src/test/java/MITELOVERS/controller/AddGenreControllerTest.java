package MITELOVERS.controller;

import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.controllers.cli.AddGenreController;
import MITELOVERS.dto.GenreResponseDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AddGenreControllerTest {

    @InjectMocks
    private AddGenreController _sut;

    @Mock
    private GenreService _genreService;

    @Test
    void addGenreReturnsResponseDTOFromService() {
        // Arrange
        String genreName = "Sample";
        GenreResponseDTO responseDTODouble = new GenreResponseDTO("SAMPLE", "Sample");

        when(_genreService.registerGenre(genreName)).thenReturn(responseDTODouble);

        // Act
        GenreResponseDTO result = _sut.addGenre(genreName);

        // Assert
        assertSame(responseDTODouble, result);
        verify(_genreService).registerGenre(genreName);
    }

    @Test
    void addGenreThrowsWhenServiceFails() {
        // Arrange
        String genreName = "Sample";

        when(_genreService.registerGenre(genreName))
                .thenThrow(new IllegalStateException("Genre already exists in the repository"));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> _sut.addGenre(genreName));

        assertEquals("Genre already exists in the repository", exception.getMessage());
        verify(_genreService).registerGenre(genreName);
    }
}