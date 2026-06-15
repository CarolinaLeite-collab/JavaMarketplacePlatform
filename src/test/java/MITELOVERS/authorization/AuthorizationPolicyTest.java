package MITELOVERS.authorization;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorizationPolicyTest {

    private AuthorizationPolicy _authorizationPolicy;
    private User _userDouble;
    private User _adminDouble;
    private User _guestDouble;

    @BeforeEach
    void setUp() {
        _authorizationPolicy = new AuthorizationPolicy();

        _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(true);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);
        when(_userDouble.hasRole(Role.NONREGISTRED)).thenReturn(false);

        _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.USER)).thenReturn(false);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_adminDouble.hasRole(Role.NONREGISTRED)).thenReturn(false);

        _guestDouble = mock(User.class);
        when(_guestDouble.hasRole(Role.USER)).thenReturn(false);
        when(_guestDouble.hasRole(Role.ADMIN)).thenReturn(false);
        when(_guestDouble.hasRole(Role.NONREGISTRED)).thenReturn(true);
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

    // ── Direct Sale ─────────────────────────────────────────────────────────

    @Test
    void canListDirectSalesUserReturnsFalse() {
        assertFalse(_authorizationPolicy.canListDirectSales(_userDouble));
    }

    @Test
    void canListActiveDirectSalesUserReturnsTrue() { assertTrue(_authorizationPolicy.canListActiveDirectSales(_userDouble)); }

    @Test
    void canListActiveDirectSalesAdminReturnsTrue() { assertTrue(_authorizationPolicy.canListActiveDirectSales(_adminDouble)); }

    @Test
    void canListDirectSalesAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canListDirectSales(_adminDouble));
    }

    @Test
    void canCreateDirectSaleUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canCreateDirectSale(_userDouble));
    }

    @Test
    void canCreateDirectSaleAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canCreateDirectSale(_adminDouble));
    }

    @Test
    void canCreateDirectSaleGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canCreateDirectSale(_guestDouble));
    }

    @Test
    void canFilterDirectSalesUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canFilterDirectSales(_userDouble));
    }

    @Test
    void canFilterDirectSalesAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canFilterDirectSales(_adminDouble));
    }

    @Test
    void canGetDirectSaleUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetDirectSale(_userDouble));
    }

    @Test
    void canGetDirectSaleAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetDirectSale(_adminDouble));
    }

    @Test
    void canListDirectSalesGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canListDirectSales(_guestDouble));
    }

    @Test
    void canGetDirectSaleGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canGetDirectSale(_guestDouble));
    }

    @Test
    void cannotGetDirectSalePricesGuestReturnsTrue() {
        assertTrue(_authorizationPolicy.cannotSeePrice(_guestDouble));
    }

    @Test
    void userCanListCountries() {
        // Act
        boolean result = _authorizationPolicy.canListCountries(_userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanListCountries() {
        // Act
        boolean result = _authorizationPolicy.canListCountries(_adminDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void guestCannotListCountries() {
        // Act
        boolean result = _authorizationPolicy.canListCountries(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanGetCountry() {
        // Act
        boolean result = _authorizationPolicy.canGetCountry(_userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanGetCountry() {
        // Act
        boolean result = _authorizationPolicy.canGetCountry(_adminDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void guestCannotGetCountry() {
        // Act
        boolean result = _authorizationPolicy.canGetCountry(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanCreateCountry() {
        // Act
        boolean result = _authorizationPolicy.canCreateCountry(_userDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void adminCanCreateCountry() {
        // Act
        boolean result = _authorizationPolicy.canCreateCountry(_adminDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void guestCannotCreateCountry() {
        // Act
        boolean result = _authorizationPolicy.canCreateCountry(_guestDouble);

        // Assert
        assertFalse(result);
    }

    // ──────────── Auction viewing ────────────

    @Test
    void userCanViewAuction() {
        assertTrue(_authorizationPolicy.canViewAuction(_userDouble));
    }

    @Test
    void adminCanViewAuction() {
        assertTrue(_authorizationPolicy.canViewAuction(_adminDouble));
    }

    @Test
    void guestCannotViewAuction() {
        assertFalse(_authorizationPolicy.canViewAuction(_guestDouble));
    }

// ──────────── Bidding on auctions ────────────

    @Test
    void userCanBid() {
        assertTrue(_authorizationPolicy.canBid(_userDouble));
    }

    @Test
    void adminCanBid() {
        assertTrue(_authorizationPolicy.canBid(_adminDouble));
    }

    @Test
    void guestCannotBid() {
        assertFalse(_authorizationPolicy.canBid(_guestDouble));
    }

}