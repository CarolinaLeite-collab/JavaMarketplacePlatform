package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.LibraryLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.LibraryItemDetailsDTO;
import MITELOVERS.dto.response.LibraryItemSummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Import;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryRestController.class)
@Import(CustomRestExceptionHandler.class)
class LibraryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibraryService libraryService;

    @MockitoBean
    private LibraryLinkProvider libraryLinkProvider;

    @MockitoBean
    private UserService userService;


    @Test
    void shouldReturn200WithItemsWhenLibraryExists() throws Exception {
        // Arrange
        LibraryItemSummaryDTO dto = new LibraryItemSummaryDTO(
                "3C5D126F8B",
                "1984",
                "https://example.com/1984.jpg"
        );
        when(libraryService.getListOfItemInfoInMyLibrary(any())).thenReturn(List.of(dto));

        // Act
        var result = mockMvc.perform(get("/my-library/")
                .header("X-User-Id", "pedro@aeiou.com"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._embedded.libraryItemSummaryDTOList[0]._links.self").exists());
    }

    @Test
    void shouldReturn200WithEmptyListWhenLibraryIsEmpty() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary(any())).thenReturn(List.of());

        // Act
        var result = mockMvc.perform(get("/my-library/")
                .header("X-User-Id", "pedro@aeiou.com"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldReturn200WithEmptyListWhenNoLibraryExists() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary(any())).thenReturn(List.of());

        // Act
        var result = mockMvc.perform(get("/my-library/")
                .header("X-User-Id", "naoexiste@aeiou.com"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }


    @Test
    void shouldReturn200WithItemDetailsWhenItemExists() throws Exception {
        // Arrange
        LibraryItemDetailsDTO dto = new LibraryItemDetailsDTO(
                "George Orwell",
                "no identifier",
                "BOOK"
        );
        when(libraryService.getItemDetail(any())).thenReturn(dto);

        // Act
        var result = mockMvc.perform(get("/my-library/3C5D126F8B"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }


    @Test
    void shouldReturn201WhenItemAddedToLibrary() throws Exception {
        // Arrange
        LibraryItemSummaryDTO dto = new LibraryItemSummaryDTO(
                "3C5D126F8B",
                "1984",
                "https://example.com/1984.jpg"
        );
        when(libraryService.addItemToLibrary(any(), any())).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(post("/my-library/")
                        .header("X-User-Id", "pedro@aeiou.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": \"3C5D126F8B\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self").exists());
    }


    @Test
    void optionsShouldReturn200WithLinksForAuthorizedUser() throws Exception {
        // Arrange
        User _userDouble = mock(User.class);
        when(userService.getUserByEmail("pedro@aeiou.com")).thenReturn(_userDouble);
        when(libraryLinkProvider.getLinks(_userDouble)).thenReturn(List.of(
                Link.of("/my-library/").withRel("library"),
                Link.of("/my-library/").withRel("library-add")
        ));

        // Act & Assert
        mockMvc.perform(options("/my-library")
                        .param("email", "pedro@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.library").exists())
                .andExpect(jsonPath("$._links.library-add").exists());
    }

    @Test
    void optionsShouldReturn200WithNoLinksForUnauthorizedUser() throws Exception {
        // Arrange
        User _userDouble = mock(User.class);
        when(userService.getUserByEmail("readonly@aeiou.com")).thenReturn(_userDouble);
        when(libraryLinkProvider.getLinks(_userDouble)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(options("/my-library")
                        .param("email", "readonly@aeiou.com")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldReturn400WhenHeaderIsMissing() throws Exception {

        // Act
        var result = mockMvc.perform(get("/my-library/"));

        // Assert
        result.andExpect(status().isBadRequest());
    }

}