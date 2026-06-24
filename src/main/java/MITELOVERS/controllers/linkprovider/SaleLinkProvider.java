package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.SaleRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.sale.SaleLine;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.SaleLineId;
import MITELOVERS.domain.valueobject.UserId;
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

/**
 * HATEOAS link provider for the Sale bounded context.
 * Computes allowed HTTP methods for sales and sale line endpoints based on
 * the current user's role and ownership, and populates representation models
 * with navigational links following the OPTIONS-before-action discipline.
 * Implements {@link RootLinkProvider} to expose the sales discovery link
 * during the root bootstrap call.
 */

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

    public void addLinksForSales(RepresentationModel<?> model, UserId userId, List<Sale> sales) {

        for (Sale sale : sales) {

            String saleId = sale.identity().toString();

            model.add(linkTo(methodOn(SaleRestController.class)
                    .getSaleById(userId.getEmail().toString(), saleId)).withRel("sale"));

        }
    }

    public void addLinksForSale(SaleResponseDTO dto, UserId userId, SaleId saleId, Sale sale) {

        dto.add(linkTo(methodOn(SaleRestController.class)
                .getSaleById(userId.getEmail().toString(), saleId.toString())).withSelfRel());

        for (SaleLine saleLine : sale.get_saleLines()) {

            String saleLineId = saleLine.get_saleLineId().toString();

            dto.add(linkTo(methodOn(SaleRestController.class)
                    .getSaleLineById(userId.getEmail().toString(), saleId.toString(), saleLineId)).withRel("sale-line"));
        }
    }

    public void addLinksForSaleLine(SaleLineResponseDTO dto, UserId userId, SaleId saleId, SaleLineId saleLineId) {

        dto.add(linkTo(methodOn(SaleRestController.class)
                .getSaleLineById(userId.getEmail().toString(), saleId.toString(), saleLineId.toString())).withSelfRel());

        dto.add(linkTo(methodOn(SaleRestController.class).getSaleById(userId.getEmail().toString(), saleId.toString())).withRel("sale"));

    }

    public void addLinksForCreatedSale(RepresentationModel<?> model, UserId userId, SaleId saleId) {

        model.add(linkTo(methodOn(SaleRestController.class)
                .getSaleById(userId.getEmail().toString(), saleId.toString())).withSelfRel());

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
