package MITELOVERS.controllers.rest;

import MITELOVERS.applicationservices.SaleService;
import MITELOVERS.applicationservices.UserService;
import MITELOVERS.controllers.linkprovider.SaleLinkProvider;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import MITELOVERS.dto.response.SaleResponseDTO;
import MITELOVERS.mapper.SaleLineResponseDTOMapper;
import MITELOVERS.mapper.SaleResponseDTOMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
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
    private final UserService _userService;
    private final SaleLinkProvider _saleLinkProvider;
    private SaleResponseDTOMapper _saleMapper;
    private SaleLineResponseDTOMapper _saleLineMapper;

    public SaleRestController(SaleService saleService,
                              UserService userService,
                              SaleLinkProvider saleLinkProvider,
                              SaleResponseDTOMapper saleMapper,
                              SaleLineResponseDTOMapper saleLineMapper) {
        this._saleService = saleService;
        this._userService = userService;
        this._saleLinkProvider = saleLinkProvider;
        this._saleMapper = saleMapper;
        this._saleLineMapper = saleLineMapper;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForSales(
            @RequestHeader("X-User-Id") String email) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(email);
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

        if (email.isBlank()) {
            throw new SecurityException("Not authorized!");
        }

        User user = _userService.getUserByEmail(email);
        List<Sale> userSales = _saleService.findUserSales(user);

        RepresentationModel<?> model = new RepresentationModel<>();
        _saleLinkProvider.addLinksForSales(model, email, userSales);

        return ResponseEntity.ok(model);
    }

    @RequestMapping(path = "/{saleId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForSale(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String saleId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(email);
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

        if (email.isBlank()) {
            throw new SecurityException("Not authorized!");
        }

        User user = _userService.getUserByEmail(email);
        SaleId reconstructedSaleId = new SaleId(saleId);
        Sale sale = _saleService.findSaleById(reconstructedSaleId);

        if (!user.identity().equals(sale.get_buyerId())) {
            throw new SecurityException("Not authorized to view this sale!");
        }

        SaleResponseDTO dto = _saleMapper.toModel(sale);
        _saleLinkProvider.addLinksForSale(dto, email, saleId, sale);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(path = "/{saleId}/sale-lines/{saleLineId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsForSaleLine(
            @RequestHeader("X-User-Id") String email,
            @PathVariable String saleId) {

        List<HttpMethod> allowedMethods = List.of(HttpMethod.OPTIONS);

        if (!email.isBlank()) {
            User user = _userService.getUserByEmail(email);
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

        if (email.isBlank()) {
            throw new SecurityException("Not authorized!");
        }

        User user = _userService.getUserByEmail(email);
        SaleId reconstructedSaleId = new SaleId(saleId);
        Sale sale = _saleService.findSaleById(reconstructedSaleId);

        if (!user.identity().equals(sale.get_buyerId())) {
            throw new SecurityException("Not authorized to view this sale line!");
        }

        SaleLineId reconstructedSaleLineId = new SaleLineId(saleLineId);
        SaleLine saleline = _saleService.getSaleLineById(reconstructedSaleId, reconstructedSaleLineId);

        SaleLineResponseDTO dto = _saleLineMapper.toModel(saleline);
        _saleLinkProvider.addLinksForSaleLine(dto, email, saleId, saleLineId);

        return ResponseEntity.ok(dto);
    }
}
