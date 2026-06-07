package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryLinkProviderTest {

    @Mock
    private AuthorizationPolicy _authorizationPolicyDouble;

    @Mock
    private User _userDouble;

    @Mock
    private User _adminDouble;

    @Mock
    private User _guestDouble;

    @InjectMocks
    private CountryLinkProvider _sut;

    @Test
    void getLinks_userCanListCountries_addsCountriesLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_userDouble)).thenReturn(true);
        when(_authorizationPolicyDouble.canGetCountry(_userDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canCreateCountry(_userDouble)).thenReturn(false);

        // Act
        List<Link> links = _sut.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("countries", links.get(0).getRel().value());
    }

    @Test
    void getLinks_adminCanListCountries_addsCountriesLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_adminDouble)).thenReturn(true);
        when(_authorizationPolicyDouble.canGetCountry(_adminDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canCreateCountry(_adminDouble)).thenReturn(false);

        // Act
        List<Link> links = _sut.getLinks(_adminDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("countries", links.get(0).getRel().value());
    }

    @Test
    void getLinks_guestCannotListCountries_doesNotAddCountriesLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_guestDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canGetCountry(_guestDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canCreateCountry(_guestDouble)).thenReturn(false);

        // Act
        List<Link> links = _sut.getLinks(_guestDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getLinks_userCanGetCountry_addsCountryLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_userDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canGetCountry(_userDouble)).thenReturn(true);
        when(_authorizationPolicyDouble.canCreateCountry(_userDouble)).thenReturn(false);

        // Act
        List<Link> links = _sut.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("country", links.get(0).getRel().value());
    }

    @Test
    void getLinks_adminCanGetCountry_addsCountryLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_adminDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canGetCountry(_adminDouble)).thenReturn(true);
        when(_authorizationPolicyDouble.canCreateCountry(_adminDouble)).thenReturn(false);

        // Act
        List<Link> links = _sut.getLinks(_adminDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("country", links.get(0).getRel().value());
    }

    @Test
    void getLinks_guestCannotGetCountry_doesNotAddCountryLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_guestDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canGetCountry(_guestDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canCreateCountry(_guestDouble)).thenReturn(false);

        // Act
        List<Link> links = _sut.getLinks(_guestDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getLinks_userCanCreateCountry_addsCreateCountryLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_userDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canGetCountry(_userDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canCreateCountry(_userDouble)).thenReturn(true);

        // Act
        List<Link> links = _sut.getLinks(_userDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("create-country", links.get(0).getRel().value());
    }

    @Test
    void getLinks_adminCanCreateCountry_addsCreateCountryLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_adminDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canGetCountry(_adminDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canCreateCountry(_adminDouble)).thenReturn(true);

        // Act
        List<Link> links = _sut.getLinks(_adminDouble);

        // Assert
        assertEquals(1, links.size());
        assertEquals("create-country", links.get(0).getRel().value());
    }

    @Test
    void getLinks_guestCannotCreateCountry_doesNotAddCreateCountryLink() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_guestDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canGetCountry(_guestDouble)).thenReturn(false);
        when(_authorizationPolicyDouble.canCreateCountry(_guestDouble)).thenReturn(false);

        // Act
        List<Link> links = _sut.getLinks(_guestDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getLinks_userWithAllPermissions_addsAllThreeLinks() {
        // Arrange
        when(_authorizationPolicyDouble.canListCountries(_userDouble)).thenReturn(true);
        when(_authorizationPolicyDouble.canGetCountry(_userDouble)).thenReturn(true);
        when(_authorizationPolicyDouble.canCreateCountry(_userDouble)).thenReturn(true);

        // Act
        List<Link> links = _sut.getLinks(_userDouble);

        // Assert
        assertEquals(3, links.size());
        assertEquals("countries", links.get(0).getRel().value());
        assertEquals("country", links.get(1).getRel().value());
        assertEquals("create-country", links.get(2).getRel().value());
    }

}