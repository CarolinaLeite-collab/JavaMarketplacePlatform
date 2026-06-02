package MITELOVERS.controllers.rest;

import MITELOVERS.authorization.AuthorizationPolicy;
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
public class PublicationLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;
    PublicationLinkProvider(
            AuthorizationPolicy authorizationPolicy) {

        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canListPublications(user)) {
            links.add(
                    WebMvcLinkBuilder.linkTo(methodOn(PublicationRestController.class)
                                    .getAllPublications())
                            .withRel("publications")
            );
        }

        if (_authorizationPolicy.canCreatePublication(user)) {
            links.add(
                    linkTo(methodOn(PublicationRestController.class)
                            .registerPublicationAndReturnDTO(null))
                            .withRel("create-publication")
            );

        }

        return links;
    }
}
