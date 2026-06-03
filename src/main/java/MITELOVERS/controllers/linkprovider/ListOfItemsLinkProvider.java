package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.ListOfItemsRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    public List<Link> getLinks (User user) {

        List<Link> links = new ArrayList<>();

        if(_authorizationPolicy.canSeeList(user)) {
            links.add(WebMvcLinkBuilder.linkTo(methodOn(ListOfItemsRestController.class).getListById(null)).withSelfRel());
            links.add(linkTo(methodOn(ListOfItemsRestController.class).getLists(user.identity().toString())).withRel("collection"));
        }

        if(_authorizationPolicy.canDeleteList(user)) {
            links.add(linkTo(methodOn(ListOfItemsRestController.class).deleteList(null)).withRel("delete"));
        }

        if(_authorizationPolicy.canAddItemTo(user)) {
            links.add(linkTo(methodOn(ListOfItemsRestController.class).addItemToList(null, null)).withRel("add-item"));
        }

        if(_authorizationPolicy.canCreateList(user)) {
            links.add(linkTo(methodOn(ListOfItemsRestController.class).createAndSaveList(user.identity().toString(), null)).withRel("create-list"));
        }

        return links;
    }



}