package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.BidResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionLinkProviderTest {

    private AuthorizationPolicy _authorizationPolicy;
    private AuctionLinkProvider _linkProvider;

    @BeforeEach
    void setUp() {
        _authorizationPolicy = mock(AuthorizationPolicy.class);
        _linkProvider = new AuctionLinkProvider(_authorizationPolicy);
    }

    @Test
    void getLinksUserCanSellContainsCreateAuctionLink() {
        User user = mock(User.class);
        UserId userId = mock(UserId.class);

        when(user.identity()).thenReturn(userId);
        when(userId.toString()).thenReturn("user123");

        when(_authorizationPolicy.canSell(user)).thenReturn(true);

        List<Link> links = _linkProvider.getLinks(user);

        assertTrue(links.stream()
                .anyMatch(l -> l.getRel().value().equals("create-auction")));
    }

    @Test
    void getLinksUserCannotSellReturnsEmptyList() {
        User user = mock(User.class);
        when(_authorizationPolicy.canSell(user)).thenReturn(false);

        List<Link> links = _linkProvider.getLinks(user);

        assertTrue(links.isEmpty());
    }

    @Test
    void getLinksForSpecificAuctionUserCanViewAndBidContainsSelfAndPlaceBidLinks() {
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        String auctionId = "AU-12345678";

        when(userDouble.identity()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user123");

        when(_authorizationPolicy.canViewAuction(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canBid(userDouble)).thenReturn(true);

        List<Link> links = _linkProvider.getLinks(userDouble, auctionId);

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

        when(_authorizationPolicy.canViewAuction(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canBid(userDouble)).thenReturn(false);

        // Act
        List<Link> links = _linkProvider.getLinks(userDouble, auctionId);

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

        when(_authorizationPolicy.canViewAuction(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canBid(userDouble)).thenReturn(true);

        // Act
        List<Link> links = _linkProvider.getLinks(userDouble, auctionId);

        // Assert
        assertEquals(1, links.size());
        assertEquals("place-bid", links.get(0).getRel().value());
    }

    @Test
    void getLinksForSpecificAuctionUserCannotViewOrBidReturnsEmptyList() {
        // Arrange
        User userDouble = mock(User.class);
        String auctionId = "AU-12345678";

        when(_authorizationPolicy.canViewAuction(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canBid(userDouble)).thenReturn(false);

        // Act
        List<Link> links = _linkProvider.getLinks(userDouble, auctionId);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void addBidLinksAddsPlaceBidAndAuctionLinks() {
        // Arrange
        BidResponseDTO dtoDouble = mock(BidResponseDTO.class);

        String auctionId = "AU-12345678";
        when(dtoDouble.getAuctionId()).thenReturn(auctionId);

        // Act
        _linkProvider.addBidLinks(dtoDouble);

        // Assert
        // Creating an ArgumentCaptor that records every Link object passed into dtoDouble.add(...)
        org.mockito.ArgumentCaptor<Link> captor = org.mockito.ArgumentCaptor.forClass(Link.class);

        // Verify that dtoDouble.add(...) was called exactly twice and capture the Link arguments for later inspection
        org.mockito.Mockito.verify(dtoDouble,times(2)).add(captor.capture());

        List<Link> addedLinks = captor.getAllValues();

        assertTrue(addedLinks.stream().anyMatch(l -> l.getRel().value().equals("place-bid")));
        assertTrue(addedLinks.stream().anyMatch(l -> l.getRel().value().equals("auction")));
    }

}