package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.PublicationRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides root-level HATEOAS links related to publications.
 *
 * <p>
 * This component is responsible for adding {@link MITELOVERS.domain.publication.Publication}
 * links to the root response when the authenticated user has at least one
 * permission related to publication operations.
 * </p>
 *
 * <p>
 * The links are only included if the user is authorized to perform actions.
 * </p>
 */

@Component
public class PublicationLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public PublicationLinkProvider(
            AuthorizationPolicy authorizationPolicy) {

        _authorizationPolicy = authorizationPolicy;
    }
    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canCreatePublication(user)) {

            links.add(linkTo(methodOn(PublicationRestController.class)
                    .registerPublicationAndReturnDTO(null))
                    .withRel("createPublication"));
        }

        if (_authorizationPolicy.canListPublications(user)) {

            links.add(linkTo(methodOn(PublicationRestController.class)
                    .getAllPublications())
                    .withRel("publications"));
        }

        return links;
    }

}
