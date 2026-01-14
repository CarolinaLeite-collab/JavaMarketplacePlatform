package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

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

}