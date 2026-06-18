package MITELOVERS.controllers.linkprovider;

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


    // Links for a specific auction (OPTIONS /auctions/{auctionId})

    public List<Link> getLinks(User user, String auctionId) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canViewAuction(user)) {
            links.add(linkTo(methodOn(AuctionRestController.class)
                    .optionsForSpecificAuction(auctionId, user.identity().toString()))
                    .withSelfRel());

            links.add(linkTo(methodOn(AuctionRestController.class)
                    .getAuctionById(auctionId.toString()))
                    .withRel("view-auction"));
        }

        if (_authorizationPolicy.canBid(user)) {
            links.add(linkTo(methodOn(AuctionRestController.class)
                    .placeBid(auctionId, user.identity().toString(), null))
                    .withRel("place-bid"));
        }

        return links;
    }

    // Links for the bids of a specific auction (OPTIONS /auctions/{auctionId}/bids)

    public List<Link> getBidLinks(User user, String auctionId) {
        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canViewAuction(user)) {
            links.add(linkTo(methodOn(AuctionRestController.class)
                    .optionsForBids(auctionId, user.identity().toString()))
                    .withSelfRel());

            links.add(linkTo(methodOn(AuctionRestController.class)
                    .getBidsForAuction(auctionId))
                    .withRel("view-bids"));
        }

        return links;
    }


    // Links attached directly to a bid response (e.g., after POST /auctions/{auctionId}/bids).

    public void addBidLinks(BidResponseDTO dto) {

        String auctionId = dto.getAuctionId();

        // Link back to the OPTIONS for auction that was just bid on
        Link auctionLink = linkTo(methodOn(AuctionRestController.class)
                .optionsForSpecificAuction(auctionId, null))
                .withRel("auction");

        dto.add(auctionLink);
    }

}
