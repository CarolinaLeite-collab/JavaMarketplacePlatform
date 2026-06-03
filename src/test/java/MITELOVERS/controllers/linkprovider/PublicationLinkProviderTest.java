package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class PublicationLinkProviderTest {

    @Test
    void shouldReturnListPublicationsLinkWhenUserCanListPublications() {
        // Arrange
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        User user = mock(User.class);
        when(authorizationPolicy.canListPublications(user)).thenReturn(true);
        when(authorizationPolicy.canCreatePublication(user)).thenReturn(false);

        PublicationLinkProvider sut = new PublicationLinkProvider(authorizationPolicy);

        // Act
        List<Link> result = sut.getLinks(user);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRel().value()).isEqualTo("publications");
    }

    @Test
    void shouldReturnCreatePublicationLinkWhenUserCanCreatePublication() {
        // Arrange
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        User user = mock(User.class);
        when(authorizationPolicy.canListPublications(user)).thenReturn(false);
        when(authorizationPolicy.canCreatePublication(user)).thenReturn(true);

        PublicationLinkProvider sut = new PublicationLinkProvider(authorizationPolicy);

        // Act
        List<Link> result = sut.getLinks(user);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRel().value()).isEqualTo("create-publication");
    }

    @Test
    void shouldReturnBothLinksWhenUserHasAllPermissions() {
        // Arrange
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        User user = mock(User.class);
        when(authorizationPolicy.canListPublications(user)).thenReturn(true);
        when(authorizationPolicy.canCreatePublication(user)).thenReturn(true);

        PublicationLinkProvider sut = new PublicationLinkProvider(authorizationPolicy);

        // Act
        List<Link> result = sut.getLinks(user);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(link -> link.getRel().value())
                .containsExactly("publications", "create-publication");
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoPermissions() {
        // Arrange
        AuthorizationPolicy authorizationPolicy = mock(AuthorizationPolicy.class);
        User user = mock(User.class);
        when(authorizationPolicy.canListPublications(user)).thenReturn(false);
        when(authorizationPolicy.canCreatePublication(user)).thenReturn(false);

        PublicationLinkProvider sut = new PublicationLinkProvider(authorizationPolicy);

        // Act
        List<Link> result = sut.getLinks(user);

        // Assert
        assertThat(result).isEmpty();
    }
}