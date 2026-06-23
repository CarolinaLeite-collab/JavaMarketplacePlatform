package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.GenreService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.GenreLinkProvider;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.GenreResponseDTO;
import MITELOVERS.mapper.GenreResponseDTOMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@WebMvcTest(GenreRestController.class)
@Import(CustomRestExceptionHandler.class)
class GenreRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @MockitoBean
    private GenreService _genreService;

    @MockitoBean
    private GenreLinkProvider _genreLinkProvider;

    @MockitoBean
    private UserService _userService;

    @MockitoBean
    private GenreResponseDTOMapper _genreResponseDTOMapper;

    @Test
    void optionsReturnsAvailableLinksForUser() throws Exception {
        String email = "user@example.com";
        User mockUser = mock(User.class);
        Link sampleLink = Link.of("http://localhost/genres", "genres");

        when(_userService.getUserByEmail(new MITELOVERS.domain.valueobject.UserId(new MITELOVERS.domain.valueobject.Email(email)))).thenReturn(mockUser);
        when(_genreLinkProvider.getLinks(mockUser)).thenReturn(List.of(sampleLink));

        _mockMvc.perform(options("/genres").param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.genres.href").value("http://localhost/genres"));
    }

    @Test
    void registerGenreAndReturnDTO() throws Exception {
        // Arrange
        Genre mockGenre = mock(Genre.class);
        GenreResponseDTO dto = new GenreResponseDTO("SAMPLE", "Sample");

        when(_genreService.registerGenre("Sample")).thenReturn(mockGenre);
        when(_genreResponseDTOMapper.toModel(mockGenre)).thenReturn(dto);

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
        Genre mockGenre = mock(Genre.class);
        GenreResponseDTO dto = new GenreResponseDTO("SAMPLE", "Sample");

        when(_genreService.getAllGenres()).thenReturn(List.of(mockGenre));
        when(_genreResponseDTOMapper.toModel(mockGenre)).thenReturn(dto);

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
        Genre mockGenre = mock(Genre.class);
        GenreResponseDTO dto = new GenreResponseDTO(genreId, "Sample");

        when(_genreService.getGenreById(genreId)).thenReturn(Optional.of(mockGenre));
        when(_genreResponseDTOMapper.toModel(mockGenre)).thenReturn(dto);

        // Act & Assert
        _mockMvc.perform(get("/genres/{id}", genreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genreId").value("SAMPLE"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/genres/SAMPLE"));
    }
    @Test
    void getGenreByIdThrowsExceptionWhenNotFound() throws Exception {
        // Arrange
        String genreId = "NON-EXISTENT";

        when(_genreService.getGenreById(genreId)).thenReturn(Optional.empty());

        // Act & Assert
        _mockMvc.perform(get("/genres/{id}", genreId))
                .andExpect(status().isNotFound());
    }
}