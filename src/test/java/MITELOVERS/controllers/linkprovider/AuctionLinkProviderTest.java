package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionLinkProviderTest {

    @Test
    void getLinksUserCanSellContainsCreateAuctionLink() {
        // Arrange
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canSell(user)).thenReturn(true);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertTrue(links.stream()
                .anyMatch(l -> l.getRel().value().equals("create-auction")));
    }

    @Test
    void getLinksUserCannotSellReturnsEmptyList() {
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canSell(user)).thenReturn(false);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertTrue(links.isEmpty());
    }
}