package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.controllers.rest.PublishingCompanyRestController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides root-level HATEOAS links related to publishing companies.
 *
 * <p>
 * This component is responsible for adding the {@link PublishingCompany}
 * link to the root response when the authenticated user has at least one
 * permission related to publishing company operations.
 * </p>
 *
 * <p>
 * The link is only included if the user is authorized to perform actions.
 * </p>
 */

@Component
public class PublishingCompanyLinkProvider implements RootLinkProvider {

    private final AuthorizationPolicy _authorizationPolicy;

    public PublishingCompanyLinkProvider(AuthorizationPolicy authorizationPolicy) {
        _authorizationPolicy = authorizationPolicy;
    }

    @Override
    public List<Link> getLinks(User user) {

        List<Link> links = new ArrayList<>();

        if (_authorizationPolicy.canCreatePublishingCompany(user)) {

            links.add(linkTo(methodOn(PublishingCompanyRestController.class)
                    .registerPublishingCompany(null))
                    .withRel("createPublishingCompany"));
        }

        if (_authorizationPolicy.canGetAllPublishingCompanies(user)
                || _authorizationPolicy.canGetPublishingCompany(user)) {

            links.add(linkTo(methodOn(PublishingCompanyRestController.class)
                    .getAllPublishingCompanies())
                    .withRel("publishingCompanies"));
        }

        if (_authorizationPolicy.canGetPublishingCompany(user)) {
            links.add(linkTo(methodOn(PublishingCompanyRestController.class)
                    .getPublishingCompanyById(null))
                    .withRel("publishingCompany"));
        }

        return links;
    }

}
