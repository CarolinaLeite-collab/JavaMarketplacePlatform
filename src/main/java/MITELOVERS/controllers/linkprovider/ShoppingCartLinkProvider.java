package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.DirectSaleRestController;
import MITELOVERS.controllers.rest.ShoppingCartRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
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

@Component
public class ShoppingCartLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public ShoppingCartLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
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

            if (_authorizationPolicy.canGetShoppingCartLines(user)) {
                methods.add(HttpMethod.GET);
            }

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

    public boolean addLinksForUserCart(ShoppingCartResponseDTO dto, String email, String cartId, ShoppingCart cart) {

        dto.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCart(email, cartId)).withSelfRel());

        if (!cart.getCartLines().isEmpty()) {

            for (ShoppingCartLine cartLine : cart.getCartLines()) {

                String cartLineId = cartLine.identity().toString();

                dto.add(linkTo(methodOn(ShoppingCartRestController.class)
                        .getUserCartLine(email, cartId, cartLineId)).withRel("shopping-cart-line"));
            }
        }

        return true;

    }

    public boolean addLinksForUserCartLine(ShoppingCartLineResponseDTO dto, String email, String cartId, String cartLineId, String directSaleId) {

        dto.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCartLine(email, cartId, cartLineId)).withSelfRel());
        dto.add(linkTo(methodOn(DirectSaleRestController.class).getDirectSaleById(directSaleId)).withRel("direct-sale"));

        return true;
    }

    public boolean addLinksForDeleteUserCartLine(RepresentationModel<?> model, String email, String cartId) {

        model.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCart(email, cartId)).withRel("shopping-cart"));

        return true;

    }

    public boolean addLinksForCreateUserCartLine(RepresentationModel<?> model, String email, String cartId, String cartLineId) {

        model.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCartLine(email,cartId,cartLineId)).withSelfRel());
        model.add(linkTo(methodOn(ShoppingCartRestController.class).getUserCart(email, cartId)).withRel("shopping-cart"));

        return true;
    }

    @Override
    public List<Link> getLinks(User user) {
        return List.of();
    }

}
