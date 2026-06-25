package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListOfItemsLinkProviderTest {

    @Mock
    AuthorizationPolicy _authorizationPolicy;

    @Mock
    User _user;

    @Mock
    UserId _userId;

    @InjectMocks
    ListOfItemsLinkProvider _linkProvider;

    @BeforeEach
    void setup() {
        when(_userId.toString()).thenReturn("john@example.com");
        when(_user.identity()).thenReturn(_userId);
    }

    @Test
    void getLinks_shouldAlwaysContainCollectionLink() {
        when(_authorizationPolicy.canCreateList(_user)).thenReturn(false);

        List<Link> links = _linkProvider.getLinks(_user);

        assertTrue(
                links.stream().anyMatch(l -> l.getRel().value().equals("collection")),
                "Expected 'collection' link to always be present"
        );
    }

    @Test
    void getLinks_shouldAlwaysContainPublicListsLink() {
        when(_authorizationPolicy.canCreateList(_user)).thenReturn(false);

        List<Link> links = _linkProvider.getLinks(_user);

        assertTrue(
                links.stream().anyMatch(l -> l.getRel().value().equals("public-lists")),
                "Expected 'public-lists' link to always be present"
        );
    }

    @Test
    void getLinks_shouldIncludeCreateList_whenUserCanCreate() {
        when(_authorizationPolicy.canCreateList(_user)).thenReturn(true);

        List<Link> links = _linkProvider.getLinks(_user);

        assertTrue(
                links.stream().anyMatch(l -> l.getRel().value().equals("create-list")),
                "Expected 'create-list' link when user can create lists"
        );
    }

    @Test
    void getLinks_shouldNotIncludeCreateList_whenUserCannotCreate() {
        when(_authorizationPolicy.canCreateList(_user)).thenReturn(false);

        List<Link> links = _linkProvider.getLinks(_user);

        assertFalse(
                links.stream().anyMatch(l -> l.getRel().value().equals("create-list")),
                "Did not expect 'create-list' link when user cannot create lists"
        );
    }

    @Test
    void getLinks_shouldHaveCorrectNumberOfLinks_whenUserHasNoPermissions() {
        when(_authorizationPolicy.canCreateList(_user)).thenReturn(false);

        List<Link> links = _linkProvider.getLinks(_user);

        assertEquals(2, links.size(), "Expected exactly 2 links: collection + public-lists");
    }

    @Test
    void getLinks_shouldHaveCorrectNumberOfLinks_whenUserHasAllPermissions() {
        when(_authorizationPolicy.canCreateList(_user)).thenReturn(true);

        List<Link> links = _linkProvider.getLinks(_user);

        assertEquals(3, links.size(), "Expected 3 links: collection + create-list + public-lists");
    }

}
