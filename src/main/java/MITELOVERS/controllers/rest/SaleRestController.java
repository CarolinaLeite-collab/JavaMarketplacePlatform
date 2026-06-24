package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.SaleService;
import MITELOVERS.applicationservices.ShoppingCartService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.SaleLinkProvider;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.shoppingcart.ShoppingCart;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.dto.request.SaleRequestDTO;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import MITELOVERS.dto.response.SaleResponseDTO;
import MITELOVERS.mapper.SaleLineResponseDTOMapper;
import MITELOVERS.mapper.SaleResponseDTOMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("sales")
public class SaleRestController {

    private final SaleService _saleService;
    private final ShoppingCartService _shoppingCartService;
    private final UserService _userService;
    private final SaleLinkProvider _saleLinkProvider;
    private final SaleResponseDTOMapper _saleMapper;
    private final SaleLineResponseDTOMapper _saleLineMapper;

    public SaleRestController(SaleService saleService,
                              ShoppingCartService shoppingCartService,
                              UserService userService,
                              SaleLinkProvider saleLinkProvider,
                              SaleResponseDTOMapper saleMapper,
                              SaleLineResponseDTOMapper saleLineMapper) {
        _saleService = saleService;
        _shoppingCartService = shoppingCartService;
        _userService = userService;
        _saleLinkProvider = saleLinkProvider;
        _saleMapper = saleMapper;
        _saleLineMapper = saleLineMapper;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForSales(
            @RequestHeader("X-User-Id") String email) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            allowedMethods = _saleLinkProvider.getAllowedMethodsForSales(user);
        }

        return ResponseEntity
                .ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> getUserSales(
            @RequestHeader("X-User-Id") String email) {

        checkEmailNotBlank(email);

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        List<Sale> userSales = _saleService.findUserSales(user.identity());

        RepresentationModel<?> model = new RepresentationModel<>();
        _saleLinkProvider.addLinksForSales(model, user.identity(), userSales);

        return ResponseEntity.ok(model);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> createSaleFromCart(
            @RequestHeader("X-User-Id") String email,
            @RequestBody SaleRequestDTO dto) {

        checkEmailNotBlank(email);

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        ShoppingCartId shoppingCartId = new ShoppingCartId(dto.getShoppingCartId());

        ShoppingCart shoppingCart = _shoppingCartService.findCartByCartId(shoppingCartId);

        if(!shoppingCart.getBuyerId().equals(user.identity())) {
            throw new SecurityException("ShoppingCart does not match the user!");
        }

        Sale newSale = _saleService.createSaleFromCart(shoppingCartId);

        RepresentationModel<?> model = new RepresentationModel<>();

        _saleLinkProvider.addLinksForCreatedSale(model, user.identity(), newSale.get_saleId());

        return ResponseEntity.status(HttpStatus.CREATED).body(model);

    }

    @RequestMapping(path = "/{saleId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForSale(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String saleId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            SaleId reconstructedSaleId = new SaleId(saleId);
            Sale sale = _saleService.findSaleById(reconstructedSaleId);
            allowedMethods = _saleLinkProvider.getAllowedMethodsForSale(user, sale);
        }

        return ResponseEntity
                .ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();
    }

    @GetMapping(path = "/{saleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> getSaleById(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String saleId) {

        checkEmailNotBlank(email);

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        SaleId reconstructedSaleId = new SaleId(saleId);
        Sale sale = _saleService.findSaleById(reconstructedSaleId);

        if (!user.identity().equals(sale.get_buyerId())) {
            throw new SecurityException("Not authorized to view this sale!");
        }

        SaleResponseDTO dto = _saleMapper.toModel(sale);
        _saleLinkProvider.addLinksForSale(dto, user.identity(), reconstructedSaleId, sale);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(path = "/{saleId}/sale-lines/{saleLineId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForSaleLine(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String saleId,
            @PathVariable String saleLineId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(new UserId(new Email(email)));
            SaleId reconstructedSaleId = new SaleId(saleId);
            Sale sale = _saleService.findSaleById(reconstructedSaleId);
            allowedMethods = _saleLinkProvider.getAllowedMethodsForSaleLine(user, sale);
        }

        return ResponseEntity
                .ok()
                .allow(allowedMethods.toArray(new HttpMethod[0]))
                .build();
    }

    @GetMapping(path = "/{saleId}/sale-lines/{saleLineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RepresentationModel<?>> getSaleLineById(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String saleId,
            @PathVariable String saleLineId) {

        checkEmailNotBlank(email);

        User user = _userService.getUserByEmail(new UserId(new Email(email)));
        SaleId reconstructedSaleId = new SaleId(saleId);
        Sale sale = _saleService.findSaleById(reconstructedSaleId);

        if (!user.identity().equals(sale.get_buyerId())) {
            throw new SecurityException("Not authorized to view this sale line!");
        }

        SaleLineId reconstructedSaleLineId = new SaleLineId(saleLineId);
        SaleLine saleline = _saleService.getSaleLineById(reconstructedSaleId, reconstructedSaleLineId);

        SaleLineResponseDTO dto = _saleLineMapper.toModel(saleline);
        _saleLinkProvider.addLinksForSaleLine(dto, user.identity(), reconstructedSaleId, reconstructedSaleLineId);

        return ResponseEntity.ok(dto);
    }

    private void checkEmailNotBlank(String email) {

        if (email.isBlank()) {
            throw new SecurityException("Not authorized!");
        }

    }

}
