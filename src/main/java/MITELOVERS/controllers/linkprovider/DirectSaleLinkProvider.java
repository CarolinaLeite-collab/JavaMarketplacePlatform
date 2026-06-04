package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.DirectSaleRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides root-level HATEOAS links related to direct sales.
 *
 * <p>
 * This component is responsible for adding the {@link DirectSale}
 * link to the root response when the authenticated user has at least one
 * permission related to direct sale operations.
 * </p>
 *
 * <p>
 * The link is only included if the user is authorized to perform actions.
 * </p>
 */

@Component
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

        if (_authorizationPolicy.canGetDirectSale(user)) {
            links.add(
                    WebMvcLinkBuilder.linkTo(methodOn(DirectSaleRestController.class)
                            .getDirectSaleById(null))
                            .withRel("direct-sale")
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

        if(_authorizationPolicy.cannotSeePrice(user)) {
            links.add(
                    linkTo(methodOn(DirectSaleRestController.class).getDirectSalesWithoutPrice()).withRel("direct-sales-without-price")
            );
        }

        return links;
    }
}
