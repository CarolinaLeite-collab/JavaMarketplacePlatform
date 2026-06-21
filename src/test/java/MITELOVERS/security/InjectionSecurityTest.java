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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OWASP A05:2025 — Injection
 *
 * Documents the absence of input validation and sanitisation at the HTTP boundary.
 * User-supplied values — such as the {@code ?email} query parameter and path variables
 * like {@code {listId}} — are forwarded to the service and domain layers without any
 * format enforcement or character-level sanitisation. While JPA parameterisation
 * mitigates direct SQL injection against the database, the lack of validation at the
 * controller level allows injection-style payloads to propagate verbatim through the
 * application stack, reaching logging subsystems (log injection), error messages
 * (information disclosure), and downstream consumers of these values.
 *
 * <p><strong>Accepted-Risk Documentation:</strong> These tests intentionally pass
 * because the conditions they describe exist in the current codebase. They do not
 * assert that input validation controls are in place; rather, they serve as a formal,
 * traceable record of risks acknowledged by the team. Adding {@code @Valid} annotations,
 * email-format constraints, and path-variable pattern restrictions is outside the scope
 * of this sprint and is tracked as a separate backlog item.</p>
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

    @Test
    @DisplayName("OWASP A05 Injection: SQL-like payload in ?email= parameter reaches the service layer without format validation")
    void emailParameter_acceptsSqlPayload_withNoValidationAtHttpLayer() throws Exception {
        String sqlPayload = "' OR '1'='1'; DROP TABLE users; --";
        when(_userService.getUserByEmail(eq(sqlPayload)))
                .thenThrow(new NoSuchElementException("User not found: " + sqlPayload));

        _mockMvc.perform(options("/my-lists").param("email", sqlPayload))
                .andExpect(status().isNotFound());

        // Confirms the raw, unsanitised payload was passed directly to the service — no controller-level rejection occurred
        verify(_userService).getUserByEmail(sqlPayload);
    }

    @Test
    @DisplayName("OWASP A05 Injection: path variable {listId} accepts special characters without format enforcement")
    void pathVariable_acceptsInjectionStylePayload_withNoPatternConstraint() throws Exception {
        // Semicolons are stripped by Spring MVC (matrix variable parsing), so the payload avoids them
        String injectionPayload = "' OR listId='LOI-OTHER-9999";
        doNothing().when(_listService).deleteList(any());

        _mockMvc.perform(delete("/my-lists/{listId}", injectionPayload))
                .andExpect(status().isOk());

        // The controller accepted the payload without rejection and forwarded it to the service unchanged
        verify(_listService).deleteList(argThat(id -> id.toString().equals(injectionPayload)));
    }
}
