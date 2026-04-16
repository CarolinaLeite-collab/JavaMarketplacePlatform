package TOPSECRET.domain.library;

import TOPSECRET.domain.valueobject.LibraryId;
import TOPSECRET.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LibraryFactoryTest {

    @Test
    void createLibrary_shouldReturnLibraryWithCorrectUser() {
        // Arrange
        UserId userId = mock(UserId.class);
        LibraryId libraryIdDouble = mock(LibraryId.class);

        //SUT
        LibraryFactory factory = new LibraryFactory();

        try (MockedConstruction<Library> mocked =
                     mockConstruction(Library.class,
                             (mock, context) -> {
                                 when(mock.identity())
                                         .thenReturn(libraryIdDouble);
                             })) {
            //Act
            Library newLibrary = factory.createLibrary(userId);

            //Assert
            assertEquals(libraryIdDouble, newLibrary.identity());
        }

    }

}