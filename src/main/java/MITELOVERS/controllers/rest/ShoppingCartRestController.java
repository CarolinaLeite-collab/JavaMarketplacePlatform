package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ShoppingCartService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.ShoppingCartLinkProvider;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.request.AddCartLineRequestDTO;
import MITELOVERS.dto.response.ShoppingCartLineResponseDTO;
import MITELOVERS.dto.response.ShoppingCartResponseDTO;
import MITELOVERS.mapper.ShoppingCartLineResponseDTOMapper;
import MITELOVERS.mapper.ShoppingCartResponseDTOMapper;
import jakarta.validation.Valid;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for exposing Shopping Cart-related endpoints
 * via HTTP endpoints.
 */

@Validated
@RestController
@RequestMapping("shopping-carts")
public class ShoppingCartRestController {

    private final ShoppingCartService _shoppingCartService;
    private final UserService _userService;
    private final ShoppingCartLinkProvider _shoppingCartLinkProvider;
    private ShoppingCartResponseDTOMapper _cartMapper;
    private ShoppingCartLineResponseDTOMapper _cartLineMapper;

    public ShoppingCartRestController(ShoppingCartService shoppingCartService,
                                      UserService userService,
                                      ShoppingCartLinkProvider shoppingCartLinkProvider,
                                      ShoppingCartResponseDTOMapper cartMapper,
                                      ShoppingCartLineResponseDTOMapper cartLineMapper) {
        _shoppingCartService = shoppingCartService;
        _userService = userService;
        _shoppingCartLinkProvider = shoppingCartLinkProvider;
        _cartMapper = cartMapper;
        _cartLineMapper = cartLineMapper;
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

    @GetMapping(path = "/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> getUserCart(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId) {

        if (email.isBlank()) {
            throw new SecurityException("Not authorized to view this cart!");
        }

        User user = _userService.getUserByEmail(email);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart!");
        }

        ShoppingCartResponseDTO dto = _cartMapper.toModel(cart);
        _shoppingCartLinkProvider.addLinksForUserCart(dto, email, cartId, cart);

        return ResponseEntity.ok(dto);

    }

    @PatchMapping(path = "/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> clearUserCart(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId) {

        if (email.isBlank()) {
            throw new SecurityException("Not authorized to view this cart!");
        }

        User user = _userService.getUserByEmail(email);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart!");
        }

        ShoppingCart clearedShoppingCart = _shoppingCartService.clearShoppingCartLines(cartId);
        ShoppingCartResponseDTO dto = _cartMapper.toModel(clearedShoppingCart);
        _shoppingCartLinkProvider.addLinksForUserCart(dto, email, cartId, clearedShoppingCart);

        return ResponseEntity.ok(dto);

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

    @PostMapping(path =  "/{cartId}/shopping-cart-lines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> addCartLine(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId,
            @Valid @RequestBody AddCartLineRequestDTO requestDTO) {

        if (email.isBlank()) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        User user = _userService.getUserByEmail(email);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        ShoppingCartLine addedLineShoppingCart = _shoppingCartService.addCartLineToCart(cartId, requestDTO.getDirectSaleId());

        RepresentationModel<?> model = new RepresentationModel<>();
        _shoppingCartLinkProvider.addLinksForCreateUserCartLine(model, email, cartId, addedLineShoppingCart.identity().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(model);

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
            ShoppingCartLine cartLine = _shoppingCartService.findCartLineByLineCartId(cartId, cartLineId);
            allowedMethods = _shoppingCartLinkProvider.getAllowedMethodsForCartLine(user, cart, cartLine);

        }

        return ResponseEntity
                .ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();

    }

    @GetMapping(path = "/{cartId}/shopping-cart-lines/{cartLineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> getUserCartLine(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId,
            @PathVariable String cartLineId) {

        if (email.isBlank()) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        User user = _userService.getUserByEmail(email);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        ShoppingCartLine cartLine = _shoppingCartService.findCartLineByLineCartId(cartId, cartLineId);
        String directSaleId = cartLine.getDirectSaleId().toString();

        ShoppingCartLineResponseDTO dto = _cartLineMapper.toModel(cartLine);
        _shoppingCartLinkProvider.addLinksForUserCartLine(dto, email, cartId, cartLineId, directSaleId);

        return ResponseEntity.ok(dto);

    }

    @DeleteMapping(path = "/{cartId}/shopping-cart-lines/{cartLineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> deleteUserCartLine(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId,
            @PathVariable String cartLineId) {

        if (email.isBlank()) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        User user = _userService.getUserByEmail(email);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(cartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        _shoppingCartService.deleteCartLineByLineCartId(cartId,cartLineId);

        RepresentationModel<?> model = new RepresentationModel<>();
        _shoppingCartLinkProvider.addLinksForDeleteUserCartLine(model, email, cartId);

        return ResponseEntity.ok(model);
    }



}
