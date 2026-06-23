package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.LibraryRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides HATEOAS links for operations related to a user's library.
 *
 * <p>
 * Links are included according to the user's permissions.
 * The sort link exposes the optional {@code sort} URI parameter,
 * while the add link exposes the operation for adding an item.
 * </p>
 */

@Component
public class LibraryLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    LibraryLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canGetLibrary(user)) {
            links.add(
                    linkTo(LibraryRestController.class)
                            .withRel("library")
            );
        }

        if (_authorizationPolicy.canAddToLibrary(user)) {
            links.add(
                    linkTo(methodOn(LibraryRestController.class)
                            .addItemToLibrary(null, null))
                            .withRel("library-add")
            );
        }

        return links;
    }
    public Link getSortLink() {
        return linkTo(methodOn(LibraryRestController.class)
                .getMyLibrary(null, null))
                .withRel("sort");
    }
}
