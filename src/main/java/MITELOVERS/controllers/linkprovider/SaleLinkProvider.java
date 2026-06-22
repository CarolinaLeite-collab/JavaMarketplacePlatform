package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.SaleRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.dto.response.SaleLineResponseDTO;
import MITELOVERS.dto.response.SaleResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SaleLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public SaleLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    public List<HttpMethod> getAllowedMethodsForSales(User user) {
        List<HttpMethod> methods = new ArrayList<>();

        if (_authorizationPolicy.canGetSales(user)) {
            methods.add(HttpMethod.GET);
        }

        if (_authorizationPolicy.canPostSales(user)) {
            methods.add(HttpMethod.POST);
        }

        methods.add(HttpMethod.OPTIONS);
        return methods;
    }

    public List<HttpMethod> getAllowedMethodsForSale(User user, Sale sale) {

        List<HttpMethod> methods = new ArrayList<>();

        if (user.identity().equals(sale.get_buyerId())) {

            if (_authorizationPolicy.canGetSale(user)) {
                methods.add(HttpMethod.GET);
            }
        }

        methods.add(HttpMethod.OPTIONS);
        return methods;
    }

    public List<HttpMethod> getAllowedMethodsForSaleLine(User user, Sale sale) {

        List<HttpMethod> methods = new ArrayList<>();

        if (user.identity().equals(sale.get_buyerId())) {

            if (_authorizationPolicy.canGetSale(user)) {
                methods.add(HttpMethod.GET);
            }
        }

        methods.add(HttpMethod.OPTIONS);
        return methods;
    }

    public void addLinksForSales(RepresentationModel<?> model, String email, List<Sale> sales) {

        for (Sale sale : sales) {

            String saleId = sale.identity().toString();

            model.add(linkTo(methodOn(SaleRestController.class)
                    .getSaleById(email, saleId)).withRel("sale"));

        }
    }

    public void addLinksForSale(SaleResponseDTO dto, String email, String saleId, Sale sale) {

        dto.add(linkTo(methodOn(SaleRestController.class)
                .getSaleById(email, saleId)).withSelfRel());

        for (SaleLine saleLine : sale.get_saleLines()) {

            String saleLineId = saleLine.get_saleLineId().toString();

            dto.add(linkTo(methodOn(SaleRestController.class)
                    .getSaleLineById(email, saleId, saleLineId)).withRel("sale-line"));
        }
    }

    public void addLinksForSaleLine(SaleLineResponseDTO dto, String email, String saleId, String saleLineId) {

        dto.add(linkTo(methodOn(SaleRestController.class)
                .getSaleLineById(email, saleId, saleLineId)).withSelfRel());

        dto.add(linkTo(methodOn(SaleRestController.class).getSaleById(email, saleId)).withRel("sale"));

    }

    public void addLinksForCreatedSale(RepresentationModel<?> model, String email, String saleId) {

        model.add(linkTo(methodOn(SaleRestController.class)
                .getSaleById(null, saleId)).withSelfRel());

    }

    @Override
    public List<Link> getLinks(User user) {

        if (_authorizationPolicy.canGetSales(user)) {
            return List.of(
                    linkTo(methodOn(SaleRestController.class)
                            .getUserSales(null)).withRel("sales")
            );
        }

        return List.of();
    }
}
