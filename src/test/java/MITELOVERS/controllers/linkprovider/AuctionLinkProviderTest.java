package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.BidResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void getLinksForSpecificAuctionUserCanViewAndBidContainsSelfAndPlaceBidLinks() {

        //Arrange
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        String auctionId = "AU-12345678";

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(userDouble.identity()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user123");

        when(authorizationPolicy.canViewAuction(userDouble)).thenReturn(true);
        when(authorizationPolicy.canBid(userDouble)).thenReturn(true);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(userDouble, auctionId);

        // Assert
        assertEquals(2, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("self")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("place-bid")));
    }

    @Test
    void getLinksForSpecificAuctionUserCanViewButNotBidContainsOnlySelfLink() {
        // Arrange
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        String auctionId = "AU-12345678";

        when(userDouble.identity()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user123");

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canViewAuction(userDouble)).thenReturn(true);
        when(authorizationPolicy.canBid(userDouble)).thenReturn(false);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(userDouble, auctionId);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.get(0).getRel().value().equals("self"));
    }

    @Test
    void getLinksForSpecificAuctionUserCanBidButNotViewContainsOnlyPlaceBidLink() {
        // Arrange
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        String auctionId = "AU-12345678";

        when(userDouble.identity()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user123");

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canViewAuction(userDouble)).thenReturn(false);
        when(authorizationPolicy.canBid(userDouble)).thenReturn(true);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(userDouble, auctionId);

        // Assert
        assertEquals(1, links.size());
        assertEquals("place-bid", links.get(0).getRel().value());
    }

    @Test
    void getLinksForSpecificAuctionUserCannotViewOrBidReturnsEmptyList() {
        // Arrange
        User userDouble = mock(User.class);
        String auctionId = "AU-12345678";

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canViewAuction(userDouble)).thenReturn(false);
        when(authorizationPolicy.canBid(userDouble)).thenReturn(false);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(userDouble, auctionId);

        // Assert
        assertTrue(links.isEmpty());
    }


}