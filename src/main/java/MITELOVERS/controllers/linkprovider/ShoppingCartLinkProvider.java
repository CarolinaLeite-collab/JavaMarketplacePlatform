package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Link> getLinks(User user) {
        return List.of();
    }

}
