package MITELOVERS.controller;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateLibraryControllerTest {

    @Mock
    private IRepository<LibraryId, Library> libraryRepo;

    @Mock
    private LibraryFactory libraryFactory;

    @Mock
    private UserId userId;

    @Mock
    private Library library;

    private CreateLibraryController controller;

    @BeforeEach
    void setUp() {
        controller = new CreateLibraryController(libraryRepo, libraryFactory);
    }

    @Test
    void controllerShouldInstantiate() {
        new CreateLibraryController(libraryRepo, libraryFactory);
    }

    @Test
    void shouldCreateLibrarySuccessfully() {

        // Arrange
        when(libraryFactory.createLibrary(userId)).thenReturn(library);
        when(library.identity()).thenReturn(new LibraryId(new Email("test@example.com")));
        when(libraryRepo.containsOfIdentity(library.identity())).thenReturn(false);

        // Act
        boolean result = controller.createLibrary(userId);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldThrowWhenLibraryAlreadyExists() {

        // Arrange
        when(libraryFactory.createLibrary(userId)).thenReturn(library);
        when(library.identity()).thenReturn(new LibraryId(new Email("test@example.com")));
        when(libraryRepo.containsOfIdentity(library.identity())).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> controller.createLibrary(userId));
    }

}

