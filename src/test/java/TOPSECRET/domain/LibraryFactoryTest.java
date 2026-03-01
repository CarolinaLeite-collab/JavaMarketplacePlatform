package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LibraryFactoryTest {

    @Test
    void createMyLibrary_shouldReturnLibraryWithCorrectUser() {
        // Arrange
        User userDouble = mock(User.class);
        LibraryFactory factory = new LibraryFactory();

        // Act
        Library myLibrary = factory.createMyLibrary(userDouble);

        // Assert
        assertNotNull(myLibrary);
        assertEquals(userDouble, myLibrary.getUser());
    }

}