package MITELOVERS.controllers.linkprovider;

import MITELOVERS.dto.response.AuctionResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.AuctionRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.BidResponseDTO;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides auction-related HATEOAS links according to the permissions
 * of the authenticated user.
 * <p>
 * This component implements {@link RootLinkProvider} and is responsible for
 * determining which auction and bid resources should be exposed to a given user.
 * The available links are generated based on the user's authorization level,
 * as defined by the {@link AuthorizationPolicy}.
 * </p>
 *
 */

@Component
public class AuctionLinkProvider implements RootLinkProvider {
    private final AuthorizationPolicy _authorizationPolicy;

    public AuctionLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    // Links for the /auctions root resource (OPTIONS /auctions)
    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canSell(user)) {
            links.add(linkTo(AuctionRestController.class)
                    .withRel("create-auction"));
        }

        return links;
    }

    // Allowed HTTP methods for /auctions (OPTIONS /auctions)
    public List<HttpMethod> getAllowedMethods(User user) {
        List<HttpMethod> methods = new ArrayList<>();

        methods.add(HttpMethod.OPTIONS);

        if(_authorizationPolicy.canSell(user)) {
            methods.add(HttpMethod.POST);
        }

        return methods;
    }


    // Allowed HTTP methods for /auctions/{auctionId}

    public List<HttpMethod> getAllowedMethodsForSpecificAuction(User user, String auctionId) {

        List<HttpMethod> methods = new ArrayList<>();

        methods.add(HttpMethod.OPTIONS);

        if (_authorizationPolicy.canViewAuction(user)) {
            methods.add(HttpMethod.GET);
        }

        return methods;
    }

    // Adds links to an auction representation returned by GET /auctions/{auctionId}

    public void addLinksForAuction(AuctionResponseDTO dto, String auctionId) {
        dto.add(linkTo(methodOn(AuctionRestController.class)
                .getAuctionById(auctionId))
                .withSelfRel());

        dto.add(linkTo(methodOn(AuctionRestController.class)
                .getBidsForAuction(auctionId))
                .withRel("bids"));
    }

    // Allowed HTTP methods for /auctions/{auctionId}/bids

    public List<HttpMethod> getAllowedMethodsForBids(User user, String auctionId) {
        List<HttpMethod> methods = new ArrayList<>();

        methods.add(HttpMethod.OPTIONS);

        if (_authorizationPolicy.canViewAuction(user)) {
            methods.add(HttpMethod.GET);
        }

        if (_authorizationPolicy.canBid(user)) {
            methods.add(HttpMethod.POST);
        }

        return methods;
    }

    // Add links to a bid representation returned by POST /auctions/{auctionId}/bids.

    public void addLinksForCreatedBid(BidResponseDTO dto) {

        dto.add(linkTo(methodOn(AuctionRestController.class)
                .getAuctionById(dto.getAuctionId()))
                .withRel("auction"));

        dto.add(linkTo(methodOn(AuctionRestController.class)
                .getBidsForAuction(dto.getAuctionId()))
                .withRel("bids"));
    }

    // Add links to bids in a bid collection returned by GET /auctions/{auctionId}/bids

    public void addLinksForBidInCollection(BidResponseDTO dto) {

        dto.add(linkTo(methodOn(AuctionRestController.class)
                .getAuctionById(dto.getAuctionId()))
                .withRel("auction"));
    }

    // Add links to the whole bid collection returned by GET /auctions/{auctionId}/bids

    public CollectionModel<BidResponseDTO> addLinksForBidCollection(List<BidResponseDTO> bids, String auctionId) {
        return CollectionModel.of(
                bids,
                linkTo(methodOn(AuctionRestController.class)
                        .getBidsForAuction(auctionId))
                        .withSelfRel(),
                linkTo(methodOn(AuctionRestController.class)
                        .getAuctionById(auctionId))
                        .withRel("auction")
        );
    }

}
