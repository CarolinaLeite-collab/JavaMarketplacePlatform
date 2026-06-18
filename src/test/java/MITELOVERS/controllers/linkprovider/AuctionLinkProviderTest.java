package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.BidResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionLinkProviderTest {

    // ------------------------------------------------------------
    // getLinks for /auction
    // ------------------------------------------------------------

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

    // ------------------------------------------------------------
    // getLinks for /auction/{auctionId}
    // ------------------------------------------------------------

    @Test
    void getLinksForSpecificAuctionUserCanViewAuctionContainsViewAndSelfLinks() {

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
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("view-auction")));
    }

    @Test
    void getLinksForSpecificAuctionUserCannotViewAuctionReturnsEmptyList() {
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

    // ------------------------------------------------------------
    // getLinks for /auction/{auctionId}/bids
    // ------------------------------------------------------------

    @Test
    void getBidLinksUserCanViewAuctionReturnsSelfAndViewBids() {
        // Arrange
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        String auctionId = "AU-12345678";

        when(userDouble.identity()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user123");

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(userDouble)).thenReturn(true);

        //SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getBidLinks(userDouble, auctionId);

        // Assert
        assertEquals(2, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("self")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("view-bids")));
    }

    @Test
    void getBidLinksUserCannotViewAuctionReturnsEmptyList() {
        // Arrange
        User userDouble = mock(User.class);
        String auctionId = "AU-12345678";

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(userDouble)).thenReturn(false);

        //SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getBidLinks(userDouble, auctionId);

        // Assert
        assertTrue(links.isEmpty());
    }

    // ------------------------------------------------------------
    // addBidLinks
    // ------------------------------------------------------------

    @Test
    void addBidLinksAddsAuctionLinkPointingToAuctionOptions() {
        // Arrange
        String auctionId = "AU-12345678";
        BidResponseDTO dto = new BidResponseDTO(
                "bid-1",
                auctionId,
                "buyer@aeiou.com",
                20.0,
                "EUR",
                Instant.parse("2026-06-10T10:00:00Z")
        );

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        linkProvider.addBidLinks(dto);

        // Assert
        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("auction")));
    }

}