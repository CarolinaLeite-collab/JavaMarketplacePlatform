package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LibraryFactoryTest {

    @Test
    void createLibrary_shouldReturnLibraryWithCorrectUser() {
        // Arrange
        User userDouble = mock(User.class);

        //SUT
        LibraryFactory factory = new LibraryFactory();

        try (MockedConstruction<Library> mocked =
                     mockConstruction(Library.class,
                             (mock, context) -> {
                                 when(mock.getUser())
                                         .thenReturn(userDouble);
                             })) {
            //Act
            Library newLibrary = factory.createLibrary(userDouble);

            //Assert
            assertEquals(userDouble, newLibrary.getUser());
        }

    }

}