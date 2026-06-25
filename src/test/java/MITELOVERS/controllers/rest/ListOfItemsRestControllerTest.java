package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.dto.request.AddItemRequestDTO;
import MITELOVERS.dto.request.ListOfItemsRequestDTO;
import MITELOVERS.dto.request.MakeListPublicRequestDTO;
import MITELOVERS.dto.response.ListOfItemsResponseDTO;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.*;
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

    @MockitoBean
    private AuthorizationPolicy _auth;

    @MockitoBean
    private ItemId _itemId;

    @MockitoBean
    private ItemId _itemId2;

    @MockitoBean
    private User _user;

    @BeforeEach
    void setup() {
        when(_userService.getUserByEmail(any())).thenReturn(_user);

        // Default: allow everything unless overridden
        when(_auth.canSeeList(any(), any())).thenReturn(true);
        when(_auth.canAddItemTo(any(), any())).thenReturn(true);
        when(_auth.canChangeVisibility(any(), any())).thenReturn(true);
        when(_auth.canDeleteList(any(), any())).thenReturn(true);
    }

    @Test
    void options_collection_returnsLinks() throws Exception {
        when(_linkProvider.getLinks(_user)).thenReturn(List.of(
                Link.of("http://localhost/my-lists/").withRel("collection"),
                Link.of("http://localhost/my-lists/").withRel("create-list"),
                Link.of("http://localhost/my-lists/public").withRel("public-lists")
        ));

        _mockMvc.perform(options("/my-lists")
                        .header("X-User-Id", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.collection.href").value("http://localhost/my-lists/"))
                .andExpect(jsonPath("$._links['create-list'].href").value("http://localhost/my-lists/"))
                .andExpect(jsonPath("$._links['public-lists'].href").value("http://localhost/my-lists/public"));
    }

    @Test
    void options_singleList_returnsAllowedActions() throws Exception {
        ListOfItems list = mock(ListOfItems.class);
        when(_listService.getListById(any())).thenReturn(list);
        when(list.isPrivate()).thenReturn(true);

        _mockMvc.perform(options("/my-lists/L1")
                        .header("X-User-Id", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/L1"))
                .andExpect(jsonPath("$._links['add-item'].href").value("http://localhost/my-lists/L1"))
                .andExpect(jsonPath("$._links['make-public'].href").value("http://localhost/my-lists/L1/visibility"))
                .andExpect(jsonPath("$._links.delete.href").value("http://localhost/my-lists/L1"));
    }

    @Test
    void options_items_returnsSelfLink_whenAllowed() throws Exception {
        ListOfItems list = mock(ListOfItems.class);
        when(_listService.getListById(any())).thenReturn(list);

        _mockMvc.perform(options("/my-lists/L1/items")
                        .header("X-User-Id", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/L1/items"));
    }

    @Test
    void options_singleList_withoutUserHeader_returnsOnlyPublicFacingLinks_whenPublic() throws Exception {
        ListOfItems list = mock(ListOfItems.class);
        when(_listService.getListById(any())).thenReturn(list);
        when(list.isPrivate()).thenReturn(false);
        when(_auth.canSeeList(isNull(), same(list))).thenReturn(true);
        when(_auth.canAddItemTo(isNull(), same(list))).thenReturn(false);
        when(_auth.canChangeVisibility(isNull(), same(list))).thenReturn(false);
        when(_auth.canDeleteList(isNull(), same(list))).thenReturn(false);

        _mockMvc.perform(options("/my-lists/L1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/L1"))
                .andExpect(jsonPath("$._links['add-item']").doesNotExist())
                .andExpect(jsonPath("$._links['make-public']").doesNotExist())
                .andExpect(jsonPath("$._links['make-private']").doesNotExist())
                .andExpect(jsonPath("$._links.delete").doesNotExist());

        verify(_userService, never()).getUserByEmail(any());
    }

    @Test
    void getLists_returnsOkWithEmbeddedLists() throws Exception {
        ListOfItems d1 = mock(ListOfItems.class);
        ListOfItems d2 = mock(ListOfItems.class);

        ListOfItemsResponseDTO dto1 =
                new ListOfItemsResponseDTO("L1", "user@x.com", "Favs", "Fiction", true, null, List.of());
        ListOfItemsResponseDTO dto2 =
                new ListOfItemsResponseDTO("L2", "user@x.com", "TBR", "NF", false, null, List.of());

        when(_listService.getUserLists(any())).thenReturn(List.of(d1, d2));
        when(_mapper.toModel(d1)).thenReturn(dto1);
        when(_mapper.toModel(d2)).thenReturn(dto2);
        when(d1.isPrivate()).thenReturn(true);
        when(d2.isPrivate()).thenReturn(false);

        _mockMvc.perform(get("/my-lists/")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.*[0].listId").value("L1"))
                .andExpect(jsonPath("$._embedded.*[1].listId").value("L2"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/"))
                .andExpect(jsonPath("$._links['create-list'].href").exists());
    }

    @Test
    void getLists_returnsNoContent_whenEmpty() throws Exception {
        when(_listService.getUserLists(any())).thenReturn(List.of());

        _mockMvc.perform(get("/my-lists/")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getListById_returnsOk() throws Exception {
        ListOfItems domain = mock(ListOfItems.class);
        ListOfItemsResponseDTO dto =
                new ListOfItemsResponseDTO("L1", "user@x.com", "Favs", "Fiction", true, null, List.of());

        when(_listService.getListById(any())).thenReturn(domain);
        when(_mapper.toModel(domain)).thenReturn(dto);

        _mockMvc.perform(get("/my-lists/L1")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("L1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/L1"));
    }

    @Test
    void getListById_returnsForbidden_whenNotAllowed() throws Exception {
        when(_auth.canSeeList(any(), any())).thenReturn(false);
        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));

        _mockMvc.perform(get("/my-lists/L1")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getListById_withoutUserHeader_returnsOk_whenPublicList() throws Exception {
        ListOfItems domain = mock(ListOfItems.class);
        ListOfItemsResponseDTO dto =
                new ListOfItemsResponseDTO("L1", "user@x.com", "Public Favs", "Fiction", false, null, List.of());

        when(_listService.getListById(any())).thenReturn(domain);
        when(domain.isPrivate()).thenReturn(false);
        when(_auth.canSeeList(isNull(), same(domain))).thenReturn(true);
        when(_auth.canAddItemTo(isNull(), same(domain))).thenReturn(false);
        when(_auth.canChangeVisibility(isNull(), same(domain))).thenReturn(false);
        when(_auth.canDeleteList(isNull(), same(domain))).thenReturn(false);
        when(_mapper.toModel(domain)).thenReturn(dto);

        _mockMvc.perform(get("/my-lists/L1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("L1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/my-lists/L1"));

        verify(_userService, never()).getUserByEmail(any());
    }

    @Test
    void getListById_withoutUserHeader_returnsForbidden_whenPrivateList() throws Exception {
        ListOfItems domain = mock(ListOfItems.class);
        when(_listService.getListById(any())).thenReturn(domain);
        when(domain.isPrivate()).thenReturn(true);
        when(_auth.canSeeList(isNull(), same(domain))).thenReturn(false);

        _mockMvc.perform(get("/my-lists/L1"))
                .andExpect(status().isForbidden());

        verify(_userService, never()).getUserByEmail(any());
    }

    @Test
    void createList_returnsCreated() throws Exception {
        ListOfItemsRequestDTO req = new ListOfItemsRequestDTO("Favs", "G1");
        ListOfItems domain = mock(ListOfItems.class);

        ListOfItemsResponseDTO dto =
                new ListOfItemsResponseDTO("L1", "user@x.com", "Favs", "G1", true, null, List.of());

        when(_listService.save(any(), any(), any())).thenReturn(domain);
        when(_mapper.toModel(domain)).thenReturn(dto);

        _mockMvc.perform(post("/my-lists/")
                        .header("X-User-Id", "user@x.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.listId").value("L1"));
    }

    @Test
    void addItem_returnsOk() throws Exception {
        AddItemRequestDTO req = new AddItemRequestDTO("ABCDEF1234");
        ListOfItems domain = mock(ListOfItems.class);

        ItemId id1 = mock(ItemId.class);
        when(id1.toString()).thenReturn("ABCDEF1234");
        when(domain.getItemIds()).thenReturn(List.of(id1));

        when(_listService.getListById(any())).thenReturn(domain);
        when(_listService.addItemToList(any(), any())).thenReturn(domain);

        ListOfItemsResponseDTO dto =
                new ListOfItemsResponseDTO("L1", "user@x.com", "Favs", "G1", false,
                        LocalDateTime.now(), List.of("ABCDEF1234"));

        when(_mapper.toModel(domain)).thenReturn(dto);

        _mockMvc.perform(post("/my-lists/L1")
                        .header("X-User-Id", "user@x.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("L1"));
    }

    @Test
    void addItem_returnsForbidden_whenNotAllowed() throws Exception {
        when(_auth.canAddItemTo(any(), any())).thenReturn(false);
        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));

        AddItemRequestDTO req = new AddItemRequestDTO("ITEM1");

        _mockMvc.perform(post("/my-lists/L1")
                        .header("X-User-Id", "user@x.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void makePublic_returnsOk() throws Exception {
        MakeListPublicRequestDTO req = new MakeListPublicRequestDTO(7);
        ListOfItems domain = mock(ListOfItems.class);

        ListOfItemsResponseDTO dto =
                new ListOfItemsResponseDTO("L1", "user@x.com", "Favs", "G1", false, null, List.of());

        when(_listService.getListById(any())).thenReturn(domain);
        when(_listService.makePublic(any(), any())).thenReturn(domain);
        when(_mapper.toModel(domain)).thenReturn(dto);

        _mockMvc.perform(patch("/my-lists/L1/visibility")
                        .header("X-User-Id", "user@x.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("L1"));
    }

    @Test
    void makePublic_returnsForbidden_whenNotAllowed() throws Exception {
        when(_auth.canChangeVisibility(any(), any())).thenReturn(false);
        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));

        MakeListPublicRequestDTO req = new MakeListPublicRequestDTO(7);

        _mockMvc.perform(patch("/my-lists/L1/visibility")
                        .header("X-User-Id", "user@x.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void makePrivate_returnsOk() throws Exception {
        ListOfItems domain = mock(ListOfItems.class);

        ListOfItemsResponseDTO dto =
                new ListOfItemsResponseDTO("L1", "user@x.com", "Favs", "G1", true,
                        LocalDateTime.now(), List.of());

        when(_listService.getListById(any())).thenReturn(domain);
        when(_listService.makePrivate(any())).thenReturn(domain);
        when(_mapper.toModel(domain)).thenReturn(dto);

        _mockMvc.perform(patch("/my-lists/L1/visibility")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listId").value("L1"));
    }

    @Test
    void deleteList_returnsOk() throws Exception {
        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));

        _mockMvc.perform(delete("/my-lists/L1")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteList_returnsForbidden_whenNotAllowed() throws Exception {
        when(_auth.canDeleteList(any(), any())).thenReturn(false);
        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));

        _mockMvc.perform(delete("/my-lists/L1")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getPublicLists_returnsOk() throws Exception {
        ListOfItems d1 = mock(ListOfItems.class);
        ListOfItems d2 = mock(ListOfItems.class);

        ListOfItemsResponseDTO dto1 =
                new ListOfItemsResponseDTO("L1", "user@x.com", "Public Favs", "Fiction", false, null, List.of());
        ListOfItemsResponseDTO dto2 =
                new ListOfItemsResponseDTO("L2", "user@x.com", "Public TBR", "NF", false, null, List.of());

        when(_listService.getPublicLists()).thenReturn(List.of(d1, d2));
        when(_mapper.toModel(d1)).thenReturn(dto1);
        when(_mapper.toModel(d2)).thenReturn(dto2);

        _mockMvc.perform(get("/my-lists/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.*[0].listId").value("L1"))
                .andExpect(jsonPath("$._embedded.*[1].listId").value("L2"))
                .andExpect(jsonPath("$._embedded.*[0]._links.items.href").value("http://localhost/my-lists/L1/items"))
                .andExpect(jsonPath("$._embedded.*[1]._links.items.href").value("http://localhost/my-lists/L2/items"));
    }

    @Test
    void getPublicLists_returnsNoContent_whenEmpty() throws Exception {
        when(_listService.getPublicLists()).thenReturn(List.of());

        _mockMvc.perform(get("/my-lists/public"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getItemsInList_returnsOk() throws Exception {
        ListOfItems domain = mock(ListOfItems.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);

        when(_listService.getListById(any())).thenReturn(domain);
        when(id1.toString()).thenReturn("A");
        when(id2.toString()).thenReturn("B");
        when(domain.getItemIds()).thenReturn(List.of(id1, id2));

        _mockMvc.perform(get("/my-lists/L1/items")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0]").value("A"))
                .andExpect(jsonPath("$.items[1]").value("B"))
                .andExpect(jsonPath("$.links.links[0].href").value("http://localhost/my-lists/L1/items"))
                .andExpect(jsonPath("$.links.links[0].rel").value("self"));
    }

    @Test
    void getItemsInList_returnsForbidden_whenNotAllowed() throws Exception {
        when(_auth.canSeeList(any(), any())).thenReturn(false);
        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));

        _mockMvc.perform(get("/my-lists/L1/items")
                        .header("X-User-Id", "user@x.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getItemsInList_withoutUserHeader_returnsOk_whenPublicList() throws Exception {
        ListOfItems domain = mock(ListOfItems.class);
        ItemId id1 = mock(ItemId.class);
        ItemId id2 = mock(ItemId.class);

        when(_listService.getListById(any())).thenReturn(domain);
        when(domain.isPrivate()).thenReturn(false);
        when(_auth.canSeeList(isNull(), same(domain))).thenReturn(true);
        when(id1.toString()).thenReturn("A");
        when(id2.toString()).thenReturn("B");
        when(domain.getItemIds()).thenReturn(List.of(id1, id2));

        _mockMvc.perform(get("/my-lists/L1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0]").value("A"))
                .andExpect(jsonPath("$.items[1]").value("B"))
                .andExpect(jsonPath("$.links.links[0].href").value("http://localhost/my-lists/L1/items"))
                .andExpect(jsonPath("$.links.links[0].rel").value("self"));

        _mockMvc.perform(get("/my-lists/{listId}/items", "LOI-1234"))
                .andExpect(status().isOk());
    }
}
