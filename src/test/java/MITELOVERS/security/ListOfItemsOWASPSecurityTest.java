package MITELOVERS.security;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OWASP A01:2021 — Broken Access Control
 *
 * Documents IDOR (Insecure Direct Object Reference) vulnerabilities in
 * ListOfItemsRestController. These tests verify the current behavior where
 * list operations accept requests without ownership verification.
 */
@Tag("security")
@WebMvcTest(ListOfItemsRestController.class)
@Import(CustomRestExceptionHandler.class)
class ListOfItemsOWASPSecurityTest {

    @Autowired
    private MockMvc _mockMvc;

    @Autowired
    private ObjectMapper _objectMapper;

    @MockitoBean
    private ListOfItemsService _listService;

    @MockitoBean
    private ListOfItemsResponseDTOMapper _mapper;

    @MockitoBean
    private ListOfItemsLinkProvider _linkProvider;

    @MockitoBean
    private UserService _userService;

    // ── V2: IDOR — DELETE /my-lists/{listId} ──────────────────────────────────

    @Test
    @DisplayName("OWASP A01 IDOR: DELETE /my-lists/{listId} succeeds without X-User-Id header")
    void deleteList_succeedsWithoutIdentityHeader() throws Exception {
        doNothing().when(_listService).deleteList(any());

        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("OWASP A01 IDOR: DELETE /my-lists/{listId} does not verify caller owns the list")
    void deleteList_doesNotPassCallerIdentityToService() throws Exception {
        doNothing().when(_listService).deleteList(any());

        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "attacker@example.com"))
                .andExpect(status().isOk());

        // Service receives only the listId — no UserId is passed for ownership check
        verify(_listService).deleteList(argThat(id -> id.toString().equals("LOI-1234")));
        verify(_listService, never()).getUserLists(any());
    }

    // ── V3: IDOR — PATCH /my-lists/{listId}/visibility ───────────────────────

    @Test
    @DisplayName("OWASP A01 IDOR: PATCH visibility (make public) succeeds without X-User-Id header")
    void makeListPublic_succeedsWithoutIdentityHeader() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response = new ListOfItemsResponseDTO(
                "LOI-1234", "owner@example.com", "Favourites", "fiction", false, null, List.of());
        when(_listService.makePublic(any(), any())).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(new MakeListPublicRequestDTO(7))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("OWASP A01 IDOR: PATCH visibility (make private) succeeds without X-User-Id header")
    void makeListPrivate_succeedsWithoutIdentityHeader() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response = new ListOfItemsResponseDTO(
                "LOI-1234", "owner@example.com", "Favourites", "fiction", true, null, List.of());
        when(_listService.makePrivate(any())).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234"))
                .andExpect(status().isOk());
    }

    // ── V4: IDOR — POST /my-lists/{listId} (add item) ────────────────────────

    @Test
    @DisplayName("OWASP A01 IDOR: POST /my-lists/{listId} adds item to any list without identity check")
    void addItemToList_succeedsWithoutIdentityHeader() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response = new ListOfItemsResponseDTO(
                "LOI-1234", "owner@example.com", "Favourites", "fiction", true, null, List.of("ITEM-001"));
        when(_listService.addItemToList(any(), any())).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(post("/my-lists/{listId}", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(new AddItemRequestDTO("ABCDEF1234"))))
                .andExpect(status().isOk());
    }

    // ── V5: IDOR — GET /my-lists/{listId} exposes private lists ──────────────

    @Test
    @DisplayName("OWASP A01 IDOR: GET /my-lists/{listId} returns private list contents to any caller")
    void getListById_returnsPrivateListToAnyCaller() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response = new ListOfItemsResponseDTO(
                "LOI-1234", "owner@example.com", "Secret Reads", "fiction", true, null,
                List.of("ITEM-001", "ITEM-002"));
        when(_listService.getListById(any())).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);
        when(domainList.isPrivate()).thenReturn(true);

        _mockMvc.perform(get("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "attacker@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$.userId").value("owner@example.com"))
                .andExpect(jsonPath("$.private").value(true));
    }

    @Test
    @DisplayName("OWASP A01 IDOR: GET /my-lists/{listId} returns private list without any X-User-Id header")
    void getListById_returnsPrivateListWithoutAnyHeader() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response = new ListOfItemsResponseDTO(
                "LOI-5678", "victim@example.com", "Private Collection", "fiction", true, null, List.of());
        when(_listService.getListById(any())).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);
        when(domainList.isPrivate()).thenReturn(true);

        _mockMvc.perform(get("/my-lists/{listId}", "LOI-5678")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.private").value(true));
    }
}
