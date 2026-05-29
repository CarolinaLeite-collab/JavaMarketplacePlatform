package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.dto.GenreResponseDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@WebMvcTest(GenreRestController.class)
@Import(CustomRestExceptionHandler.class)
class GenreRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @Autowired
    private GenreRestController _controller;

    @MockBean
    private GenreService _genreService;

    @Test
    void registerGenreAndReturnDTO() throws Exception {
        // Arrange
        GenreResponseDTO dto = new GenreResponseDTO("SAMPLE", "Sample");

        when(_genreService.registerGenre("Sample")).thenReturn(dto);

        // Act & Assert
        _mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"genreName\":\"Sample\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.genreId").value("SAMPLE"))
                .andExpect(jsonPath("$.genreName").value("Sample"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/genres/SAMPLE"));
    }

    @Test
    void getAllGenresReturnsList() throws Exception {
        // Arrange
        GenreResponseDTO dto = new GenreResponseDTO("SAMPLE", "Sample");

        when(_genreService.getAllGenres()).thenReturn(List.of(dto));

        // Act & Assert
        _mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genreId").value("SAMPLE"))
                .andExpect(jsonPath("$[0].genreName").value("Sample"))
                .andExpect(jsonPath("$[0].links[0].href").value("http://localhost/genres/SAMPLE"));
    }

    @Test
    void getAllGenresReturnsNoContentWhenEmpty() throws Exception {
        // Arrange
        when(_genreService.getAllGenres()).thenReturn(List.of());

        // Act & Assert
        _mockMvc.perform(get("/genres"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getGenreByIdReturnsOkResponse() throws Exception {
        // Arrange
        String genreId = "SAMPLE";

        // Act & Assert
        _mockMvc.perform(get("/genres/{id}", genreId))
                .andExpect(status().isOk());
    }

    @Test
    void getGenreByIdReturnsResponseEntity() {
        // Arrange
        String genreId = "SAMPLE";

        // Act
        ResponseEntity<GenreResponseDTO> response = _controller.getGenreById(genreId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
