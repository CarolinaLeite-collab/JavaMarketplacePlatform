package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.ItemRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides root-level HATEOAS links related to items.
 *
 * <p>
 * Links are conditionally included based on the authenticated user's
 * permissions, following the HATEOAS level 3 contract (US047).
 * </p>
 */

@Component
public class ItemLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public ItemLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canCreateItem(user)) {
            links.add(linkTo(methodOn(ItemRestController.class)
                    .registerItem(null))
                    .withRel("createItem"));
        }

        if (_authorizationPolicy.canListItems(user)) {
            links.add(linkTo(methodOn(ItemRestController.class)
                    .getAllItems())
                    .withRel("items"));
        }

        if (_authorizationPolicy.canGetLibrary(user)) {
            links.add(linkTo(methodOn(ItemRestController.class)
                    .getItemsIdsInLibrary(null))
                    .withRel("myLibraryItems"));
        }

        return links;
    }
}