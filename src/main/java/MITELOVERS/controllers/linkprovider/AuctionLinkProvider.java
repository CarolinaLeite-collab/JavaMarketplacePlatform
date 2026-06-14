package MITELOVERS.controllers.linkprovider;

import org.springframework.hateoas.Link;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.AuctionRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AuctionLinkProvider implements RootLinkProvider {
    private final AuthorizationPolicy _authorizationPolicy;

    public AuctionLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canSell(user)) {
            links.add(linkTo(AuctionRestController.class)
                    .withRel("create-auction"));
        }

        return links;
    }
}
