package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListOfItemsRestController.class)
class ListOfItemsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ListOfItemsService listService;

    @Test
    void getLists_returnsOkWithBodyAndSelfLinks_whenListsExist() throws Exception {
        // Arrange
        ListOfItemsResponseDTO first =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favourites", "Fiction", true, null, List.of());
        ListOfItemsResponseDTO second =
                new ListOfItemsResponseDTO("LOI-1235", "user@cenas.com", "TBR", "Non-Fiction", false, null, List.of());

        when(listService.getUserLists("user@cenas.com"))
                .thenReturn(List.of(first, second));

        // Act + Assert
        mockMvc.perform(get("/my-lists/")
                        .header("X-User-Id", "user@cenas.com")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].listId").value("LOI-1234"))
                .andExpect(jsonPath("$[1].listId").value("LOI-1235"))
                .andExpect(jsonPath("$[0].links[0].href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$[1].links[0].href").value("http://localhost/my-lists/LOI-1235"));
    }

    @Test
    void getLists_returnsNoContent_whenServiceReturnsEmptyList() throws Exception {
        // Arrange
        when(listService.getUserLists("user@cenas.com")).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/my-lists/")
                        .header("X-User-Id", "user@cenas.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getLists_returnsBadRequest_whenHeaderMissing() throws Exception {
        // Arrange – nothing

        // Act + Assert
        mockMvc.perform(get("/my-lists/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getListById_returnsOkWithSelfLink_whenServiceReturnsDto() throws Exception {
        // Arrange
        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", true, null, List.of());

        when(listService.getListById("LOI-1234")).thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/my-lists/{listId}", "LOI-1234")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getListById_returnsNotFound_whenServiceThrows() throws Exception {
        // Arrange
        when(listService.getListById("LOI-1234"))
                .thenThrow(new RuntimeException("not found"));

        // Act + Assert
        mockMvc.perform(get("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAndSaveList_returnsCreatedWithSelfLink_whenServiceSucceeds() throws Exception {
        // Arrange
        ListOfItemsRequestDTO request =
                new ListOfItemsRequestDTO("Favorites", "genre-1");

        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", true, null, List.of());

        when(listService.save(eq("user@cenas.com"), any(ListOfItemsRequestDTO.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/my-lists/")
                        .header("X-User-Id", "user@cenas.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void createAndSaveList_returnsInternalServerError_whenServiceThrows() throws Exception {
        // Arrange
        ListOfItemsRequestDTO request =
                new ListOfItemsRequestDTO("Favorites", "genre-1");

        when(listService.save(eq("user@cenas.com"), any(ListOfItemsRequestDTO.class)))
                .thenThrow(new RuntimeException("boom"));

        // Act + Assert
        mockMvc.perform(post("/my-lists/")
                        .header("X-User-Id", "user@cenas.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addItemToList_returnsOkWithSelfLink_whenServiceSucceeds() throws Exception {
        // Arrange
        AddItemRequestDTO request =
                new AddItemRequestDTO("ITEM-999");

        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", false, LocalDateTime.of(2026,7, 2, 2, 2), List.of("ABCDEF1234"));

        when(listService.addItemToList(eq("LOI-1234"), any(AddItemRequestDTO.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/my-lists/{listId}", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void addItemToList_returnsInternalServerError_whenServiceThrows() throws Exception {
        // Arrange
        AddItemRequestDTO request =
                new AddItemRequestDTO("ITEM-999");

        when(listService.addItemToList(eq("LOI-1234"), any(AddItemRequestDTO.class)))
                .thenThrow(new RuntimeException("boom"));

        // Act + Assert
        mockMvc.perform(post("/my-lists/{listId}", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void makeListPublic_returnsOkWithSelfLink_whenServiceSucceeds() throws Exception {
        // Arrange
        MakeListPublicRequestDTO request =
                new MakeListPublicRequestDTO(7);

        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", true, null, List.of());

        when(listService.makePublic(eq("LOI-1234"), any(MakeListPublicRequestDTO.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void makeListPublic_returnsInternalServerError_whenServiceThrows() throws Exception {
        // Arrange
        MakeListPublicRequestDTO request =
                new MakeListPublicRequestDTO(7);

        when(listService.makePublic(eq("LOI-1234"), any(MakeListPublicRequestDTO.class)))
                .thenThrow(new RuntimeException("boom"));

        // Act + Assert
        mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void makeListPrivate_returnsOkWithSelfLink_whenServiceSucceeds() throws Exception {
        // Arrange
        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", false, LocalDateTime.of(2026,7, 2, 2, 2), List.of());

        when(listService.makePrivate("LOI-1234"))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void makeListPrivate_returnsInternalServerError_whenServiceThrows() throws Exception {
        // Arrange
        when(listService.makePrivate("LOI-1234"))
                .thenThrow(new RuntimeException("boom"));

        // Act + Assert
        mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteList_returnsOkAndInvokesService() throws Exception {
        // Arrange
        doNothing().when(listService).deleteList("LOI-1234");

        // Act + Assert
        mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isOk());

        verify(listService).deleteList("LOI-1234");
    }

    @Test
    void deleteList_returnsInternalServerError_whenServiceThrows() throws Exception {
        // Arrange
        doThrow(new RuntimeException("boom"))
                .when(listService).deleteList("LOI-1234");

        // Act + Assert
        mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isInternalServerError());
    }
}