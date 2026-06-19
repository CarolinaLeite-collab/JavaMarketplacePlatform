package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ShoppingCartService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.ShoppingCartLinkProvider;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for exposing Shopping Cart-related endpoints
 * via HTTP endpoints.
 */

@RestController
@RequestMapping("shopping-carts")
public class ShoppingCartRestController {

    private final ShoppingCartService _shoppingCartService;
    private final UserService _userService;
    private final ShoppingCartLinkProvider _shoppingCartLinkProvider;

    public ShoppingCartRestController(ShoppingCartService shoppingCartService,
                                      UserService userService,
                                      ShoppingCartLinkProvider shoppingCartLinkProvider) {
        _shoppingCartService = shoppingCartService;
        _userService = userService;
        _shoppingCartLinkProvider = shoppingCartLinkProvider;
    }

    @RequestMapping(path= "/{cartId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForCart(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if(!email.isBlank()) {

            User user = _userService.getUserByEmail(email);
            ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);
            allowedMethods = _shoppingCartLinkProvider.getAllowedMethodsForCart(user, cart);

        }

            return ResponseEntity
                    .ok()
                    .allow(allowedMethods.toArray(new HttpMethod[0]))
                    .build();

    }

    @RequestMapping(path= "/{cartId}/shopping-cart-lines", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForCartLines(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if(!email.isBlank()) {

            User user = _userService.getUserByEmail(email);
            ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);
            allowedMethods = _shoppingCartLinkProvider.getAllowedMethodsForCartLines(user, cart);

        }

        return ResponseEntity
                .ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();

    }

    @RequestMapping(path= "/{cartId}/shopping-cart-lines/{cartLineId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForCartLine(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId,
            @PathVariable String cartLineId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if(!email.isBlank()) {

            User user = _userService.getUserByEmail(email);
            ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);
            ShoppingCartLine cartLine = _shoppingCartService.findCartLineByUserId(cartId, cartLineId);
            allowedMethods = _shoppingCartLinkProvider.getAllowedMethodsForCartLine(user, cart, cartLine);

        }

        return ResponseEntity
                .ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();

    }

}
