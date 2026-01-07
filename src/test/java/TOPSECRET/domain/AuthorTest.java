package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

}
