package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ItemService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ItemRestController.class)
@Import(CustomRestExceptionHandler.class)
class ItemRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

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
}