package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublishingCompanyLinkProviderTest {

    @Mock
    private AuthorizationPolicy authorizationPolicy;

    @Mock
    private User user;

    @InjectMocks
    private PublishingCompanyLinkProvider provider;

    @Test
    void shouldReturnAllLinksWhenUserHasAllPermissions() {

        when(authorizationPolicy.canGetAllPublishingCompanies(user))
                .thenReturn(true);

        when(authorizationPolicy.canCreatePublishingCompany(user))
                .thenReturn(true);

        List<Link> links = provider.getLinks(user);

        assertEquals(2, links.size());

        assertTrue(
                links.stream()
                        .anyMatch(link ->
                                link.getRel().value().equals("publishingCompanies"))
        );

        assertTrue(
                links.stream()
                        .anyMatch(link ->
                                link.getRel().value().equals("createPublishingCompany"))
        );
    }

    @Test
    void shouldReturnOnlyGetLink() {

        when(authorizationPolicy.canGetAllPublishingCompanies(user))
                .thenReturn(true);

        when(authorizationPolicy.canCreatePublishingCompany(user))
                .thenReturn(false);

        List<Link> links = provider.getLinks(user);

        assertEquals(1, links.size());

        assertEquals(
                "publishingCompanies",
                links.get(0).getRel().value()
        );
    }

    @Test
    void shouldReturnNoLinksWhenUserHasNoPermissions() {

        when(authorizationPolicy.canGetAllPublishingCompanies(user))
                .thenReturn(false);

        when(authorizationPolicy.canCreatePublishingCompany(user))
                .thenReturn(false);

        List<Link> links = provider.getLinks(user);

        assertTrue(links.isEmpty());
    }
}