package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.CountryRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CountryLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public CountryLinkProvider(AuthorizationPolicy authorizationPolicy) {

        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canListCountries(user)) {
            links.add(
                    linkTo(methodOn(CountryRestController.class)
                            .listAll())
                            .withRel("countries")
            );
        }

        if (_authorizationPolicy.canGetCountry(user)) {
            links.add(
                    linkTo(methodOn(CountryRestController.class)
                            .findById(null))
                            .withRel("country")
            );
        }

        if (_authorizationPolicy.canCreateCountry(user)) {
            links.add(
                    linkTo(methodOn(CountryRestController.class)
                            .create(null))
                            .withRel("create-country")
            );
        }
        return links;
    }
}
