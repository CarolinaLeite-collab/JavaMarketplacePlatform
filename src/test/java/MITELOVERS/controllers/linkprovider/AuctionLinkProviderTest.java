package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.AuctionNoPriceResponseDTO;
import MITELOVERS.dto.response.AuctionResponseDTO;
import MITELOVERS.dto.response.BidResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;

import java.time.Instant;
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
        when(authorizationPolicy.cannotSeePrice(user)).thenReturn(false);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertTrue(links.stream()
                .anyMatch(l -> l.getRel().value().equals("auctions")));
    }

    @Test
    void getLinksUserCannotSellReturnsEmptyList() {
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canSell(user)).thenReturn(false);
        when(authorizationPolicy.cannotSeePrice(user)).thenReturn(false);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getLinksUserCannotSeePriceContainsAuctionsWithoutPriceLink() {
        // Arrange
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canSell(user)).thenReturn(false);
        when(authorizationPolicy.cannotSeePrice(user)).thenReturn(true);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertTrue(links.stream()
                .anyMatch(l -> l.getRel().value().equals("auctions-without-price")));
    }

    @Test
    void getLinksUserCanSellAndCannotSeePriceContainsBothLinks() {
        // Arrange
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canSell(user)).thenReturn(true);
        when(authorizationPolicy.cannotSeePrice(user)).thenReturn(true);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertTrue(links.stream()
                .anyMatch(l -> l.getRel().value().equals("auctions")));
        assertTrue(links.stream()
                .anyMatch(l -> l.getRel().value().equals("auctions-without-price")));
    }

    @Test
    void addLinksForAuctionNoPriceAddsAuctionsAndAuctionsWithoutPriceLinks() {
        AuctionNoPriceResponseDTO dto = new AuctionNoPriceResponseDTO(
                "AU-12345678",
                List.of("ABCDEF1234"),
                Instant.parse("2026-06-10T10:00:00Z"),
                Instant.parse("2026-06-20T10:00:00Z"),
                "pedro@aeiou.com"
        );

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        linkProvider.addLinksForAuction(dto);

        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("auctions")));
        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("auctions-without-price")));
    }

    @Test
    void getAllowedMethodsUserCanViewAuctionContainsGetAndOptions() {
        // Arrange
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canViewAuction(user)).thenReturn(true);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<HttpMethod> methods = linkProvider.getAllowedMethodsForSpecificAuction(user);

        // Assert
        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertTrue(methods.contains(HttpMethod.GET));
        assertEquals(2, methods.size());
    }

    @Test
    void getAllowedMethodsUserCannotViewAuctionContainsOnlyOptions() {
        // Arrange
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canViewAuction(user)).thenReturn(false);

        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<HttpMethod> methods = linkProvider.getAllowedMethodsForSpecificAuction(user);

        // Assert
        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertFalse(methods.contains(HttpMethod.GET));
        assertEquals(1, methods.size());
    }

    @Test
    void getAllowedMethodsUserCanSellContainsPost() {
        // Arrange
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canSell(user)).thenReturn(true);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<HttpMethod> links = linkProvider.getAllowedMethods(user);

        // Assert
        assertTrue(links.contains(HttpMethod.POST));
        assertTrue(links.contains(HttpMethod.OPTIONS));
    }

    @Test
    void getAllowedMethodsUserCannotSellContainsOnlyOptions() {
        // Arrange
        User user = mock(User.class);
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);

        when(authorizationPolicy.canSell(user)).thenReturn(false);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<HttpMethod> links = linkProvider.getAllowedMethods(user);

        assertFalse(links.contains(HttpMethod.POST));
        assertTrue(links.contains(HttpMethod.OPTIONS));
    }

    @Test
    void getLinksForSpecificAuctionUserCanViewAuctionContainsGetAndOptions() {

        //Arrange
        User userDouble = mock(User.class);

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(userDouble)).thenReturn(true);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<HttpMethod> methods = linkProvider.getAllowedMethodsForSpecificAuction(userDouble);

        // Assert
        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertTrue(methods.contains(HttpMethod.GET));
        assertEquals(2, methods.size());
    }

    @Test
    void getAllowedMethodsForSpecificAuctionUserCannotViewAuctionContainsOnlyOptions() {
        // Arrange
        User userDouble = mock(User.class);

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(userDouble)).thenReturn(false);

        // SUT
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        // Act
        List<HttpMethod> methods = linkProvider.getAllowedMethodsForSpecificAuction(userDouble);

        // Assert
        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertFalse(methods.contains(HttpMethod.GET));
        assertEquals(1, methods.size());
    }

    // ------------------------------------------------------------
    // addLinksForAuction
    // ------------------------------------------------------------

    @Test
    void addLinksForAuctionAddsSelfAndBidsLinks() {
        String auctionId = "AU-12345678";

        AuctionResponseDTO dto = new AuctionResponseDTO(
                auctionId,
                List.of("ABCDEF1234"),
                10.0,
                25.0,
                50.0,
                "EUR",
                Instant.parse("2026-06-10T10:00:00Z"),
                Instant.parse("2026-06-20T10:00:00Z"),
                "pedro@aeiou.com",
                10.0
        );

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        linkProvider.addLinksForAuction(dto);

        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("self")));
        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("bids")));
    }

    // ------------------------------------------------------------
    // getAllowedMethodsForBids
    // ------------------------------------------------------------

    @Test
    void getAllowedMethodsForBidsUserCanBidButIsSellerContainsNoPost() {
        User user = mock(User.class);
        Auction auctionDouble = mock(Auction.class);
        UserId userIdDouble = mock(UserId.class);

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(user)).thenReturn(true);
        when(authorizationPolicy.canBid(user)).thenReturn(true);
        when(auctionDouble.getSeller()).thenReturn(userIdDouble);
        when(user.identity()).thenReturn(userIdDouble);

        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        List<HttpMethod> methods = linkProvider.getAllowedMethodsForBids(user, auctionDouble);

        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertTrue(methods.contains(HttpMethod.GET));
        assertFalse(methods.contains(HttpMethod.POST));
        assertEquals(2, methods.size());
    }

    @Test
    void getAllowedMethodsForBidsUserCanViewAndBidContainsOptionsGetAndPost() {
        User user = mock(User.class);
        Auction auctionDouble = mock(Auction.class);
        UserId sellerIdDouble = mock(UserId.class);
        UserId userIdentityDouble = mock(UserId.class);

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(user)).thenReturn(true);
        when(authorizationPolicy.canBid(user)).thenReturn(true);
        when(auctionDouble.getSeller()).thenReturn(sellerIdDouble);
        when(user.identity()).thenReturn(userIdentityDouble);

        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        List<HttpMethod> methods = linkProvider.getAllowedMethodsForBids(user, auctionDouble);

        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertTrue(methods.contains(HttpMethod.GET));
        assertTrue(methods.contains(HttpMethod.POST));
        assertEquals(3, methods.size());
    }

    @Test
    void getAllowedMethodsForBidsUserCanViewButCannotBidContainsOptionsAndGetOnly() {
        User user = mock(User.class);
        Auction auctionDouble = mock(Auction.class);
        UserId userIdDouble = mock(UserId.class);

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(user)).thenReturn(true);
        when(authorizationPolicy.canBid(user)).thenReturn(false);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");
        when(auctionDouble.getSeller()).thenReturn(userIdDouble);

        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        List<HttpMethod> methods = linkProvider.getAllowedMethodsForBids(user, auctionDouble);

        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertTrue(methods.contains(HttpMethod.GET));
        assertFalse(methods.contains(HttpMethod.POST));
        assertEquals(2, methods.size());
    }

    @Test
    void getAllowedMethodsForBidsUserCannotViewButCanBidContainsOptionsAndPostOnly() {
        User user = mock(User.class);
        Auction auctionDouble = mock(Auction.class);
        UserId sellerIdDouble = mock(UserId.class);
        UserId userIdentityDouble = mock(UserId.class);

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(user)).thenReturn(false);
        when(authorizationPolicy.canBid(user)).thenReturn(true);
        when(auctionDouble.getSeller()).thenReturn(sellerIdDouble);
        when(user.identity()).thenReturn(userIdentityDouble);

        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        List<HttpMethod> methods = linkProvider.getAllowedMethodsForBids(user, auctionDouble);

        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertFalse(methods.contains(HttpMethod.GET));
        assertTrue(methods.contains(HttpMethod.POST));
        assertEquals(2, methods.size());
    }

    @Test
    void getAllowedMethodsForBidsUserCannotViewAndCannotBidContainsOnlyOptions() {
        User user = mock(User.class);
        Auction auctionDouble = mock(Auction.class);
        UserId userIdDouble = mock(UserId.class);

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        when(authorizationPolicy.canViewAuction(user)).thenReturn(false);
        when(authorizationPolicy.canBid(user)).thenReturn(false);
        when(userIdDouble.toString()).thenReturn("pedro@aeiou.com");
        when(auctionDouble.getSeller()).thenReturn(userIdDouble);

        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        List<HttpMethod> methods = linkProvider.getAllowedMethodsForBids(user, auctionDouble);

        assertTrue(methods.contains(HttpMethod.OPTIONS));
        assertFalse(methods.contains(HttpMethod.GET));
        assertFalse(methods.contains(HttpMethod.POST));
        assertEquals(1, methods.size());
    }

    // ------------------------------------------------------------
    // addLinksForCreatedBid
    // ------------------------------------------------------------

    @Test
    void addLinksForCreatedBidAddsAuctionAndBidsLinks() {
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

        linkProvider.addLinksForCreatedBid(dto);

        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("auction")));
        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("bids")));
    }

    // ------------------------------------------------------------
    // addLinksForBidInCollection
    // ------------------------------------------------------------

    @Test
    void addLinksForBidInCollectionAddsOnlyAuctionLink() {
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

        linkProvider.addLinksForBidInCollection(dto);

        assertTrue(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("auction")));
        assertFalse(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("bids")));
        assertFalse(dto.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("self")));
    }

    // ------------------------------------------------------------
    // addLinksForBidCollection
    // ------------------------------------------------------------

    @Test
    void addLinksForBidCollectionAddsSelfAndAuctionLinksToCollection() {
        String auctionId = "AU-12345678";

        BidResponseDTO dto1 = new BidResponseDTO(
                "bid-1",
                auctionId,
                "buyer1@aeiou.com",
                20.0,
                "EUR",
                Instant.parse("2026-06-10T10:00:00Z")
        );

        BidResponseDTO dto2 = new BidResponseDTO(
                "bid-2",
                auctionId,
                "buyer2@aeiou.com",
                30.0,
                "EUR",
                Instant.parse("2026-06-11T10:00:00Z")
        );

        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        AuctionLinkProvider linkProvider = new AuctionLinkProvider(authorizationPolicy);

        CollectionModel<BidResponseDTO> collectionModel =
                linkProvider.addLinksForBidCollection(List.of(dto1, dto2), auctionId);

        assertEquals(2, collectionModel.getContent().size());
        assertTrue(collectionModel.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("self")));
        assertTrue(collectionModel.getLinks().stream()
                .anyMatch(l -> l.getRel().value().equals("auction")));
    }
}