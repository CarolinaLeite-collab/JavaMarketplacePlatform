package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.dto.AuthorResponseDTO;
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
@WebMvcTest(AuthorRestController.class)
@Import(CustomRestExceptionHandler.class)
class AuthorRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @Autowired
    private AuthorRestController _controller;

    @MockBean
    private AuthorService _authorService;

    @Test
    void registerAuthorAndReturnDTO() throws Exception {
        // Arrange
        AuthorResponseDTO dto = new AuthorResponseDTO("SAMPLE", "Sample Name");

        when(_authorService.registerAuthor("Sample Name")).thenReturn(dto);

        // Act & Assert
        _mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"authorName\":\"Sample Name\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorId").value("SAMPLE"))
                .andExpect(jsonPath("$.authorName").value("Sample Name"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/authors/SAMPLE"));
    }

    @Test
    void getAllAuthorsReturnsList() throws Exception {
        // Arrange
        AuthorResponseDTO dto = new AuthorResponseDTO("SAMPLE", "Sample Name");

        when(_authorService.getAllAuthors()).thenReturn(List.of(dto));

        // Act & Assert
        _mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorId").value("SAMPLE"))
                .andExpect(jsonPath("$[0].authorName").value("Sample Name"))
                .andExpect(jsonPath("$[0].links[0].href").value("http://localhost/authors/SAMPLE"));
    }

    @Test
    void getAllAuthorsReturnsNoContentWhenEmpty() throws Exception {
        // Arrange
        when(_authorService.getAllAuthors()).thenReturn(List.of());

        // Act & Assert
        _mockMvc.perform(get("/authors"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAuthorByIdReturnsOkResponse() throws Exception {
        // Arrange
        String authorId = "SAMPLE";

        // Act & Assert
        _mockMvc.perform(get("/authors/{id}", authorId))
                .andExpect(status().isOk());
    }

    @Test
    void getAuthorByIdReturnsResponseEntity() {
        // Arrange
        String authorId = "SAMPLE";

        // Act
        ResponseEntity<AuthorResponseDTO> response = _controller.getAuthorById(authorId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
