package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.dto.LibraryItemDetailsDTO;
import MITELOVERS.dto.LibraryItemSummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryRestController.class)
@Import(CustomRestExceptionHandler.class)
class LibraryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
        var result = mockMvc.perform(get("/my-library/publications")
                .header("X-User-Id", "pedro@aeiou.com"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldReturn200WithEmptyListWhenLibraryIsEmpty() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary(any())).thenReturn(List.of());

        // Act
        var result = mockMvc.perform(get("/my-library/publications")
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
        var result = mockMvc.perform(get("/my-library/publications")
                .header("X-User-Id", "naoexiste@aeiou.com"));

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        // Arrange
        // email inválido lança IllegalArgumentException → 400

        // Act
        var result = mockMvc.perform(get("/my-library/publications")
                .header("X-User-Id", "invalid-email"));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenHeaderIsMissing() throws Exception {
        // Arrange
        // sem header X-User-Id

        // Act
        var result = mockMvc.perform(get("/my-library/publications"));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    // ----------------------------------------------------------------
    // GET /my-library/publications/{itemId}
    // ----------------------------------------------------------------

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
        var result = mockMvc.perform(get("/my-library/publications/3C5D126F8B"));

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenItemNotFound() throws Exception {
        // Arrange
        when(libraryService.getItemDetail(any()))
                .thenThrow(new IllegalStateException("Item not found!"));

        // Act
        var result = mockMvc.perform(get("/my-library/publications/INVALID-ID"));

        // Assert
        result.andExpect(status().isNotFound());
    }
}