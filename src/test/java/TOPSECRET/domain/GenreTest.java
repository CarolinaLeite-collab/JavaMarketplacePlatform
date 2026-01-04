package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    void allGenreConstantsExist() {
        Genre[] genres = Genre.values();
        assertEquals(33, genres.length);

        for (Genre genre : genres) {
            assertNotNull(genre); // checks all 33 genres are not null
            assertNotNull(genre.toString()); // checks all 33 genres in String format are not null
            assertEquals(Genre.valueOf(genre.name()), genre); // 'round-trip' validation (Genre -> String -> Genre)
        }
    }

    @Test
    void toStringRoundtripAllGenres() {
        // toString() → fromString() → original Genre
        // E.g., AUTOBIOGRAPHY -> "Autobiography" -> AUTOBIOGRAPHY
        for (Genre genre : Genre.values()) {
            assertEquals(genre, Genre.fromString(genre.toString()));
        }
    }

    @Test
    void genreOrdinalOrderIsStable() {
        assertEquals(0, Genre.ACTION.ordinal());
        assertEquals(5, Genre.CHILDREN.ordinal());
        assertEquals(32, Genre.YOUNG_ADULT.ordinal());
    }

    @Test
    void differentGenresAreNotEqual() {
        assertNotEquals(Genre.TECHNOLOGY, Genre.CHILDREN);
    }

    @Test
    void fromStringHandlesDifferentValidInputs() {
        assertEquals(Genre.SCI_FI, Genre.fromString("  sci-Fi"));
        assertEquals(Genre.SCI_FI, Genre.fromString("sci fi"));
        assertEquals(Genre.GRAPHIC_NOVEL, Genre.fromString("graphic novel"));
        assertEquals(Genre.SELF_HELP, Genre.fromString("self help "));
    }

    @Test
    void fromStringInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> Genre.fromString(""));
        assertThrows(IllegalArgumentException.class, () -> Genre.fromString(" "));
        assertThrows(IllegalArgumentException.class, () -> Genre.fromString(null));
        assertThrows(IllegalArgumentException.class, () -> Genre.fromString("Invalid"));
        assertThrows(IllegalArgumentException.class, () -> Genre.fromString("Funny book"));
    }

}