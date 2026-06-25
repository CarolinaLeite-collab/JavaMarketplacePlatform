package MITELOVERS.security;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.user.User;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OWASP A08:2025 — Software and Data Integrity Failures
 *
 * Documents the absence of request integrity controls. The application does not
 * require any nonce, timestamp, HMAC signature, or idempotency key on mutating
 * requests. As a result, any captured HTTP request can be replayed an arbitrary
 * number of times without detection. Additionally, API responses do not include
 * security-oriented headers that inform clients how to handle response content
 * safely, leaving browser-side integrity enforcement to default (permissive)
 * behaviour — for example, allowing MIME-type sniffing.
 *
 * <p><strong>Accepted-Risk Documentation:</strong> These tests intentionally pass
 * because the conditions they describe exist in the current codebase. They do not
 * assert that integrity controls are in place; rather, they serve as a formal,
 * traceable record of risks acknowledged by the team. Implementing idempotency keys,
 * HMAC request signing, and security response headers is outside the scope of this
 * sprint and is tracked as a separate backlog item.</p>
 */
@Tag("security")
@WebMvcTest(ListOfItemsRestController.class)
@Import(CustomRestExceptionHandler.class)
class DataIntegritySecurityTest {

    @Autowired
    private MockMvc _mockMvc;

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
    @DisplayName("OWASP A08 Data Integrity: identical mutating request is accepted on replay — no idempotency key or nonce is enforced")
    void mutatingRequest_isAcceptedOnReplay_withNoIdempotencyControl() throws Exception {
        // Arrange
        User user = mock(User.class);
        ListOfItems list = mock(ListOfItems.class);

        when(_userService.getUserByEmail(any()))
                .thenReturn(user);

        when(_listService.getListById(any()))
                .thenReturn(list);

        when(_authorizationPolicy.canDeleteList(user, list))
                .thenReturn(true);

        doNothing().when(_listService).deleteList(any());

        // First execution
        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk());

        // Exact replay — the server accepts it without challenge, nonce, or idempotency token
        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "user@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("OWASP A08 Data Integrity: API responses omit X-Content-Type-Options — MIME-type sniffing is not suppressed in browsers")
    void apiResponse_omitsXContentTypeOptionsHeader_allowingMimeSniffing() throws Exception {
        ListOfItems domainList = mock(ListOfItems.class);
        ListOfItemsResponseDTO responseDTO = new ListOfItemsResponseDTO(
                "LOI-1234", "owner@example.com", "Favourites", "fiction", false, null, List.of());

        when(_authorizationPolicy.canSeeList(any(), any()))
                .thenReturn(true);
        when(_listService.getListById(any())).thenReturn(domainList);
        when(_mapper.toModel(domainList)).thenReturn(responseDTO);

        _mockMvc.perform(get("/my-lists/{listId}", "LOI-1234")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("X-Content-Type-Options"));
    }
}
