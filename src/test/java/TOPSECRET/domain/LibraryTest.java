package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private User _user;

    @BeforeEach
    void setUp() {

        _user = new User (
                new Name ("Zé Isep"),
                new Email ("test@isep.com")
        );

    }



    @Test
    void testConstructor() {

        new Library(_user);

    }

    @Test
    void test_get_userID() {

        //arrange and act
        Library myLibrary = new Library(_user);
        User userID = myLibrary.getUser();

        //assert
        assertEquals(userID,myLibrary.getUser());

    }



    @Test
    void getPublicationsInLibraryShouldReturnEmptyListWhenNoPublications() {
        // Arrange
        Library library = new Library(_user);

        // Act
        List<PublicationDetails> result = library.getPublicationsInLibrary();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPublicationsInLibraryShouldReturnPublicationsWhenPublicationsExist() {
        //missing add publication method to library
    }

    @Test
    void addPublicationToLibraryShouldAddPublicationWhenValid() {
        // Verifies that a valid publication is successfully added to the library

        // Arrange
        Library library = new Library(_user);

        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        // Act
        boolean result = library.addPublicationToLibrary(p);

        // Assert
        assertTrue(result);
        assertEquals(1, library.getAllPublications().size());
        assertTrue(library.getAllPublications().contains(p));
    }

    @Test
    void getAllPublicationsShouldReturnEmptyListWhenLibraryIsEmpty() {
        // Verifies that an empty library returns an empty list of publications

        // Arrange
        Library library = new Library(_user);

        // Act
        List<Publication> result = library.getAllPublications();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationsShouldReturnUnmodifiableList() {
        // Ensures that getAllPublications returns an unmodifiable list

        // Arrange
        Library library = new Library(_user);

        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        library.addPublicationToLibrary(p);

        // Act
        List<Publication> result = library.getAllPublications();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(p));
    }

    @Test
    void addPublicationToLibraryShouldNotAllowDuplicatePublication() {
        // Checks that the library rejects duplicate publications

        // Arrange
        Library library = new Library(_user);

        Publication p = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(new Publisher("Penguin"))
                .build();

        library.addPublicationToLibrary(p);

        // Act
        boolean result = library.addPublicationToLibrary(p);

        // Assert
        assertFalse(result);
        assertEquals(1, library.getAllPublications().size());
    }

    @Test
    void addPublicationToLibraryShouldReturnFalseWhenPublicationIsNull() {
        // Ensures that null publications are not added to the library

        // Arrange
        Library library = new Library(_user);

        // Act
        boolean result = library.addPublicationToLibrary(null);

        // Assert
        assertFalse(result);
        assertTrue(library.getAllPublications().isEmpty());
    }


}