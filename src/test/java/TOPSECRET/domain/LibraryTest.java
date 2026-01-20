package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    void testConstructor() {

        new Library("myOwner");

    }

    @Test
    void test_get_userID() {

        //arrange and act
        Library myLibrary = new Library("myOwner");
        String userID = myLibrary.getUserID();

        //assert
        assertEquals(userID,myLibrary.getUserID());

    }



    @Test
    void getPublicationsInLibraryShouldReturnEmptyListWhenNoPublications() {
        // Arrange
        Library library = new Library("myOwner");

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