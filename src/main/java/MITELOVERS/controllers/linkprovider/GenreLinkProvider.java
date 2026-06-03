package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.GenreRestController;
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
public class GenreLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public GenreLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {
        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canListGenres(user)) {
            links.add(
                    WebMvcLinkBuilder.linkTo(methodOn(GenreRestController.class)
                                    .getAllGenres())
                            .withRel("genres")
            );
        }

        if (_authorizationPolicy.canAddGenre(user)) {
            links.add(
                    linkTo(methodOn(GenreRestController.class)
                            .registerGenreAndReturnDTO(null))
                            .withRel("create-genre")
            );
        }

        return links;
    }
}

