package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ListOfItemsLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public ListOfItemsLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        String userId = user.identity().toString();

        // GET /my-lists
        links.add(
                linkTo(methodOn(ListOfItemsRestController.class)
                        .getLists(userId))
                        .withRel("collection")
        );

        // POST /my-lists
        if (_authorizationPolicy.canCreateList(user)) {
            links.add(
                    linkTo(methodOn(ListOfItemsRestController.class)
                            .createAndSaveList(userId, null))
                            .withRel("create-list")
            );
        }

        // GET /my-lists/public
        links.add(
                linkTo(methodOn(ListOfItemsRestController.class)
                        .getPublicLists())
                        .withRel("public-lists")
        );

        return links;
    }

}