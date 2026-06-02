package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ItemService;
import MITELOVERS.applicationservices.LibraryService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.response.ItemResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRestController.class)
@Import(CustomRestExceptionHandler.class)
class ItemRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private LibraryService libraryService;

    // ----------------------------------------------------------------
    // POST /items
    // ----------------------------------------------------------------

    @Test
    void shouldReturn201WhenItemIsCreated() throws Exception {
        // Arrange
        ItemResponseDTO dto = new ItemResponseDTO(
                "3C5D126F8B", "GOOD", "Nice copy", "NotOnSale",
                "E-ABCDEF12", "no identifier", "ENGLISH", 1949, "BOOK",
                "1984", "George Orwell", 1949, "Fiction"
        );
        when(itemService.registerItem(any(), any(), any())).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "editionId": "E-ABCDEF12",
                                    "condition": "GOOD",
                                    "description": "Nice copy"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn422WhenConditionIsInvalid() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "editionId": "E-ABCDEF12",
                                    "condition": "AMAZING",
                                    "description": "Nice copy"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturn404WhenEditionNotFound() throws Exception {
        // Arrange
        when(itemService.registerItem(any(), any(), any()))
                .thenThrow(new NoSuchElementException("Edition not found"));

        // Act + Assert
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "editionId": "E-INVALID",
                                    "condition": "GOOD",
                                    "description": "Nice copy"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // ----------------------------------------------------------------
    // GET /items
    // ----------------------------------------------------------------

    @Test
    void shouldReturn200WithListOfItems() throws Exception {
        // Arrange
        ItemResponseDTO dto = new ItemResponseDTO(
                "3C5D126F8B", "GOOD", "Nice copy", "NotOnSale",
                "E-ABCDEF12", "no identifier", "ENGLISH", 1949, "BOOK",
                "1984", "George Orwell", 1949, "Fiction"
        );
        when(itemService.getAllItems()).thenReturn(List.of(dto));

        // Act + Assert
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ----------------------------------------------------------------
    // GET /items/{id}
    // ----------------------------------------------------------------

    @Test
    void shouldReturn200WhenItemExists() throws Exception {
        // Arrange
        ItemResponseDTO dto = new ItemResponseDTO(
                "3C5D126F8B", "GOOD", "Nice copy", "NotOnSale",
                "E-ABCDEF12", "no identifier", "ENGLISH", 1949, "BOOK",
                "1984", "George Orwell", 1949, "Fiction"
        );
        when(itemService.getItemById(any())).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/items/3C5D126F8B"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void shouldReturn404WhenItemNotFound() throws Exception {
        // Arrange
        when(itemService.getItemById(any()))
                .thenThrow(new NoSuchElementException("Item not found"));

        // Act + Assert
        mockMvc.perform(get("/items/INVALID"))
                .andExpect(status().isNotFound());
    }

    // ----------------------------------------------------------------
    // GET /items/my-library
    // ----------------------------------------------------------------

    @Test
    void shouldReturn200WithItemsFromMyLibrary() throws Exception {
        // Arrange
        ItemResponseDTO dto = new ItemResponseDTO(
                "3C5D126F8B", "GOOD", "Nice copy", "NotOnSale",
                "E-ABCDEF12", "no identifier", "ENGLISH", 1949, "BOOK",
                "1984", "George Orwell", 1949, "Fiction"
        );
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.getValue()).thenReturn("3C5D126F8B");
        when(libraryService.getItemIdsInLibrary(any())).thenReturn(List.of(itemIdDouble));
        when(itemService.getItemById("3C5D126F8B")).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/items/my-library")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").isMap());
    }

    @Test
    void shouldReturn200WithEmptyListWhenLibraryIsEmpty() throws Exception {
        // Arrange
        when(libraryService.getItemIdsInLibrary(any())).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/items/my-library")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturn404WhenItemInLibraryNotFound() throws Exception {
        // Arrange
        ItemId itemIdDouble = mock(ItemId.class);
        when(itemIdDouble.getValue()).thenReturn("INVALID-ID");
        when(libraryService.getItemIdsInLibrary(any())).thenReturn(List.of(itemIdDouble));
        when(itemService.getItemById("INVALID-ID"))
                .thenThrow(new NoSuchElementException("Item not found"));

        // Act + Assert
        mockMvc.perform(get("/items/my-library")
                        .header("X-User-Id", "pedro@aeiou.com"))
                .andExpect(status().isNotFound());
    }
}