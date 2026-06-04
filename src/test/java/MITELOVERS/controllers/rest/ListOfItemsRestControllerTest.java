package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListOfItemsRestController.class)
class ListOfItemsRestControllerTest {

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

    @Test
    void options_shouldReturnLinksForUser() throws Exception {
        String email = "john@example.com";
        User user = mock(User.class);
        when(_userService.getUserByEmail(email)).thenReturn(user);
        when(_linkProvider.getLinks(user)).thenReturn(List.of(
                Link.of("/my-lists").withRel("self"),
                Link.of("/my-lists/create").withRel("create-list")
        ));

        _mockMvc.perform(options("/my-lists")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").value("/my-lists"))
                .andExpect(jsonPath("$._links['create-list'].href").value("/my-lists/create"));
    }

    @Test
    void options_shouldReturnOkWithNoLinks() throws Exception {
        String email = "readonly@example.com";
        User user = mock(User.class);
        when(_userService.getUserByEmail(email)).thenReturn(user);
        when(_linkProvider.getLinks(user)).thenReturn(List.of());

        _mockMvc.perform(options("/my-lists")
                        .param("email", email))
                .andExpect(status().isOk());
    }

    @Test
    void getLists_returnsOkWithEmbeddedBodyAndLinks_whenListsExist() throws Exception {
        ListOfItems firstDomain = mock(ListOfItems.class);
        ListOfItems secondDomain = mock(ListOfItems.class);
        ListOfItemsResponseDTO first =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favourites", "Fiction", true, null, List.of());
        ListOfItemsResponseDTO second =
                new ListOfItemsResponseDTO("LOI-1235", "user@cenas.com", "TBR", "Non-Fiction", false, null, List.of());

        when(_listService.getUserLists(any(UserId.class))).thenReturn(List.of(firstDomain, secondDomain));
        when(_mapper.toModel(firstDomain)).thenReturn(first);
        when(_mapper.toModel(secondDomain)).thenReturn(second);
        when(firstDomain.isPrivate()).thenReturn(true);
        when(secondDomain.isPrivate()).thenReturn(false);

        _mockMvc.perform(get("/my-lists/")
                        .header("X-User-Id", "user@cenas.com")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.*[0].listId").value("LOI-1234"))
                .andExpect(jsonPath("$._embedded.*[1].listId").value("LOI-1235"))
                .andExpect(jsonPath("$._embedded.*[0]._links.self.href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$._embedded.*[1]._links.self.href").value("http://localhost/my-lists/LOI-1235"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/"))
                .andExpect(jsonPath("$._links['create-list'].href").exists())
                .andExpect(jsonPath("$._embedded.*[0]._links['make-public'].href")
                .value("http://localhost/my-lists/LOI-1234/visibility"))
                .andExpect(jsonPath("$._embedded.*[1]._links['make-private'].href")
                        .value("http://localhost/my-lists/LOI-1235/visibility"));
    }

    @Test
    void getLists_returnsNoContent_whenServiceReturnsEmptyList() throws Exception {
        when(_listService.getUserLists(any(UserId.class))).thenReturn(List.of());

        _mockMvc.perform(get("/my-lists/")
                        .header("X-User-Id", "user@cenas.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getLists_returnsBadRequest_whenHeaderMissing() throws Exception {
        _mockMvc.perform(get("/my-lists/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getListById_returnsOkWithSelfLink_whenServiceReturnsDto() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", true, null, List.of());
        when(_listService.getListById(any(ListOfItemsId.class))).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(get("/my-lists/{listId}", "LOI-1234")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$._links.collection.href").value("http://localhost/my-lists/"))
                .andExpect(jsonPath("$._links['add-item'].href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$._links['make-public'].href").value("http://localhost/my-lists/LOI-1234/visibility"))
                .andExpect(jsonPath("$._links.delete.href").value("http://localhost/my-lists/LOI-1234"));
    }

    @Test
    void getListById_returnsNotFound_whenServiceThrows() throws Exception {
        when(_listService.getListById(any(ListOfItemsId.class)))
                .thenThrow(new RuntimeException("not found"));

        _mockMvc.perform(get("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAndSaveList_returnsCreatedWithSelfLink_whenServiceSucceeds() throws Exception {
        ListOfItemsRequestDTO request = new ListOfItemsRequestDTO("Favorites", "genre-1");
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", true, null, List.of());
        when(_listService.save(any(UserId.class), any(Name.class), any(GenreId.class))).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(post("/my-lists/")
                        .header("X-User-Id", "user@cenas.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$._links.collection.href").value("http://localhost/my-lists/"))
                .andExpect(jsonPath("$._links['add-item'].href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$._links['make-public'].href").value("http://localhost/my-lists/LOI-1234/visibility"))
                .andExpect(jsonPath("$._links.delete.href").value("http://localhost/my-lists/LOI-1234"));
    }

    @Test
    void createAndSaveList_returnsInternalServerError_whenServiceThrows() throws Exception {
        ListOfItemsRequestDTO request = new ListOfItemsRequestDTO("Favorites", "genre-1");
        when(_listService.save(any(UserId.class), any(Name.class), any(GenreId.class)))
                .thenThrow(new RuntimeException("boom"));

        _mockMvc.perform(post("/my-lists/")
                        .header("X-User-Id", "user@cenas.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addItemToList_returnsOkWithSelfLink_whenServiceSucceeds() throws Exception {
        AddItemRequestDTO request = new AddItemRequestDTO("ABCDEF1234");
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", false,
                        LocalDateTime.of(2026, 7, 2, 2, 2), List.of("ABCDEF1234"));
        when(_listService.addItemToList(any(ListOfItemsId.class), any(ItemId.class))).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(post("/my-lists/{listId}", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$._links.collection.href").value("http://localhost/my-lists/"))
                .andExpect(jsonPath("$._links['make-private'].href").value("http://localhost/my-lists/LOI-1234/visibility"));
    }

    @Test
    void addItemToList_returnsInternalServerError_whenServiceThrows() throws Exception {
        AddItemRequestDTO request = new AddItemRequestDTO("ITEM-999");
        when(_listService.addItemToList(any(ListOfItemsId.class), any(ItemId.class)))
                .thenThrow(new RuntimeException("boom"));

        _mockMvc.perform(post("/my-lists/{listId}", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void makeListPublic_returnsOkWithSelfLink_whenServiceSucceeds() throws Exception {
        MakeListPublicRequestDTO request = new MakeListPublicRequestDTO(7);
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", false, null, List.of());
        when(_listService.makePublic(any(ListOfItemsId.class), any(SharedDuration.class))).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$._links['make-private'].href").value("http://localhost/my-lists/LOI-1234/visibility"));
    }

    @Test
    void makeListPublic_returnsInternalServerError_whenServiceThrows() throws Exception {
        MakeListPublicRequestDTO request = new MakeListPublicRequestDTO(7);
        when(_listService.makePublic(any(ListOfItemsId.class), any(SharedDuration.class)))
                .thenThrow(new RuntimeException("boom"));

        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void makeListPrivate_returnsOkWithSelfLink_whenServiceSucceeds() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO response =
                new ListOfItemsResponseDTO("LOI-1234", "user@cenas.com", "Favorites", "genre-1", true,
                        LocalDateTime.of(2026, 7, 2, 2, 2), List.of());
        when(_listService.makePrivate(any(ListOfItemsId.class))).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(response);

        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("LOI-1234"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/LOI-1234"))
                .andExpect(jsonPath("$._links['make-public'].href").value("http://localhost/my-lists/LOI-1234/visibility"));
    }

    @Test
    void makeListPrivate_returnsInternalServerError_whenServiceThrows() throws Exception {
        when(_listService.makePrivate(any(ListOfItemsId.class)))
                .thenThrow(new RuntimeException("boom"));

        _mockMvc.perform(patch("/my-lists/{listId}/visibility", "LOI-1234"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteList_returnsOk_whenServiceSucceeds() throws Exception {
        doNothing().when(_listService).deleteList(any(ListOfItemsId.class));

        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteList_returnsInternalServerError_whenServiceThrows() throws Exception {
        doThrow(new RuntimeException("boom"))
                .when(_listService).deleteList(any(ListOfItemsId.class));

        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isInternalServerError());
    }
}