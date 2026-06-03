package MITELOVERS.authorization;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorizationPolicyTest {

    private AuthorizationPolicy _authorizationPolicy;
    private User _userDouble;
    private User _adminDouble;

    @BeforeEach
    void setUp() {
        _authorizationPolicy = new AuthorizationPolicy();

        _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(true);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.USER)).thenReturn(false);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
    }

    // ──────────── Publishing Company ────────────

    @Test
    void canCreatePublishingCompanyAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canCreatePublishingCompany(_adminDouble));
    }

    @Test
    void canCreatePublishingCompanyUserReturnsFalse() {
        assertFalse(_authorizationPolicy.canCreatePublishingCompany(_userDouble));
    }

    @Test
    void canGetPublishingCompaniesAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetAllPublishingCompanies(_adminDouble));
    }

    @Test
    void canGetPublishingCompaniesUserReturnsFalse() {
        assertFalse(_authorizationPolicy.canGetAllPublishingCompanies(_userDouble));
    }

    @Test
    void canGetPublishingCompanyAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetPublishingCompany(_adminDouble));
    }

    @Test
    void canGetPublishingCompanyUserReturnsFalse() {
        assertFalse(_authorizationPolicy.canGetPublishingCompany(_userDouble));
    }

    // ──────────── Publication Type ────────────

    @Test
    void userCanListPublicationTypes() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(true);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertTrue(policy.canListPublicationTypes(_userDouble));
    }

    @Test
    void adminCanListPublicationTypes() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertTrue(policy.canListPublicationTypes(_userDouble));
    }

    @Test
    void userWithNoRoleCannotListPublicationTypes() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(false);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertFalse(policy.canListPublicationTypes(_userDouble));
    }

    @Test
    void adminCanCreatePublicationType() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertTrue(policy.canCreatePublicationType(_userDouble));
    }

    @Test
    void userCannotCreatePublicationType() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(true);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertFalse(policy.canCreatePublicationType(_userDouble));
    }

    @Test
    void userWithNoRoleCannotCreatePublicationType() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(false);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertFalse(policy.canCreatePublicationType(_userDouble));
    }

    // ──────────── Edition ────────────

    @Test
    void userCanListEditions() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(true);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertTrue(policy.canListEditions(_userDouble));
    }

    @Test
    void adminCanListEditions() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertTrue(policy.canListEditions(_userDouble));
    }

    @Test
    void userWithNoRoleCannotListEditions() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(false);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertFalse(policy.canListEditions(_userDouble));
    }

    @Test
    void userCanCreateEdition() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(true);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertTrue(policy.canCreateEdition(_userDouble));
    }

    @Test
    void adminCanCreateEdition() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertTrue(policy.canCreateEdition(_userDouble));
    }

    @Test
    void userWithNoRoleCannotCreateEdition() {
        // Arrange
        User _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(false);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);

        // SUT
        AuthorizationPolicy policy = new AuthorizationPolicy();

        // Assert
        assertFalse(policy.canCreateEdition(_userDouble));
    }






}