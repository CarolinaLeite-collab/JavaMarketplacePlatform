package TOPSECRET.domain.library;

import TOPSECRET.domain.valueobject.LibraryId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LibraryFactoryTest {

    @Test
    void createLibrary_shouldReturnLibraryWithCorrectUser() {
        // Arrange
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
            Library newLibrary = factory.createLibrary(libraryIdDouble);

            //Assert
            assertEquals(libraryIdDouble, newLibrary.identity());
        }

    }

}