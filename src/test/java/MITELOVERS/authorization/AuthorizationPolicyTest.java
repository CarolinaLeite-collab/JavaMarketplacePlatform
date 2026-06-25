package MITELOVERS.authorization;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Role;
import MITELOVERS.domain.valueobject.UserId;
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

    private UserId _userIdMock;
    private UserId _adminIdMock;
    private UserId _guestIdMock;

    private ListOfItems _listOwnedByUser;
    private ListOfItems _listOwnedByAdmin;
    private ListOfItems _publicList;

    @BeforeEach
    void setUp() {
        _authorizationPolicy = new AuthorizationPolicy();

        _userIdMock = mock(UserId.class);
        when(_userIdMock.toString()).thenReturn("user@example.com");

        _adminIdMock = mock(UserId.class);
        when(_adminIdMock.toString()).thenReturn("admin@example.com");

        _guestIdMock = mock(UserId.class);
        when(_guestIdMock.toString()).thenReturn("guest@example.com");

        _userDouble = mock(User.class);
        when(_userDouble.hasRole(Role.USER)).thenReturn(true);
        when(_userDouble.hasRole(Role.ADMIN)).thenReturn(false);
        when(_userDouble.hasRole(Role.NONREGISTRED)).thenReturn(false);
        when(_userDouble.identity()).thenReturn(_userIdMock);

        _adminDouble = mock(User.class);
        when(_adminDouble.hasRole(Role.USER)).thenReturn(false);
        when(_adminDouble.hasRole(Role.ADMIN)).thenReturn(true);
        when(_adminDouble.hasRole(Role.NONREGISTRED)).thenReturn(false);
        when(_adminDouble.identity()).thenReturn(_adminIdMock);

        _guestDouble = mock(User.class);
        when(_guestDouble.hasRole(Role.USER)).thenReturn(false);
        when(_guestDouble.hasRole(Role.ADMIN)).thenReturn(false);
        when(_guestDouble.hasRole(Role.NONREGISTRED)).thenReturn(true);
        when(_guestDouble.identity()).thenReturn(_guestIdMock);

        _listOwnedByUser = mock(ListOfItems.class);
        when(_listOwnedByUser.getUserId()).thenReturn(_userIdMock);

        _listOwnedByAdmin = mock(ListOfItems.class);
        when(_listOwnedByAdmin.getUserId()).thenReturn(_adminIdMock);

        _publicList = mock(ListOfItems.class);
        when(_publicList.isPrivate()).thenReturn(false);
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
    void canGetPublishingCompaniesUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetAllPublishingCompanies(_userDouble));
    }

    @Test
    void canGetPublishingCompanyAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetPublishingCompany(_adminDouble));
    }

    @Test
    void canGetPublishingCompanyUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetPublishingCompany(_userDouble));
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
        // ──────────── Shopping Cart ────────────
    }

    @Test
    void canGetShoppingCartUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetShoppingCart(_userDouble));
    }

    @Test
    void canGetShoppingCartAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetShoppingCart(_adminDouble));
    }

    @Test
    void canGetShoppingCartGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canGetShoppingCart(_guestDouble));
    }

    @Test
    void canPatchShoppingCartUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canPatchShoppingCart(_userDouble));
    }

    @Test
    void canPatchShoppingCartAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canPatchShoppingCart(_adminDouble));
    }

    @Test
    void canPatchShoppingCartGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canPatchShoppingCart(_guestDouble));
    }

    @Test
    void canGetShoppingCartLinesUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetShoppingCartLines(_userDouble));
    }

    @Test
    void canGetShoppingCartLinesAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetShoppingCartLines(_adminDouble));
    }

    @Test
    void canGetShoppingCartLinesGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canGetShoppingCartLines(_guestDouble));
    }

    @Test
    void canPostShoppingCartLinesUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canPostShoppingCartLines(_userDouble));
    }

    @Test
    void canPostShoppingCartLinesAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canPostShoppingCartLines(_adminDouble));
    }

    @Test
    void canPostShoppingCartLinesGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canPostShoppingCartLines(_guestDouble));
    }

    @Test
    void canGetShoppingCartLineUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetShoppingCartLine(_userDouble));
    }

    @Test
    void canGetShoppingCartLineAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetShoppingCartLine(_adminDouble));
    }

    @Test
    void canGetShoppingCartLineGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canGetShoppingCartLine(_guestDouble));
    }

    @Test
    void canDeleteShoppingCartLineUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canDeleteShoppingCartLine(_userDouble));
    }

    @Test
    void canDeleteShoppingCartLineAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canDeleteShoppingCartLine(_adminDouble));
    }

    @Test
    void canDeleteShoppingCartLineGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canDeleteShoppingCartLine(_guestDouble));
    }

    @Test
    void canGetSalesUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetSales(_userDouble));
    }

    @Test
    void canGetSalesAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetSales(_adminDouble));
    }

    @Test
    void canGetSalesGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canGetSales(_guestDouble));
    }

    @Test
    void canPostSalesUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canPostSales(_userDouble));
    }

    @Test
    void canPostSalesAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canPostSales(_adminDouble));
    }

    @Test
    void canPostSalesGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canPostSales(_guestDouble));
    }

    @Test
    void canGetSaleUserReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetSale(_userDouble));
    }

    @Test
    void canGetSaleAdminReturnsTrue() {
        assertTrue(_authorizationPolicy.canGetSale(_adminDouble));
    }

    @Test
    void canGetSaleGuestReturnsFalse() {
        assertFalse(_authorizationPolicy.canGetSale(_guestDouble));
    }

    // ──────────── Lists ───────────

    @Test
    void userCanCreateList() {
        assertTrue(_authorizationPolicy.canCreateList(_userDouble));
    }

    @Test
    void adminCanCreateList() {
        assertTrue(_authorizationPolicy.canCreateList(_adminDouble));
    }

    @Test
    void guestCannotCreateList() {
        assertFalse(_authorizationPolicy.canCreateList(_guestDouble));
    }

    @Test
    void userCanSeeOwnPrivateList() {
        when(_listOwnedByUser.isPrivate()).thenReturn(true);
        assertTrue(_authorizationPolicy.canSeeList(_userDouble, _listOwnedByUser));
    }

    @Test
    void userCannotSeeOthersPrivateList() {
        when(_listOwnedByAdmin.isPrivate()).thenReturn(true);
        assertFalse(_authorizationPolicy.canSeeList(_userDouble, _listOwnedByAdmin));
    }

    @Test
    void anyoneCanSeePublicList() {
        assertTrue(_authorizationPolicy.canSeeList(_guestDouble, _publicList));
    }

    @Test
    void userCanAddItemToOwnList() {
        assertTrue(_authorizationPolicy.canAddItemTo(_userDouble, _listOwnedByUser));
    }

    @Test
    void userCannotAddItemToOthersList() {
        assertFalse(_authorizationPolicy.canAddItemTo(_userDouble, _listOwnedByAdmin));
    }

    @Test
    void adminCanDeleteOwnList() {
        assertTrue(_authorizationPolicy.canDeleteList(_adminDouble, _listOwnedByAdmin));
    }

    @Test
    void userCannotDeleteOthersList() {
        assertFalse(_authorizationPolicy.canDeleteList(_userDouble, _listOwnedByAdmin));
    }

    @Test
    void ownerCanChangeVisibility() {
        assertTrue(_authorizationPolicy.canChangeVisibility(_userDouble, _listOwnedByUser));
    }

    @Test
    void nonOwnerCannotChangeVisibility() {
        assertFalse(_authorizationPolicy.canChangeVisibility(_userDouble, _listOwnedByAdmin));
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
    void anyoneCanSeeItemsInPublicList() {
        assertTrue(_authorizationPolicy.canSeeItemsInPublicList(_guestDouble, _publicList));
    }

    @Test
    void cannotSeeItemsInPrivateList() {
        when(_listOwnedByUser.isPrivate()).thenReturn(true);
        assertFalse(_authorizationPolicy.canSeeItemsInPublicList(_guestDouble, _listOwnedByUser));
    }

    @Test
    void userCanListGenres() {
        //Act
        boolean result = _authorizationPolicy.canListGenres(_userDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void adminCanListGenres() {
        //Act
        boolean result = _authorizationPolicy.canListGenres(_adminDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotListGenres() {
        //Act
        boolean result = _authorizationPolicy.canListGenres(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void adminCanAddGenre() {
        //Act
        boolean result = _authorizationPolicy.canAddGenre(_adminDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void userCannotAddGenre() {
        //Act
        boolean result = _authorizationPolicy.canAddGenre(_userDouble);

        //Assert
        assertFalse(result);
    }

    @Test
    void userCanViewLibrary() {
        //Act
        boolean result = _authorizationPolicy.canViewLibrary(_userDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void adminCanViewLibrary() {
        //Act
        boolean result = _authorizationPolicy.canViewLibrary(_adminDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotViewLibrary() {
        //Act
        boolean result = _authorizationPolicy.canViewLibrary(_guestDouble);

        //Assert
        assertFalse(result);
    }

    @Test
    void userCanSell() {
        //Act
        boolean result = _authorizationPolicy.canSell(_userDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void adminCanSell() {
        // Act
        boolean result = _authorizationPolicy.canSell(_adminDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotSell() {
        // Act
        boolean result = _authorizationPolicy.canSell(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanListPublications() {
        // Act
        boolean result = _authorizationPolicy.canListPublications(_userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotListPublications() {
        // Act
        boolean result = _authorizationPolicy.canListPublications(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanCreatePublication() {
        // Act
        boolean result = _authorizationPolicy.canCreatePublication(_userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotCreatePublication() {
        // Act
        boolean result = _authorizationPolicy.canCreatePublication(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanGetLibrary() {
        // Act
        boolean result = _authorizationPolicy.canGetLibrary(_userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCannotGetLibrary() {
        // Act
        boolean result = _authorizationPolicy.canGetLibrary(_adminDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userWithNoRoleCannotGetLibrary() {
        // Act
        boolean result = _authorizationPolicy.canGetLibrary(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanAddToLibrary() {
        // Act
        boolean result = _authorizationPolicy.canAddToLibrary(_userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCannotAddToLibrary() {
        // Act
        boolean result = _authorizationPolicy.canAddToLibrary(_adminDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userWithNoRoleCannotAddToLibrary() {
        // Act
        boolean result = _authorizationPolicy.canAddToLibrary(_guestDouble);

        // Assert
        assertFalse(result);
    }


    // ------------------------------------------------------------------------
    // Author Aggregate Authorization Tests
    // ------------------------------------------------------------------------

    @Test
    void userCanListAuthors() {
        // Act
        boolean result = _authorizationPolicy.canListAuthors(_userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanListAuthors() {
        // Act
        boolean result = _authorizationPolicy.canListAuthors(_adminDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotListAuthors() {
        // Act
        boolean result = _authorizationPolicy.canListAuthors(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void userCanCreateAuthor() {
        // Act
        boolean result = _authorizationPolicy.canCreateAuthor(_userDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void adminCanCreateAuthor() {
        // Act
        boolean result = _authorizationPolicy.canCreateAuthor(_adminDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void userWithNoRoleCannotCreateAuthor() {
        // Act
        boolean result = _authorizationPolicy.canCreateAuthor(_guestDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void guestCannotFilterDirectSales() {
        //Act & Assert
        assertFalse(_authorizationPolicy.canFilterDirectSales(_guestDouble));
    }

    @Test
    void userCanCreateItem() {
        //Act & Assert
        assertTrue(_authorizationPolicy.canCreateItem(_userDouble));
    }

    @Test
    void adminCanCreateItem() {
        //Act & Assert
        assertTrue(_authorizationPolicy.canCreateItem(_adminDouble));
    }

    @Test
    void guestCannotCreateItem() {
        //Act & Assert
        assertFalse(_authorizationPolicy.canCreateItem(_guestDouble));
    }

    @Test
    void userCanListItems() {
        //Act & Assert
        assertTrue(_authorizationPolicy.canListItems(_userDouble));
    }

    @Test
    void adminCanListItems() {
        //Act & Assert
        assertTrue(_authorizationPolicy.canListItems(_adminDouble));
    }

    @Test
    void guestCannotListItems() {
        //Act & Assert
        assertFalse(_authorizationPolicy.canListItems(_guestDouble));
    }

    @Test
    void userCanSeePrice() {
        //Act & Assert
        assertFalse(_authorizationPolicy.cannotSeePrice(_adminDouble));
    }

    @Test
    void adminCanSeePrice() {
        User user = mock(User.class);
        when(user.hasRole(Role.NONREGISTRED)).thenReturn(false);

        //Act & Assert
        assertFalse(_authorizationPolicy.cannotSeePrice(user));
    }

}