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
    void libraryIdIsEqualWithItself(){
        //SUT
        LibraryId libraryId = new LibraryId(_emailDouble);

        //assert
        assertEquals(libraryId, libraryId);

    }

    @Test
    void libraryIdIsNotEqualWithNull(){
        //SUT
        LibraryId libraryId = new LibraryId(_emailDouble);

        //assert
        assertNotEquals(null, libraryId);

    }

    @Test
    void libraryIdIsNotEqualWithDifferentObjectType(){
        //arrange
        String  differentType = "differentType";

        //SUT
        LibraryId libraryId = new LibraryId(_emailDouble);

        //assert
        assertFalse(libraryId.equals(differentType));

    }

    @Test
    void libraryIdIsEqualWithAnotherLibraryIdWithSameEmail(){

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

    @Test
    void testToString(){

        // Arrange
        when(_emailDouble.toString()).thenReturn("email@email.com");

        // SUT
        LibraryId libraryId = new LibraryId(_emailDouble);

        // Act
        String result = libraryId.toString();

        // Assert
        assertEquals("email@email.com", result);
    }

}