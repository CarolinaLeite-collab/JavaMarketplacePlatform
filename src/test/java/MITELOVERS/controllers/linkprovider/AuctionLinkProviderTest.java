package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionLinkProviderTest {

    private AuthorizationPolicy _authorizationPolicy;
    private AuctionLinkProvider _linkProvider;

    @BeforeEach
    void setUp() {
        _authorizationPolicy = mock(AuthorizationPolicy.class);
        _linkProvider = new AuctionLinkProvider(_authorizationPolicy);
    }

    @Test
    void getLinks_userCanSell_containsCreateAuctionLink() {
        User user = mock(User.class);
        when(_authorizationPolicy.canSell(user)).thenReturn(true);

        List<Link> links = _linkProvider.getLinks(user);

        assertTrue(links.stream()
                .anyMatch(l -> l.getRel().value().equals("create-auction")));
    }

    @Test
    void getLinks_userCannotSell_returnsEmptyList() {
        User user = mock(User.class);
        when(_authorizationPolicy.canSell(user)).thenReturn(false);

        List<Link> links = _linkProvider.getLinks(user);

        assertTrue(links.isEmpty());
    }
}