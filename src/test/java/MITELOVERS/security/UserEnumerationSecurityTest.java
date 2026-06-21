package MITELOVERS.security;

import MITELOVERS.applicationservices.ListOfItemsService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.exception.CustomRestExceptionHandler;
import MITELOVERS.controllers.linkprovider.ListOfItemsLinkProvider;
import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.domain.user.User;
import MITELOVERS.mapper.ListOfItemsResponseDTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OWASP A04:2021 — Insecure Design / Information Disclosure
 *
 * Documents user enumeration via the OPTIONS ?email= endpoint.
 * The application responds differently (404 vs 200) depending on whether
 * the supplied email belongs to a registered account, providing a binary
 * oracle that allows an attacker to enumerate valid user accounts.
 */
@Tag("security")
@WebMvcTest(ListOfItemsRestController.class)
@Import(CustomRestExceptionHandler.class)
class UserEnumerationSecurityTest {

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
    @DisplayName("OWASP A04 Info Disclosure: OPTIONS returns 404 with email in body for non-existent account")
    void options_returns404WithEmailInBody_whenUserNotFound() throws Exception {
        String nonExistentEmail = "nonexistent@example.com";
        when(_userService.getUserByEmail(nonExistentEmail))
                .thenThrow(new NoSuchElementException("User not found: " + nonExistentEmail));

        _mockMvc.perform(options("/my-lists").param("email", nonExistentEmail))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found: " + nonExistentEmail));
    }

    @Test
    @DisplayName("OWASP A04 Info Disclosure: OPTIONS returns 200 for existing account (enumeration oracle confirmed)")
    void options_returns200_whenUserExists() throws Exception {
        String existingEmail = "existing@example.com";
        User user = mock(User.class);
        when(_userService.getUserByEmail(existingEmail)).thenReturn(user);
        when(_linkProvider.getLinks(user)).thenReturn(List.of());

        _mockMvc.perform(options("/my-lists").param("email", existingEmail))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("OWASP A04 Info Disclosure: 404 body leaks the queried email address")
    void options_leaksEmailInErrorBody_forUnknownAccount() throws Exception {
        String targetEmail = "victim@example.com";
        when(_userService.getUserByEmail(targetEmail))
                .thenThrow(new NoSuchElementException("User not found: " + targetEmail));

        _mockMvc.perform(options("/my-lists").param("email", targetEmail))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString(targetEmail)));
    }
}