package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryIdTest {

    private Email _emailDouble;
    private UserId _userIdDouble;

    @BeforeEach
    void setUp() {

        _emailDouble = mock(Email.class);
        _userIdDouble = mock(UserId.class);
        when(_userIdDouble.getEmail()).thenReturn(_emailDouble);

    }

    @Test
    void testAConstructor(){

        //SUT
        new LibraryId(_emailDouble);

    }

    @Test
    void testFromUserIdIsSameWhenSameUserId(){

        //SUT
        LibraryId libraryId = new LibraryId(_emailDouble);

        //act
        LibraryId result = LibraryId.fromUserId(_userIdDouble);

        //assert
        assertEquals(libraryId, result);
    }

    @Test
    void testFromUserIdDifferentWhenDifferentUserId(){

        //arrange
        Email email2Double = mock(Email.class);
        UserId userId2Double = mock(UserId.class);
        when(userId2Double.getEmail()).thenReturn(email2Double);

        //SUT
        LibraryId libraryId = new LibraryId(_emailDouble);

        //act
        LibraryId result = LibraryId.fromUserId(userId2Double);

        //assert
        assertNotEquals(libraryId, result);

    }

    @Test
    void libraryIDisEqualWithItself(){
        //SUT
        LibraryId libraryID = new LibraryId(_emailDouble);

        //assert
        assertEquals(libraryID, libraryID);

    }

    @Test
    void libraryIDisNotEqualWithNull(){
        //SUT
        LibraryId libraryID = new LibraryId(_emailDouble);

        //assert
        assertNotEquals(null, libraryID);

    }

    @Test
    void libraryIDisNotEqualWithDifferentObjectType(){
        //arrange
        String  differentType = "differentType";

        //SUT
        LibraryId libraryID = new LibraryId(_emailDouble);

        //assert
        assertNotEquals(differentType, libraryID);

    }

    @Test
    void libraryIDisEqualWithAnotherLibraryIDWithSameEmail(){

        //SUT
        LibraryId libraryID = new LibraryId(_emailDouble);
        LibraryId result = new LibraryId(_emailDouble);

        //assert
        assertEquals(libraryID, result);
    }

    @Test
    void hashIsEqualWithSameEmail(){

        //SUT
        LibraryId libraryID = new LibraryId(_emailDouble);
        LibraryId result = new LibraryId(_emailDouble);

        //assert
        assertEquals(libraryID.hashCode(), result.hashCode());

    }

    @Test
    void hashIsNotEqualWithDifferentEmails(){

        //arrange
        Email email2Double = mock(Email.class);

        //SUT
        LibraryId libraryID = new LibraryId(_emailDouble);
        LibraryId result = new LibraryId(email2Double);

        //assert
        assertNotEquals(libraryID.hashCode(), result.hashCode());

    }

}