package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ItemService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.dto.ItemResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRestController.class)
@Import(CustomRestExceptionHandler.class)
class ItemRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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

        // Act
        var result = mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "editionId": "E-ABCDEF12",
                            "condition": "GOOD",
                            "description": "Nice copy"
                        }
                        """));

        // Assert
        result.andExpect(status().isCreated());
    }


    @Test
    void shouldReturn404WhenEditionNotFound() throws Exception {
        // Arrange
        when(itemService.registerItem(any(), any(), any()))
                .thenThrow(new NoSuchElementException("Edition not found"));

        // Act
        var result = mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "editionId": "E-INVALID",
                            "condition": "GOOD",
                            "description": "Nice copy"
                        }
                        """));

        // Assert
        result.andExpect(status().isNotFound());
    }

    // ----------------------------------------------------------------
    // GET /items
    // ----------------------------------------------------------------

    @Test
    void shouldReturn200WithListOfItems() throws Exception {
        // Arrange
        when(itemService.getAllItems()).thenReturn(List.of());

        // Act
        var result = mockMvc.perform(get("/items"));

        // Assert
        result.andExpect(status().isOk());
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

        // Act
        var result = mockMvc.perform(get("/items/3C5D126F8B"));

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenItemNotFound() throws Exception {
        // Arrange
        when(itemService.getItemById(any()))
                .thenThrow(new NoSuchElementException("Item not found"));

        // Act
        var result = mockMvc.perform(get("/items/INVALID"));

        // Assert
        result.andExpect(status().isNotFound());
    }
}
