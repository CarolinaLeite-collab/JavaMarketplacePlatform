package MITELOVERS.controller;


import MITELOVERS.controllers.cli.CreateLibraryController;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
class CreateLibraryControllerTest {

    @Mock
    private ILibraryRepo _libraryRepo;

    @Mock
    private LibraryFactory _libraryFactory;

    @Mock
    private UserId _userId;

    @Mock
    private Library _library;

    @InjectMocks
    private CreateLibraryController _controller;


    @Test
    void controllerShouldInstantiate() {
        new CreateLibraryController(_libraryRepo, _libraryFactory);
    }

    @Test
    void shouldCreateLibrarySuccessfully() {

        // Arrange
        when(_libraryFactory.createLibrary(_userId)).thenReturn(_library);
        when(_library.identity()).thenReturn(new LibraryId(new Email("test@example.com")));
        when(_libraryRepo.containsOfIdentity(_library.identity())).thenReturn(false);

        // Act
        boolean result = _controller.createLibrary(_userId);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldThrowWhenLibraryAlreadyExists() {

        // Arrange
        when(_libraryFactory.createLibrary(_userId)).thenReturn(_library);
        when(_library.identity()).thenReturn(new LibraryId(new Email("test@example.com")));
        when(_libraryRepo.containsOfIdentity(_library.identity())).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalStateException.class,
                () -> _controller.createLibrary(_userId));
    }

}

