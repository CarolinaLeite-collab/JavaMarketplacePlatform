package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.EditionRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.dto.response.EditionResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * HATEOAS link provider for the Edition bounded context.
 * Computes allowed HTTP methods for edition collection and item endpoints
 * based on the current user's permissions, and populates representation
 * models with navigational links for individual editions and edition
 * collections, including publication-filtered listings. Implements
 * {@link RootLinkProvider} to expose edition-related discovery links
 * during the root bootstrap call.
 */

@Component
public class EditionLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public EditionLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    // Links for the /editions root resource (OPTIONS /editions)
    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canListEditions(user)) {
            links.add(linkTo(EditionRestController.class)
                    .withRel("editions"));
        }

        if (_authorizationPolicy.canCreateEdition(user)) {
            links.add(linkTo(methodOn(EditionRestController.class)
                    .registerEdition(null, null))
                    .withRel("edition-create"));
        }
        return links;
    }

    // Allowed HTTP methods for /editions (OPTIONS /editions)
    public List<HttpMethod> getAllowedMethods(User user) {

        List<HttpMethod> methods = new ArrayList<>();

        methods.add(HttpMethod.OPTIONS);

        if(_authorizationPolicy.canCreateEdition(user)) {
            methods.add(HttpMethod.POST);
        }

        if(_authorizationPolicy.canListEditions(user)) {
            methods.add(HttpMethod.GET);
        }

        return methods;
    }

    public List<HttpMethod> getAllowedMethodsForPublication (User user) {

        List<HttpMethod> methods = new ArrayList<>();

        methods.add(HttpMethod.OPTIONS);

        if (_authorizationPolicy.canListEditions(user)) {
            methods.add(HttpMethod.GET);
        }

        return methods;
    }

    public List<HttpMethod> getAllowedMethodsForEditionId (User user) {

        List<HttpMethod> methods = new ArrayList<>();

        methods.add(HttpMethod.OPTIONS);

        if (_authorizationPolicy.canListEditions(user)) {
            methods.add(HttpMethod.GET);
        }

        return methods;
    }

    public void addLinkForEdition(RepresentationModel<?> model, EditionId editionId) {

        model.add(linkTo(methodOn(EditionRestController.class)
                .getEditionById(editionId.toString())).withSelfRel());

    }

    public CollectionModel<EditionResponseDTO> addLinksForAllEditions(
            List<EditionResponseDTO> editions) {

        for (EditionResponseDTO dto : editions) {
            dto.add(linkTo(methodOn(EditionRestController.class)
                    .getEditionById(dto.getEditionId()))
                    .withSelfRel());
        }

        return CollectionModel.of(
                editions,
                linkTo(methodOn(EditionRestController.class)
                        .getAllEditions())
                        .withSelfRel()
        );
    }

    public CollectionModel<EditionResponseDTO> addLinksForEditionsByPublication(
            List<EditionResponseDTO> editions, String publicationId) {

        for (EditionResponseDTO dto : editions) {
            dto.add(linkTo(methodOn(EditionRestController.class)
                    .getEditionById(dto.getEditionId()))
                    .withSelfRel());
        }

        return CollectionModel.of(
                editions,
                linkTo(methodOn(EditionRestController.class)
                        .getAllEditionsByPublication(publicationId))
                        .withSelfRel()
        );
    }

}