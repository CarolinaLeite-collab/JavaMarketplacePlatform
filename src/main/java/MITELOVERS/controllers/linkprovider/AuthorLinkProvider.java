package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.AuthorRestController;
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
public class AuthorLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public AuthorLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {
        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canListAuthors(user)) {
            links.add(
                    WebMvcLinkBuilder.linkTo(methodOn(AuthorRestController.class)
                                    .getAllAuthors())
                            .withRel("authors")
            );
        }

        if (_authorizationPolicy.canCreateAuthor(user)) {
            links.add(
                    linkTo(methodOn(AuthorRestController.class)
                            .registerAuthorAndReturnDTO(null))
                            .withRel("create-author")
            );
        }

        return links;
    }
}

