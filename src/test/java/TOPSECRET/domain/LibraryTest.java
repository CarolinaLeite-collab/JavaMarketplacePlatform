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
}