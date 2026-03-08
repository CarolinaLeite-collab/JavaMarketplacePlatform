package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListOfPublicationsFactoryTest {

    private ListOfPublicationsFactory factory;
    private User user;
    private Genre genre;

    @BeforeEach
    void setUp() {
        factory = new ListOfPublicationsFactory();
        user = new User(new Name("José Sousa"), new Email("jose@gmail.com"));
        genre = new Genre("Fantasy");
    }

    @Test
    void createListOfPublications_validArguments_createsPrivateList() {
        // Arrange
        String name = "My List";

        // Act
        ListOfPublications list = factory.createListOfPublications(user, name, genre);

        // Assert
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(user, list.getUser()),
                () -> assertEquals("My List", list.getName()),
                () -> assertEquals(genre, list.getGenre()),
                () -> assertTrue(list.isPrivate())
        );
    }

    @Test
    void createPublicListOfPublications_validArguments_createsPublicList() {
        // Arrange
        String name = "Public List";

        // Act
        ListOfPublications list = factory.createPublicListOfPublications(user, name, genre);

        // Assert
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(user, list.getUser()),
                () -> assertEquals("Public List", list.getName()),
                () -> assertEquals(genre, list.getGenre()),
                () -> assertFalse(list.isPrivate())
        );
    }

    @Test
    void createListOfPublications_nullUser_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createListOfPublications(null, "My List", genre));
    }

    @Test
    void createListOfPublications_nullName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createListOfPublications(user, null, genre));
    }

    @Test
    void createListOfPublications_nullGenre_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createListOfPublications(user, "My List", null));
    }

    @Test
    void createPublicListOfPublications_nullUser_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createPublicListOfPublications(null, "My List", genre));
    }

    @Test
    void createPublicListOfPublications_nullName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createPublicListOfPublications(user, null, genre));
    }

    @Test
    void createPublicListOfPublications_nullGenre_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.createPublicListOfPublications(user, "My List", null));
    }
}