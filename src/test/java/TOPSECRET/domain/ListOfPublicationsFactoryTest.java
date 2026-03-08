package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ListOfPublicationsFactoryTest {

    @Test
    void createListOfPublications_validInputs_createsPrivateList() {
        // Arrange
        ListOfPublicationsFactory factory = new ListOfPublicationsFactory();
        User user = mock(User.class);
        Genre genre = mock(Genre.class);

        // Act
        ListOfPublications list = factory.createListOfPublications(user, "My List", genre);

        // Assert
        assertNotNull(list);
        assertSame(user, list.getUser());
        assertEquals("My List", list.getName());
        assertSame(genre, list.getGenre());
        assertTrue(list.isPrivate());
    }

    @Test
    void createPublicListOfPublications_validInputs_createsPublicList() {
        // Arrange
        ListOfPublicationsFactory factory = new ListOfPublicationsFactory();
        User user = mock(User.class);
        Genre genre = mock(Genre.class);

        // Act
        ListOfPublications list = factory.createPublicListOfPublications(user, "My List", genre);

        // Assert
        assertNotNull(list);
        assertSame(user, list.getUser());
        assertEquals("My List", list.getName());
        assertSame(genre, list.getGenre());
        assertFalse(list.isPrivate());
    }

    @Test
    void createListOfPublications_nullUser_throwsIllegalArgumentException() {
        // Arrange
        ListOfPublicationsFactory factory = new ListOfPublicationsFactory();
        Genre genre = mock(Genre.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.createListOfPublications(null, "My List", genre));
    }

    @Test
    void createListOfPublications_nullName_throwsIllegalArgumentException() {
        // Arrange
        ListOfPublicationsFactory factory = new ListOfPublicationsFactory();
        User user = mock(User.class);
        Genre genre = mock(Genre.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.createListOfPublications(user, null, genre));
    }

    @Test
    void createListOfPublications_nullGenre_throwsIllegalArgumentException() {
        // Arrange
        ListOfPublicationsFactory factory = new ListOfPublicationsFactory();
        User user = mock(User.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.createListOfPublications(user, "My List", null));
    }
}