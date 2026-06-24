package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.AuthorService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.AuthorLinkProvider;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.AuthorResponseDTO;
import MITELOVERS.mapper.AuthorResponseDTOMapper;
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
@WebMvcTest(AuthorRestController.class)
@Import(CustomRestExceptionHandler.class)
class AuthorRestControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    @MockitoBean
    private AuthorService _authorService;

    @MockitoBean
    private AuthorLinkProvider _authorLinkProvider;

    @MockitoBean
    private UserService _userService;

    @MockitoBean
    private AuthorResponseDTOMapper _authorResponseDTOMapper;

    @Test
    void optionsReturnsAvailableLinksForUser() throws Exception {
        // Arrange
        String email = "user@example.com";
        User mockUser = mock(User.class);
        Link sampleLink = Link.of("http://localhost/authors", "authors");

        when(_userService.getUserByEmail(new UserId(new Email(email)))).thenReturn(mockUser);
        when(_authorLinkProvider.getLinks(mockUser)).thenReturn(List.of(sampleLink));

        // Act & Assert
        _mockMvc.perform(options("/authors").param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.authors.href").value("http://localhost/authors"));
    }

    @Test
    void registerAuthorAndReturnDTO() throws Exception {
        // Arrange
        Author mockAuthor = mock(Author.class);
        AuthorResponseDTO dto = new AuthorResponseDTO("SAMPLE", "Sample Name");

        when(_authorService.registerAuthor("Sample Name")).thenReturn(mockAuthor);
        when(_authorResponseDTOMapper.toModel(mockAuthor)).thenReturn(dto);

        // Act & Assert
        _mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"authorName\":\"Sample Name\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorId").value("SAMPLE"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/authors/SAMPLE"));
    }

    @Test
    void getAllAuthorsReturnsList() throws Exception {
        // Arrange
        Author mockAuthor = mock(Author.class);
        AuthorResponseDTO dto = new AuthorResponseDTO("SAMPLE", "Sample Name");

        when(_authorService.getAllAuthors()).thenReturn(List.of(mockAuthor));
        when(_authorResponseDTOMapper.toModel(mockAuthor)).thenReturn(dto);

        // Act & Assert
        _mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorId").value("SAMPLE"))
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
        Author mockAuthor = mock(Author.class);
        AuthorResponseDTO dto = new AuthorResponseDTO(authorId, "Sample Name");

        when(_authorService.getAuthorById(authorId)).thenReturn(Optional.of(mockAuthor));
        when(_authorResponseDTOMapper.toModel(mockAuthor)).thenReturn(dto);

        // Act & Assert
        _mockMvc.perform(get("/authors/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorId").value("SAMPLE"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/authors/SAMPLE"));
    }

    @Test
    void getAuthorByIdThrowsExceptionWhenNotFound() throws Exception {
        // Arrange
        String authorId = "NON-EXISTENT";

        when(_authorService.getAuthorById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        _mockMvc.perform(get("/authors/{id}", authorId))
                .andExpect(status().isNotFound());
    }
}