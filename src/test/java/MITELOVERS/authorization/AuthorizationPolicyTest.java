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
        // Assert
        assertTrue(_authorizationPolicy.canListPublicationTypes(_userDouble));
    }

    @Test
    void adminCanListPublicationTypes() {
        // Assert
        assertTrue(_authorizationPolicy.canListPublicationTypes(_userDouble));
    }

    @Test
    void userWithNoRoleCannotListPublicationTypes() {
        // Arrange
        when(_userDouble.hasRole(Role.USER)).thenReturn(false);

        // Assert
        assertFalse(_authorizationPolicy.canListPublicationTypes(_userDouble));
    }

    @Test
    void adminCanCreatePublicationType() {
        // Arrange
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);

        // Assert
        assertTrue(_authorizationPolicy.canCreatePublicationType(_userDouble));
    }

    @Test
    void userCannotCreatePublicationType() {

        // Assert
        assertFalse(_authorizationPolicy.canCreatePublicationType(_userDouble));
    }

    @Test
    void userWithNoRoleCannotCreatePublicationType() {
        // Arrange
        when(_userDouble.hasRole(Role.USER)).thenReturn(false);

        // Assert
        assertFalse(_authorizationPolicy.canCreatePublicationType(_userDouble));
    }

    // ──────────── Edition ────────────

    @Test
    void userCanListEditions() {
        // Assert
        assertTrue(_authorizationPolicy.canListEditions(_userDouble));
    }

    @Test
    void adminCanListEditions() {
        // Arrange
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);

        // Assert
        assertTrue(_authorizationPolicy.canListEditions(_userDouble));
    }

    @Test
    void userWithNoRoleCannotListEditions() {
        // Arrange
        when(_userDouble.hasRole(Role.USER)).thenReturn(false);

        // Assert
        assertFalse(_authorizationPolicy.canListEditions(_userDouble));
    }

    @Test
    void userCanCreateEdition() {
        // Assert
        assertTrue(_authorizationPolicy.canCreateEdition(_userDouble));
    }

    @Test
    void adminCanCreateEdition() {
        // Arrange
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(true);

        // Assert
        assertTrue(_authorizationPolicy.canCreateEdition(_userDouble));
    }

    @Test
    void userWithNoRoleCannotCreateEdition() {
        // Arrange
        when(_userDouble.hasRole(Role.USER)).thenReturn(false);

        // Assert
        assertFalse(_authorizationPolicy.canCreateEdition(_userDouble));
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

    @Test
    void userCanSeePublicLists() {
        assertTrue(_authorizationPolicy.canSeePublicLists(_userDouble));
    }

    @Test
    void adminCanSeePublicLists() {
        assertTrue(_authorizationPolicy.canSeePublicLists(_adminDouble));
    }

    @Test
    void guestCannotSeePublicLists() {
        assertFalse(_authorizationPolicy.canSeePublicLists(_guestDouble));
    }

    @Test
    void userCanSeeItemsInPublicList() {
        assertTrue(_authorizationPolicy.canSeeItemsInPublicList(_userDouble));
    }

    @Test
    void adminCanSeeItemsInPublicList() {
        assertTrue(_authorizationPolicy.canSeeItemsInPublicList(_adminDouble));
    }

    @Test
    void guestCannotSeeItemsInPublicList() {
        assertFalse(_authorizationPolicy.canSeeItemsInPublicList(_guestDouble));
    }

}