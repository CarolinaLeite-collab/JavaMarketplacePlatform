package MITELOVERS.security;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * OWASP A05:2025 — Injection
 *
 * Documents the input-validation posture at the HTTP boundary for the
 * {@link ListOfItemsRestController} endpoints.
 *
 * <p><strong>{@code ?email} query parameter — format validation IS enforced.</strong>
 * The OPTIONS endpoint converts the raw {@code ?email} parameter into an {@code Email}
 * value object ({@code new Email(email)}). The {@code Email} constructor validates the
 * value against a strict format pattern and throws {@link IllegalArgumentException} for
 * malformed input, which {@link CustomRestExceptionHandler} maps to HTTP 400. As a
 * result, injection-style payloads (SQL/log/command fragments containing quotes,
 * semicolons, whitespace, etc.) are rejected at the controller boundary and never reach
 * the service or domain layers. The first test below documents this control.</p>
 *
 * <p><strong>{@code {listId}} path variable — NO format validation.</strong> Path
 * variables are forwarded into the service layer without character-level pattern
 * enforcement. The second test below documents this remaining gap: an injection-style
 * payload propagates verbatim to the service. Pattern-restricting path variables is
 * outside the scope of this sprint and is tracked as a separate backlog item.</p>
 *
 * <p><strong>Note on evolution:</strong> a previous iteration documented the {@code ?email}
 * parameter as <em>unvalidated</em> (accepted risk). The introduction of {@code Email}
 * value-object validation at the boundary turned that accepted risk into an enforced
 * control; this test was updated to reflect the control now in place.</p>
 */
@Tag("security")
@WebMvcTest(ListOfItemsRestController.class)
@Import(CustomRestExceptionHandler.class)
class InjectionSecurityTest {

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
    @DisplayName("OWASP A05 Injection: SQL-like payload in ?email= is rejected by Email format validation at the controller boundary (400), never reaching the service")
    void emailParameter_rejectsSqlPayload_atValidationBoundary() throws Exception {
        // A SQL-injection payload is not a syntactically valid email. The controller builds
        // `new Email(email)`, whose constructor throws IllegalArgumentException ("Invalid email
        // format!"), mapped to HTTP 400 by CustomRestExceptionHandler. The payload is rejected
        // at the boundary, so the service is never invoked.
        // NOTE: we pass the payload as a raw request param and let the controller validate it.
        // We intentionally do NOT build `new Email(payload)` here — that would throw inside the
        // test itself, before exercising the controller.
        String sqlPayload = "' OR '1'='1'; DROP TABLE users; --";

        _mockMvc.perform(get("/my-lists/search").param("email", sqlPayload))
                .andExpect(status().is4xxClientError());

        // The malformed value was rejected at the validation boundary: the service was never reached.
        verify(_userService, never()).getUserByEmail(any());
    }

    @Test
    @DisplayName("OWASP A05 Injection: path variable {listId} accepts special characters without format enforcement")
    void pathVariable_acceptsInjectionStylePayload_withNoPatternConstraint() throws Exception {
        // Semicolons are stripped by Spring MVC (matrix variable parsing), so the payload avoids them
        String injectionPayload = "' OR listId='LOI-ABCDEF12";

        when(_listService.getListById(any())).thenReturn(mock(ListOfItems.class));

        _mockMvc.perform(delete("/my-lists/{listId}", injectionPayload)
                        .header("X-User-Id", "pedro@mail.com"))
                .andExpect(status().isForbidden());

        // The controller accepted the payload without rejection and forwarded it to the service unchanged
        verify(_listService).getListById(any());
    }
}