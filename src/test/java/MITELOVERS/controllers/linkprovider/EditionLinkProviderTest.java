package MITELOVERS.controllers.linkprovider;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.dto.response.EditionResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditionLinkProviderTest {

    @Mock
    private AuthorizationPolicy _authorizationPolicy;

    @InjectMocks
    private EditionLinkProvider _linkProvider;

    // --- getAllowedMethods ---

    @Test
    void getAllowedMethodsReturnsGetPostAndOptionsWhenAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canCreateEdition(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethods(userDouble);

        // Assert
        assertTrue(result.contains(HttpMethod.OPTIONS));
        assertTrue(result.contains(HttpMethod.POST));
        assertTrue(result.contains(HttpMethod.GET));
    }

    @Test
    void getAllowedMethodsReturnsOnlyOptionsWhenNotAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canCreateEdition(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethods(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    @Test
    void getAllowedMethodsReturnsGetAndOptionsWhenCanListOnly() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canCreateEdition(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethods(userDouble);

        // Assert
        assertTrue(result.contains(HttpMethod.OPTIONS));
        assertTrue(result.contains(HttpMethod.GET));
        assertFalse(result.contains(HttpMethod.POST));
    }

    // --- getAllowedMethodsForPublication ---

    @Test
    void getAllowedMethodsForPublicationReturnsGetAndOptionsWhenAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForPublication(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS, HttpMethod.GET), result);
    }

    @Test
    void getAllowedMethodsForPublicationReturnsOnlyOptionsWhenNotAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForPublication(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    // --- getAllowedMethodsForEditionId ---

    @Test
    void getAllowedMethodsForEditionIdReturnsGetAndOptionsWhenAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(true);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForEditionId(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS, HttpMethod.GET), result);
    }

    @Test
    void getAllowedMethodsForEditionIdReturnsOnlyOptionsWhenNotAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(false);

        // Act
        List<HttpMethod> result = _linkProvider.getAllowedMethodsForEditionId(userDouble);

        // Assert
        assertEquals(List.of(HttpMethod.OPTIONS), result);
    }

    // --- getLinks ---

    @Test
    void getLinksReturnsEditionsLinkWhenCanList() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canCreateEdition(userDouble)).thenReturn(false);

        // Act
        List<Link> result = _linkProvider.getLinks(userDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("editions", result.get(0).getRel().value());
    }

    @Test
    void getLinksReturnsCreateLinkWhenCanCreate() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canCreateEdition(userDouble)).thenReturn(true);

        // Act
        List<Link> result = _linkProvider.getLinks(userDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("edition-create", result.get(0).getRel().value());
    }

    @Test
    void getLinksReturnsBothLinksWhenFullyAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(true);
        when(_authorizationPolicy.canCreateEdition(userDouble)).thenReturn(true);

        // Act
        List<Link> result = _linkProvider.getLinks(userDouble);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getLinksReturnsEmptyWhenNotAuthorized() {
        // Arrange
        User userDouble = mock(User.class);
        when(_authorizationPolicy.canListEditions(userDouble)).thenReturn(false);
        when(_authorizationPolicy.canCreateEdition(userDouble)).thenReturn(false);

        // Act
        List<Link> result = _linkProvider.getLinks(userDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    // --- addLinkForEdition ---

    @Test
    void addLinkForEditionAddsSelfLink() {
        // Arrange
        RepresentationModel<?> model = new RepresentationModel<>();
        EditionId editionIdDouble = mock(EditionId.class);
        when(editionIdDouble.toString()).thenReturn("ED-001");

        // Act
        _linkProvider.addLinkForEdition(model, editionIdDouble);

        // Assert
        assertTrue(model.hasLink("self"));
        assertTrue(model.getRequiredLink("self").getHref().endsWith("/editions/ED-001"));
    }

    // --- addLinksForAllEditions ---

    @Test
    void addLinksForAllEditionsAddsSelfLinkToEachEditionAndCollection() {
        // Arrange
        EditionResponseDTO dto1 = mock(EditionResponseDTO.class);
        EditionResponseDTO dto2 = mock(EditionResponseDTO.class);
        when(dto1.getEditionId()).thenReturn("ED-001");
        when(dto2.getEditionId()).thenReturn("ED-002");

        // Act
        CollectionModel<EditionResponseDTO> result = _linkProvider.addLinksForAllEditions(List.of(dto1, dto2));

        // Assert
        assertTrue(result.hasLink("self"));
        assertTrue(result.getRequiredLink("self").getHref().endsWith("/editions"));
        verify(dto1).add(any(Link.class));
        verify(dto2).add(any(Link.class));
    }

    @Test
    void addLinksForAllEditionsReturnsEmptyCollectionWhenNoEditions() {
        // Act
        CollectionModel<EditionResponseDTO> result = _linkProvider.addLinksForAllEditions(List.of());

        // Assert
        assertTrue(result.hasLink("self"));
        assertFalse(result.getContent().iterator().hasNext());
    }

    // --- addLinksForEditionsByPublication ---

    @Test
    void addLinksForEditionsByPublicationAddsSelfLinkToEachAndCollection() {
        // Arrange
        EditionResponseDTO dto1 = mock(EditionResponseDTO.class);
        when(dto1.getEditionId()).thenReturn("ED-001");

        // Act
        CollectionModel<EditionResponseDTO> result = _linkProvider.addLinksForEditionsByPublication(List.of(dto1), "PUB-001");

        // Assert
        assertTrue(result.hasLink("self"));
        assertTrue(result.getRequiredLink("self").getHref().contains("by-publication"));
        verify(dto1).add(any(Link.class));
    }

    @Test
    void addLinksForEditionsByPublicationReturnsEmptyCollectionWhenNoEditions() {
        // Act
        CollectionModel<EditionResponseDTO> result = _linkProvider.addLinksForEditionsByPublication(List.of(), "PUB-001");

        // Assert
        assertTrue(result.hasLink("self"));
        assertFalse(result.getContent().iterator().hasNext());
    }
}