package MITELOVERS.security;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.user.User;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OWASP A01:2021 — Broken Access Control
 *
 * The ListOfItemsRestController now enforces authorization via AuthorizationPolicy
 * and requires the X-User-Id header. These tests verify that protection:
 * authorized callers succeed, unauthorized callers receive 403, and requests
 * missing the identity header receive 400.
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

    @MockitoBean
    private AuthorizationPolicy _authorizationPolicy;


    @Test
    @DisplayName("DELETE /my-lists/{listId} without X-User-Id header returns 400")
    void deleteList_withoutIdentityHeader_returnsBadRequest() throws Exception {
        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /my-lists/{listId} by unauthorized caller returns 403")
    void deleteList_unauthorizedCaller_isForbidden() throws Exception {
        when(_userService.getUserByEmail(any())).thenReturn(mock(User.class));
        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));
        when(_authorizationPolicy.canDeleteList(any(), any())).thenReturn(false);

        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "attacker@example.com"))
                .andExpect(status().isForbidden());

        verify(_listService, never()).deleteList(any());
    }

    @Test
    @DisplayName("DELETE /my-lists/{listId} by authorized owner succeeds")
    void deleteList_authorizedOwner_succeeds() throws Exception {
        when(_userService.getUserByEmail(any())).thenReturn(mock(User.class));
        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));
        when(_authorizationPolicy.canDeleteList(any(), any())).thenReturn(true);
        doNothing().when(_listService).deleteList(any());

        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "owner@example.com"))
                .andExpect(status().isOk());

        verify(_listService).deleteList(argThat(id -> id.toString().equals("LOI-1234")));
    }

    @Test
    @DisplayName("PATCH visibility (make public) without X-User-Id header returns 400")
    void makeListPublic_withoutIdentityHeader_returnsBadRequest() throws Exception {
        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(new MakeListPublicRequestDTO(7))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH visibility (make public) by authorized owner succeeds")
    void makeListPublic_authorizedOwner_succeeds() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response = new ListOfItemsResponseDTO(
                "LOI-1234", "owner@example.com", "Favourites", "fiction", false, null, List.of());
        when(_userService.getUserByEmail(any())).thenReturn(mock(User.class));
        when(_listService.getListById(any())).thenReturn(domainList);
        when(_authorizationPolicy.canChangeVisibility(any(), any())).thenReturn(true);
        when(_listService.makePublic(any(), any())).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .header("X-User-Id", "owner@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(new MakeListPublicRequestDTO(7))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH visibility (make private) without X-User-Id header returns 400")
    void makeListPrivate_withoutIdentityHeader_returnsBadRequest() throws Exception {
        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /my-lists/{listId} (add item) without X-User-Id header returns 400")
    void addItemToList_withoutIdentityHeader_returnsBadRequest() throws Exception {
        _mockMvc.perform(post("/my-lists/{listId}", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(new AddItemRequestDTO("ABCDEF1234"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /my-lists/{listId} (add item) by authorized owner succeeds")
    void addItemToList_authorizedOwner_succeeds() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response = new ListOfItemsResponseDTO(
                "LOI-1234", "owner@example.com", "Favourites", "fiction", true, null, List.of("ITEM-001"));
        when(_userService.getUserByEmail(any())).thenReturn(mock(User.class));
        when(_listService.getListById(any())).thenReturn(domainList);
        when(_authorizationPolicy.canAddItemTo(any(), any())).thenReturn(true);
        when(_listService.addItemToList(any(), any())).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(post("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "owner@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(new AddItemRequestDTO("ABCDEF1234"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /my-lists/{listId} by unauthorized caller returns 403")
    void getListById_unauthorizedCaller_isForbidden() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        when(_userService.getUserByEmail(any())).thenReturn(mock(User.class));
        when(_listService.getListById(any())).thenReturn(domainList);
        when(_authorizationPolicy.canSeeList(any(), any())).thenReturn(false);

        _mockMvc.perform(get("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "attacker@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /my-lists/{listId} without header and unauthorized returns 403")
    void getListById_withoutHeader_isForbidden() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        when(_listService.getListById(any())).thenReturn(domainList);
        when(_authorizationPolicy.canSeeList(any(), any())).thenReturn(false);

        _mockMvc.perform(get("/my-lists/{listId}", "LOI-5678")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}