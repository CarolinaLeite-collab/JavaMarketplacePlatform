package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.DirectSaleRestController;
import MITELOVERS.controllers.rest.SaleRestController;
import MITELOVERS.controllers.rest.ShoppingCartRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ShoppingCartId;
import MITELOVERS.domain.valueobject.ShoppingCartLineId;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.dto.response.ShoppingCartLineResponseDTO;
import MITELOVERS.dto.response.ShoppingCartResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * HATEOAS link provider for the ShoppingCart bounded context.
 * Computes allowed HTTP methods for cart, cart lines, and cart line endpoints
 * based on the current user's role and ownership, and populates representation
 * models with navigational links — including a conditional checkout link when
 * the cart has lines. Implements {@link RootLinkProvider} to expose the
 * shopping cart discovery link during the root bootstrap call.
 */

@Component
public class ShoppingCartLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public ShoppingCartLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    public List<HttpMethod> getAllowedMethodsForCarts(User user) {
        List<HttpMethod> methods = new ArrayList<>();

        if (_authorizationPolicy.canGetShoppingCart(user)) {
            methods.add(HttpMethod.GET);
        }

        methods.add(HttpMethod.OPTIONS);
        return methods;
    }


    public List <HttpMethod> getAllowedMethodsForCart(User user, ShoppingCart shoppingCart) {

        List<HttpMethod> methods = new ArrayList<>();

        if(user.identity().equals(shoppingCart.getBuyerId())) {

            if (_authorizationPolicy.canGetShoppingCart(user)) {
                methods.add(HttpMethod.GET);
            }

            if (_authorizationPolicy.canPatchShoppingCart(user)) {
                methods.add(HttpMethod.PATCH);
            }

        }

        methods.add(HttpMethod.OPTIONS);

        return methods;

    }

    public List <HttpMethod> getAllowedMethodsForCartLines(User user, ShoppingCart shoppingCart) {

        List<HttpMethod> methods = new ArrayList<>();

        if(user.identity().equals(shoppingCart.getBuyerId())) {

            if(_authorizationPolicy.canPostShoppingCartLines(user)) {
                methods.add(HttpMethod.POST);
            }

        }

        methods.add(HttpMethod.OPTIONS);

        return methods;

    }

    public List <HttpMethod> getAllowedMethodsForCartLine(User user, ShoppingCart shoppingCart, ShoppingCartLine shoppingCartLine) {

        List<HttpMethod> methods = new ArrayList<>();

        if(user.identity().equals(shoppingCart.getBuyerId())) {

            if((shoppingCart.getCartLines()).contains(shoppingCartLine)) {

                if (_authorizationPolicy.canGetShoppingCartLine(user)) {
                    methods.add(HttpMethod.GET);
                }

                if (_authorizationPolicy.canDeleteShoppingCartLine(user)) {
                    methods.add(HttpMethod.DELETE);
                }

            }

        }

        methods.add(HttpMethod.OPTIONS);

        return methods;

    }

    public boolean addLinksForUserCartDiscovery (RepresentationModel<?> model, UserId userId, ShoppingCartId cartId) {

         model.add(linkTo(methodOn(ShoppingCartRestController.class)
                .getUserCart(userId.toString(), cartId.toString())).withSelfRel());

         return true;

    }

    public boolean addLinksForUserCart(ShoppingCartResponseDTO dto, UserId userId, ShoppingCartId cartId, ShoppingCart cart) {

        dto.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCart(userId.toString(), cartId.toString())).withSelfRel());

        if (!cart.getCartLines().isEmpty()) {

            dto.add(linkTo(methodOn(SaleRestController.class).createSaleFromCart(userId.toString(), null)).withRel("sale"));

            for (ShoppingCartLine cartLine : cart.getCartLines()) {

                String cartLineId = cartLine.identity().toString();

                dto.add(linkTo(methodOn(ShoppingCartRestController.class)
                        .getUserCartLine(userId.toString(), cartId.toString(), cartLineId)).withRel("shopping-cart-line"));
            }
        }

        return true;

    }

    public boolean addLinksForUserCartLine(ShoppingCartLineResponseDTO dto, UserId userId, ShoppingCartId cartId, ShoppingCartLineId cartLineId, DirectSaleId directSaleId) {

        dto.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCartLine(userId.toString(), cartId.toString(), cartLineId.toString())).withSelfRel());
        dto.add(linkTo(methodOn(DirectSaleRestController.class).getDirectSaleById(userId.toString(),directSaleId.toString())).withRel("direct-sale"));

        return true;
    }

    public boolean addLinksForDeleteUserCartLine(RepresentationModel<?> model, UserId userId, ShoppingCartId cartId) {

        model.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCart(userId.toString(), cartId.toString())).withRel("shopping-cart"));

        return true;

    }

    public boolean addLinksForCreateUserCartLine(RepresentationModel<?> model, UserId userId, ShoppingCartId cartId, ShoppingCartLineId cartLineId) {

        model.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCartLine(userId.toString(),cartId.toString(),cartLineId.toString())).withSelfRel());
        model.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCart(userId.toString(), cartId.toString())).withRel("shopping-cart"));

        return true;
    }

    @Override
    public List<Link> getLinks(User user) {

        if (_authorizationPolicy.canGetShoppingCart(user)) {
            return List.of(
                    linkTo(methodOn(ShoppingCartRestController.class)
                            .getUserCartLink(null)).withRel("shopping-cart")
            );
        }

        return List.of();
    }

}
