package MITELOVERS.security;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OWASP A10:2025 — Mishandling of Exceptional Conditions
 *
 * Documents information leakage through exception messages. When domain or
 * persistence operations throw exceptions that propagate to
 * {@link CustomRestExceptionHandler}, the raw exception message — including internal
 * architectural language such as aggregate identifiers, domain state descriptions,
 * and implementation class names — is serialised verbatim into the HTTP response body.
 * A caller can use these responses to infer the application's internal structure,
 * understand the domain model, and craft more targeted follow-up requests.
 *
 * <p>Note: not all controller methods propagate exceptions to the handler; some catch
 * internally and return empty 4xx responses. This inconsistency is itself a mishandling
 * finding — error responses are neither uniformly informative nor uniformly sanitised.</p>
 *
 * <p><strong>Accepted-Risk Documentation:</strong> These tests intentionally pass
 * because the conditions they describe exist in the current codebase. They do not
 * assert that exception messages are sanitised before reaching the client; rather,
 * they serve as a formal, traceable record of risks acknowledged by the team.
 * Replacing raw exception messages with generic, client-safe error bodies while
 * routing full detail to server-side logs only is outside the scope of this sprint
 * and is tracked as a separate backlog item.</p>
 */
@Tag("security")
@WebMvcTest(ListOfItemsRestController.class)
@Import(CustomRestExceptionHandler.class)
class ExceptionHandlingSecurityTest {

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

    @Test
    @DisplayName("OWASP A10 Exception Handling: domain exception with internal DDD language is propagated verbatim in the HTTP response body")
    void domainException_containingInternalLanguage_isPropagatedVerbatimInResponseBody() throws Exception {
        String internalMessage =
                "No ListOfItems aggregate found for AggregateId[LOI-9999]; " +
                "AuctionSubdomain holds 3 active bids — aggregate root cannot be resolved " +
                "[ref: AggregateRoot@io.domain.listofitems.ListOfItems]";
        when(_listService.getPublicLists())
                .thenThrow(new NoSuchElementException(internalMessage));

        _mockMvc.perform(get("/my-lists/public"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("AggregateId")));
    }

    @Test
    @DisplayName("OWASP A10 Exception Handling: queried email is reflected in error body via OPTIONS endpoint, enabling differential enumeration")
    void queriedEmail_isReflectedInErrorBody_enablingDifferentialEnumeration() throws Exception {
        String targetEmail = "victim@example.com";
        when(_userService.getUserByEmail(eq(targetEmail)))
                .thenThrow(new NoSuchElementException("User not found: " + targetEmail));

        _mockMvc.perform(options("/my-lists").param("email", targetEmail))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString(targetEmail)));
    }
}
