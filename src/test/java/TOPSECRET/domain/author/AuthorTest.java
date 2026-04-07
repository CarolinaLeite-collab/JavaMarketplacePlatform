package TOPSECRET.domain.author;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorTest {
    @Test
    void validNameAuthor() {
        Author a = new Author("Eça de Queirós");
        assertEquals("Eça de Queirós", a.getName());
    }

    @Test
    void authorNameIsTrimmed() {
        Author a1 = new Author(" Eça de Queirós ");
        assertEquals("Eça de Queirós", a1.getName());
    }

    @Test
    void capitalizationNameTest() {
        Author a2 = new Author("Eça De Queirós");
        Author a3 = new Author("EÇA DE QUEIRÓS");
        Author a4 = new Author("eça de queirós");

        assertEquals(a2.getLowerCaseName(), a3.getLowerCaseName());
        assertEquals(a2.getLowerCaseName(), a4.getLowerCaseName());
    }

    @Test
    void authorNameIsTrimmedAndLowerCased() {
        Author a5 = new Author("  EÇA DE QUEIRÓS  ");
        assertEquals("eça de queirós", a5.getLowerCaseName());
    }

    @Test
    void rejectEmptyNameAuthor() { assertThrows(IllegalArgumentException.class, () -> {new Author("   ");}); }

    @Test
    void rejectNullNameAuthor() { assertThrows(IllegalArgumentException.class, () -> {new Author(null);}); }

    @Test
    void testEqualsWithDifferentObjectTypes(){

        //act and arrange
        Author a = new Author("Seneca");
        String b = "Seneca";
        Author b2 = null;

        //assert
        assertFalse(a.equals(b));
        assertFalse(a.equals(b2));

    }

    @Test
    void testEqualsWithSameObject() {

        //act and arrange
        Author a = new Author("Seneca");

        //assert
        assertTrue(a.equals(a));

    }

    @Test
    void testEqualsWithDifferentAuthorObjectsSameName() {

        //act and arrange
        Author a = new Author("Seneca");
        Author b = new Author("SeNeca");

        //assert
        assertTrue(a.equals(b));

    }

    @Test
    void testEqualsWithDifferentAuthorObjectsDifferentName() {

        //act and arrange
        Author a = new Author("Seneca");
        Author b = new Author("Justinian");

        //assert
        assertFalse(a.equals(b));

    }

    @Test
    void test_equal_hash_code(){

        //act and arrange
        Author a = new Author("Seneca");
        Author a2 = new Author("SeneCA");

        //assert
        assertEquals(a.hashCode(), a2.hashCode());

    }

    @Test
    void test_non_equal_hash_code(){

        //act and arrange
        Author a = new Author("Seneca");
        Author a2 = new Author("SeneCAR");

        //assert
        assertNotEquals(a.hashCode(), a2.hashCode());

    }

}
