package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.ShoppingCartService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.ShoppingCartLinkProvider;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.shoppingcart.ShoppingCartLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
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
 * REST controller exposing ShoppingCart-related endpoints under {@code /shopping-carts}.
 * Supports cart discovery, viewing and clearing a cart, managing cart lines
 * (OPTIONS/POST/GET/DELETE), and enforces ownership checks before any cart or
 * cart line operation. Delegates link construction to {@link ShoppingCartLinkProvider}
 * following the OPTIONS-before-action HATEOAS discipline.
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

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForCarts(
            @RequestHeader("X-User-Id") String email) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            allowedMethods = _shoppingCartLinkProvider.getAllowedMethodsForCarts(user);
        }

        return ResponseEntity
                .ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> getUserCartLink(
            @RequestHeader("X-User-Id") String email) {

        if (email.isBlank()) {
            throw new SecurityException("Not authorized!");
        }

        UserId reconstructedUserId = new UserId(new Email(email));
        ShoppingCart cart = _shoppingCartService.findCartByUserId(reconstructedUserId);

        RepresentationModel<?> model = new RepresentationModel<>();
        _shoppingCartLinkProvider.addLinksForUserCartDiscovery(model, reconstructedUserId, cart.identity());

        return ResponseEntity.ok(model);
    }

    @RequestMapping(path= "/{cartId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForCart(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if(!email.isBlank()) {

            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            ShoppingCartId reconstructedCartId = new ShoppingCartId(cartId);
            ShoppingCart cart = _shoppingCartService.findCartByCartId(reconstructedCartId);
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

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        ShoppingCartId reconstructedCartId = new ShoppingCartId(cartId);

        ShoppingCart cart = _shoppingCartService.findCartByCartId(reconstructedCartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart!");
        }

        ShoppingCartResponseDTO dto = _cartMapper.toModel(cart);
        _shoppingCartLinkProvider.addLinksForUserCart(dto, user.identity(), cart.identity(), cart);

        return ResponseEntity.ok(dto);

    }

    @PatchMapping(path = "/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> clearUserCart(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId) {

        if (email.isBlank()) {
            throw new SecurityException("Not authorized to view this cart!");
        }

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        ShoppingCartId reconstructedCartId = new ShoppingCartId(cartId);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(reconstructedCartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart!");
        }

        ShoppingCart clearedShoppingCart = _shoppingCartService.clearShoppingCartLines(reconstructedCartId);
        ShoppingCartResponseDTO dto = _cartMapper.toModel(clearedShoppingCart);
        _shoppingCartLinkProvider.addLinksForUserCart(dto, user.identity(), cart.identity(), clearedShoppingCart);

        return ResponseEntity.ok(dto);

    }


    @RequestMapping(path= "/{cartId}/shopping-cart-lines", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForCartLines(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if(!email.isBlank()) {

            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            ShoppingCartId reconstructedCartId = new ShoppingCartId(cartId);
            ShoppingCart cart = _shoppingCartService.findCartByCartId(reconstructedCartId);
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

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        ShoppingCartId reconstructedCartId = new ShoppingCartId(cartId);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(reconstructedCartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        DirectSaleId reconstructedDirectSaleId = new DirectSaleId(requestDTO.getDirectSaleId());
        ShoppingCartLine addedLineShoppingCart = _shoppingCartService.addCartLineToCart(reconstructedCartId, reconstructedDirectSaleId);

        RepresentationModel<?> model = new RepresentationModel<>();
        _shoppingCartLinkProvider.addLinksForCreateUserCartLine(model, user.identity(), cart.identity(), addedLineShoppingCart.identity());

        return ResponseEntity.status(HttpStatus.CREATED).body(model);

    }

    @RequestMapping(path= "/{cartId}/shopping-cart-lines/{cartLineId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForCartLine(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String cartId,
            @PathVariable String cartLineId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if(!email.isBlank()) {

            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            ShoppingCartId reconstructedCartId = new ShoppingCartId(cartId);
            ShoppingCart cart = _shoppingCartService.findCartByCartId(reconstructedCartId);
            ShoppingCartLineId reconstructedCartLineId = new ShoppingCartLineId(cartLineId);
            ShoppingCartLine cartLine = _shoppingCartService.findCartLineByLineCartId(reconstructedCartId, reconstructedCartLineId);
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

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        ShoppingCartId reconstructedCartId = new ShoppingCartId(cartId);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(reconstructedCartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        ShoppingCartLineId reconstructedCartLineId = new ShoppingCartLineId(cartLineId);
        ShoppingCartLine cartLine = _shoppingCartService.findCartLineByLineCartId(reconstructedCartId, reconstructedCartLineId);

        DirectSaleId reconstructedDirectSaleId = cartLine.getDirectSaleId();
        ShoppingCartLineResponseDTO dto = _cartLineMapper.toModel(cartLine);

        _shoppingCartLinkProvider.addLinksForUserCartLine(dto, user.identity(), reconstructedCartId, reconstructedCartLineId, reconstructedDirectSaleId);

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

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        ShoppingCartId reconstructedCartId = new ShoppingCartId(cartId);
        ShoppingCart cart = _shoppingCartService.findCartByCartId(reconstructedCartId);

        if (!user.identity().equals(cart.getBuyerId())) {
            throw new SecurityException("Not authorized to view this cart line!");
        }

        ShoppingCartLineId reconstructedCartLineId = new ShoppingCartLineId(cartLineId);
        _shoppingCartService.deleteCartLineByLineCartId(reconstructedCartId,reconstructedCartLineId);

        RepresentationModel<?> model = new RepresentationModel<>();
        _shoppingCartLinkProvider.addLinksForDeleteUserCartLine(model, user.identity(), reconstructedCartId);

        return ResponseEntity.ok(model);
    }



}
