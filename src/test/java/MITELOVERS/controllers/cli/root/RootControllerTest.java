package MITELOVERS.controllers.cli.root;

import MITELOVERS.controllers.rest.root.RootController;
import MITELOVERS.controllers.rest.root.RootLinkProvider;
import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RootControllerTest {

    private final IUserRepo userRepo = mock(IUserRepo.class);
    private final RootLinkProvider linkProvider = mock(RootLinkProvider.class);

    private final RootController controller =
            new RootController(List.of(linkProvider), userRepo);

    @Test
    void shouldReturnRootWithSelfLinkAndProviderLinks() {
        // Arrange
        String email = "test@email.com";
        User user = mock(User.class);

        when(userRepo.ofIdentity(new UserId(new Email(email))))
                .thenReturn(Optional.of(user));

        Link myListsLink = Link.of("/api/my-lists").withRel("myLists");

        when(linkProvider.getLinks(user))
                .thenReturn(List.of(myListsLink));

        // Act
        RepresentationModel<?> result = controller.root(email);

        // Assert
        assertTrue(result.hasLink("self"));
        assertTrue(result.hasLink("myLists"));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        String email = "missing@email.com";

        when(userRepo.ofIdentity(new UserId(new Email(email))))
                .thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.root(email)
        );

        assertEquals("User not found: " + email, exception.getMessage());
    }
}