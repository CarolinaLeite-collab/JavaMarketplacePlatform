package MITELOVERS.restcontroller;

import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.controllers.cli.root.CustomRestExceptionHandler;
import MITELOVERS.controllers.rest.LibraryRestController;
import MITELOVERS.dto.ItemDetailsDTO;
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
        ItemDetailsDTO dto = new ItemDetailsDTO("1984", "George Orwell", "Book", "N/A");
        when(libraryService.getListOfItemInfoInMyLibrary(any())).thenReturn(List.of(dto));

        // Act + Assert
        mockMvc.perform(get("/my-library/publications")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.itemDetailsDTOList[0].title").value("1984"))
                .andExpect(jsonPath("$._embedded.itemDetailsDTOList[0].authorName").value("George Orwell"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldReturn200WithEmptyListWhenLibraryIsEmpty() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary(any())).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/my-library/publications")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void shouldReturn422WhenLibraryNotFound() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary(any()))
                .thenThrow(new IllegalStateException("Library not found for user!"));

        // Act + Assert
        mockMvc.perform(get("/my-library/publications")
                        .header("X-User-Id", "naoexiste@aeiou.com"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturn422WhenExceptionOccurs() throws Exception {
        // Arrange
        when(libraryService.getListOfItemInfoInMyLibrary(any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act + Assert
        mockMvc.perform(get("/my-library/publications")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isUnprocessableEntity());
    }
}