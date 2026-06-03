package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.DirectSaleRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class DirectSaleLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public DirectSaleLinkProvider(AuthorizationPolicy authorizationPolicy) {

        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canListDirectSales(user)) {
            links.add(
                    WebMvcLinkBuilder.linkTo(methodOn(DirectSaleRestController.class)
                            .getAllDirectSales())
                            .withRel("direct-sales")
            );
        }

        if (_authorizationPolicy.canCreateDirectSale(user)) {
            links.add(
                    linkTo(methodOn(DirectSaleRestController.class)
                            .createDirectSale(null))
                            .withRel("create-direct-sale")
            );
        }

        if (_authorizationPolicy.canFilterDirectSales(user)) {
            links.add(
                    linkTo(methodOn(DirectSaleRestController.class)
                            .getDirectSaleItemsByGenre(null))
                            .withRel("direct-sales-by-genre")
            );
        }

        return links;
    }
}
