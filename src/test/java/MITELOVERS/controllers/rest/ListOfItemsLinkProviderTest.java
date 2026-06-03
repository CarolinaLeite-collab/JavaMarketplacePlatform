package MITELOVERS.controllers.rest;

import MITELOVERS.authorization.AuthorizationPolicy;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListOfItemsLinkProviderTest {

    @Mock
    AuthorizationPolicy authorizationPolicy;

    @Mock
    User user;

    @InjectMocks
    ListOfItemsLinkProvider linkProvider;

    @Test
    void getLinks_returnsAllLinks_whenUserHasAllPermissions() {
        // Arrange
        when(authorizationPolicy.canSeeList(user)).thenReturn(true);
        when(authorizationPolicy.canDeleteList(user)).thenReturn(true);
        when(authorizationPolicy.canAddItemTo(user)).thenReturn(true);
        when(authorizationPolicy.canCreateList(user)).thenReturn(true);
        UserId userIdDouble = mock(UserId.class);
        when(user.identity()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user@cenas.com");

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertFalse(links.isEmpty());

        assertTrue(
                links.stream().anyMatch(l -> "self".equals(l.getRel().value())),
                "should contain self link"
        );
        assertTrue(
                links.stream().anyMatch(l -> "collection".equals(l.getRel().value())),
                "should contain collection link"
        );
        assertTrue(
                links.stream().anyMatch(l -> "delete".equals(l.getRel().value())),
                "should contain delete link"
        );
        assertTrue(
                links.stream().anyMatch(l -> "add-item".equals(l.getRel().value())),
                "should contain add-item link"
        );
        assertTrue(
                links.stream().anyMatch(l -> "create-list".equals(l.getRel().value())),
                "should contain create-list link"
        );
    }

    @Test
    void getLinks_containsOnlySeeListLinks_whenUserCanOnlySeeLists() {
        // Arrange
        when(authorizationPolicy.canSeeList(user)).thenReturn(true);
        when(authorizationPolicy.canDeleteList(user)).thenReturn(false);
        when(authorizationPolicy.canAddItemTo(user)).thenReturn(false);
        when(authorizationPolicy.canCreateList(user)).thenReturn(false);
        UserId userIdDouble = mock(UserId.class);
        when(user.identity()).thenReturn(userIdDouble);
        when(userIdDouble.toString()).thenReturn("user@cenas.com");

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertFalse(links.isEmpty());

        assertTrue(
                links.stream().anyMatch(l -> "self".equals(l.getRel().value())),
                "should contain self link"
        );
        assertTrue(
                links.stream().anyMatch(l -> "collection".equals(l.getRel().value())),
                "should contain collection link"
        );

        assertFalse(
                links.stream().anyMatch(l -> "delete".equals(l.getRel().value())),
                "should not contain delete link"
        );
        assertFalse(
                links.stream().anyMatch(l -> "add-item".equals(l.getRel().value())),
                "should not contain add-item link"
        );
        assertFalse(
                links.stream().anyMatch(l -> "create-list".equals(l.getRel().value())),
                "should not contain create-list link"
        );
    }

    @Test
    void getLinks_returnsEmpty_whenUserHasNoPermissions() {
        // Arrange
        when(authorizationPolicy.canSeeList(user)).thenReturn(false);
        when(authorizationPolicy.canDeleteList(user)).thenReturn(false);
        when(authorizationPolicy.canAddItemTo(user)).thenReturn(false);
        when(authorizationPolicy.canCreateList(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getLinks(user);

        // Assert
        assertTrue(links.isEmpty());
    }

}