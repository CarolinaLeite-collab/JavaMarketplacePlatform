package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.PublicationTypeRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides root-level HATEOAS links related to publication types.
 */
@Component
public class PublicationTypeLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public PublicationTypeLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canListPublicationTypes(user)) {
            links.add(linkTo(methodOn(PublicationTypeRestController.class)
                    .getAllPublicationTypes())
                    .withRel("publication-types"));
        }

        return links;
    }
}