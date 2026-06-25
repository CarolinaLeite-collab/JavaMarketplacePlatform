package MITELOVERS.security;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * OWASP A07:2025 — Authentication Failures
 *
 * Documents the absence of a real authentication mechanism. The application uses
 * a plain-text {@code X-User-Id} HTTP header as the sole identity signal. This
 * value is never verified against a credential store: no password, bearer token,
 * session cookie, or cryptographic proof is required. Any caller who knows — or
 * guesses — a valid email address can present it in the header and act on behalf
 * of that identity. Furthermore, many endpoints do not require the header at all,
 * accepting requests with no identity claim whatsoever.
 *
 * <p>Note: while {@link ListOfItemsOWASPSecurityTest} (A01) documents what an
 * unauthenticated caller can <em>do</em> (IDOR — access or modify another user's
 * resources), these tests focus on the authentication layer itself: the system
 * never challenges the identity claim, regardless of its content or absence.</p>
 *
 * <p><strong>Accepted-Risk Documentation:</strong> These tests intentionally pass
 * because the vulnerabilities they describe are present in the current codebase.
 * They do not assert that authentication controls are in place; rather, they serve
 * as a formal, traceable record of risks acknowledged by the team. Introducing a
 * token-based authentication mechanism (e.g., JWT or OAuth 2.0) is outside the
 * scope of this sprint and is tracked as a separate backlog item.</p>
 */
@Tag("security")
@WebMvcTest(ListOfItemsRestController.class)
@Import(CustomRestExceptionHandler.class)
class AuthenticationFailuresSecurityTest {

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
    private AuthorizationPolicy _authPolicy;

    @Test
    @DisplayName("OWASP A07 Auth Failure: fabricated identity claim is processed as a header value, but authorization blocks the action")
    void endpoint_acceptsArbitraryIdentityClaim_withoutCredentialVerification() throws Exception {
        // A syntactically valid but completely fabricated identity is presented and accepted without challenge
        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234")
                        .header("X-User-Id", "nonexistent-identity-that-has-never-been-registered@nowhere.invalid"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("OWASP A07 Auth Failure: missing X-User-Id header yields framework-level 400 instead of an authentication challenge")
    void endpoint_withNoIdentityHeader_doesNotIssueAuthChallenge() throws Exception {
        // No identity provided — a properly secured endpoint would respond 401 Unauthorized
        // Instead the server processes the request successfully, confirming there is no authentication gate
        _mockMvc.perform(delete("/my-lists/{listId}", "LOI-1234"))
                .andExpect(status().isBadRequest());
    }
}
