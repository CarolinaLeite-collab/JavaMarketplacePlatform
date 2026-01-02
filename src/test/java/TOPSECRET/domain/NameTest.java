package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    void shouldCreateNameWithValidSimpleName() {
        Name name = new Name("Jose Mourinho");
        assertEquals("Jose Mourinho", name.get_Name());
    }

    @Test
    void shouldAllowAccentedLetters() {
        Name name = new Name("José Mourinho");
        assertEquals("José Mourinho", name.get_Name());
    }

    @Test
    void shouldAllowHyphenAndApostrophe() {
        Name n1 = new Name("Ana-Maria");
        assertEquals("Ana-Maria", n1.get_Name());

        Name n2 = new Name("D'Avila");
        assertEquals("D'Avila", n2.get_Name());
    }

    @Test
    void shouldNormalizeSpacesTrimAndCollapse() {
        Name name = new Name("   José     Mourinho   ");
        assertEquals("José Mourinho", name.get_Name());
    }

    @Test
    void shouldRejectNull() {
        assertThrows(IllegalArgumentException.class, () -> new Name(null));
    }

    @Test
    void shouldRejectBlankOrOnlySpaces() {
        assertThrows(IllegalArgumentException.class, () -> new Name(""));
        assertThrows(IllegalArgumentException.class, () -> new Name("   "));
        assertThrows(IllegalArgumentException.class, () -> new Name("\t \n"));
    }

    @Test
    void shouldRejectTooShortOrTooLong() {
        assertThrows(IllegalArgumentException.class, () -> new Name("A"));

        String longName = "A".repeat(81);
        assertThrows(IllegalArgumentException.class, () -> new Name(longName));
    }

    @Test
    void shouldAcceptBoundaryLengths() {
        Name min = new Name("Al"); // 2 chars
        assertEquals("Al", min.get_Name());

        Name max = new Name("A".repeat(80)); // 80 chars
        assertEquals("A".repeat(80), max.get_Name());
    }

    @Test
    void shouldRejectNumbers() {
        assertThrows(IllegalArgumentException.class, () -> new Name("Jose2 Mourinho"));
        assertThrows(IllegalArgumentException.class, () -> new Name("1234"));
    }

    @Test
    void shouldRejectInvalidSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new Name("Jose_Mourinho"));
        assertThrows(IllegalArgumentException.class, () -> new Name("Jose@Mourinho"));
        assertThrows(IllegalArgumentException.class, () -> new Name("Jose.Mourinho"));
    }

    @Test
    void shouldRejectStartingOrEndingWithSeparator() {
        assertThrows(IllegalArgumentException.class, () -> new Name("-Jose"));
        assertThrows(IllegalArgumentException.class, () -> new Name("Jose-"));
        assertThrows(IllegalArgumentException.class, () -> new Name("'Jose"));
        assertThrows(IllegalArgumentException.class, () -> new Name("Jose'"));
    }

    @Test
    void toStringShouldReturnValue() {
        Name name = new Name("José Mourinho");
        assertEquals("José Mourinho", name.toString());
    }
}