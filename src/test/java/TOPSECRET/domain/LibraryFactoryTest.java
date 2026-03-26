package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class LibraryFactoryTest {

    @Test
    void createLibrary_shouldReturnLibraryWithCorrectUser() {
        // Arrange
        User userDouble = mock(User.class);
        LibraryFactory factory = new LibraryFactory();

        // Act
        Library myLibrary = factory.createLibrary(userDouble);

        // Assert
        assertNotNull(myLibrary);
        assertEquals(userDouble, myLibrary.getUser());
    }

}