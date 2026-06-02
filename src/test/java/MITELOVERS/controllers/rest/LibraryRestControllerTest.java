package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.dto.response.LibraryItemDetailsDTO;
import MITELOVERS.dto.response.LibraryItemSummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryRestController.class)
@Import(CustomRestExceptionHandler.class)
class LibraryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibraryService libraryService;

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
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary("invalid-email"))
                .thenThrow(new IllegalArgumentException("Invalid email"));

        // Act
        var result = mockMvc.perform(get("/my-library/")
                .header("X-User-Id", "invalid-email"));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenHeaderIsMissing() throws Exception {

        // Act
        var result = mockMvc.perform(get("/my-library/"));

        // Assert
        result.andExpect(status().isBadRequest());
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
    void shouldReturn404WhenItemNotFound() throws Exception {
        // Arrange
        when(libraryService.getItemDetail(any()))
                .thenThrow(new IllegalStateException("Item not found!"));

        // Act
        var result = mockMvc.perform(get("/my-library/INVALID-ID"));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn201WhenItemAddedToLibrary() throws Exception {
        // Arrange
        // Act
        var result = mockMvc.perform(post("/my-library/")
                .header("X-User-Id", "pedro@aeiou.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"itemId\": \"3C5D126F8B\"}"));

        // Assert
        result.andExpect(status().isCreated());
    }

    @Test
    void shouldReturn409WhenItemAlreadyInLibrary() throws Exception {
        // Arrange
        doThrow(new IllegalStateException("Item already exists in library"))
                .when(libraryService).addItemToLibrary(any(), any());

        // Act
        var result = mockMvc.perform(post("/my-library/")
                .header("X-User-Id", "pedro@aeiou.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"itemId\": \"3C5D126F8B\"}"));

        // Assert
        result.andExpect(status().isConflict());
    }
}