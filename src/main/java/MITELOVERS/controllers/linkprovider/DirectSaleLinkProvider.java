package MITELOVERS.controllers.linkprovider;

import MITELOVERS.applicationservices.UserService;
import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.DirectSaleRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.DirectSaleResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import MITELOVERS.applicationservices.ShoppingCartService;
import MITELOVERS.controllers.rest.ShoppingCartRestController;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.valueobject.DirectSaleStatus;

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
    private final UserService _userService;
    private final ShoppingCartService _shoppingCartService;

    public DirectSaleLinkProvider(AuthorizationPolicy authorizationPolicy, UserService userService, ShoppingCartService shoppingCartService) {

        _authorizationPolicy = authorizationPolicy;
        _userService = userService;
        _shoppingCartService = shoppingCartService;
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

        if (_authorizationPolicy.canListActiveDirectSales(user)) {
            links.add(
                    WebMvcLinkBuilder.linkTo(methodOn(DirectSaleRestController.class)
                                    .getAllActiveDirectSales(null))
                            .withRel("active-direct-sales")
            );
        }

        if (_authorizationPolicy.canGetDirectSale(user)) {
            links.add(
                    WebMvcLinkBuilder.linkTo(methodOn(DirectSaleRestController.class)
                            .getDirectSaleById(null,null))
                            .withRel("direct-sale")
            );
        }

        if (_authorizationPolicy.canCreateDirectSale(user)) {
            links.add(
                    linkTo(methodOn(DirectSaleRestController.class)
                            .createDirectSale(null, null))
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

    public void addResourceLinks(DirectSaleResponseDTO dto, String email) {

        User user = _userService.getUserByEmail(email);

        dto.add(
                linkTo(methodOn(DirectSaleRestController.class)
                        .getDirectSaleById(email,dto.getDirectSaleId()))
                        .withSelfRel()
        );

        if(_authorizationPolicy.canDeleteList(user)) {

            dto.add(
                    linkTo(methodOn(DirectSaleRestController.class)
                            .deleteDirectSale(null))
                            .withRel("delete")
                    );

        }

        if (_authorizationPolicy.canPostShoppingCartLines(user)
                && dto.getStatus() == DirectSaleStatus.ACTIVE
                && !dto.getSellerId().equals(email)) {

            ShoppingCart cart =
                    _shoppingCartService.findCartByUserId(user.identity());

            dto.add(
                    linkTo(methodOn(ShoppingCartRestController.class)
                            .addCartLine(
                                    email,
                                    cart.identity().toString(),
                                    null
                            ))
                            .withRel("shopping-cart")
            );
        }
    }

    public void addCollectionLinks(CollectionModel<DirectSaleResponseDTO> dtos, String email) {

        User user = _userService.getUserByEmail(email);

        dtos.add(linkTo(methodOn(DirectSaleRestController.class)
                .getAllActiveDirectSales(null))
                .withSelfRel()
        );

    }

}
