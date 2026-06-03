package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.EditionRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides root-level HATEOAS links related to editions.
 */
@Component
public class EditionLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public EditionLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canListEditions(user)) {
            links.add(linkTo(methodOn(EditionRestController.class)
                    .getAllEditions())
                    .withRel("editions"));
        }

        if (_authorizationPolicy.canCreateEdition(user)) {
            links.add(linkTo(methodOn(EditionRestController.class)
                    .registerEdition(null, null))
                    .withRel("edition-create"));
        }

        return links;
    }
}