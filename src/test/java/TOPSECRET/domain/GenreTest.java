package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    void allGenreConstantsExist() {
        Genre[] genres = Genre.getAllGenres().toArray(new Genre[0]);
        assertEquals(33, genres.length);

        for (Genre genre : genres) {
            assertNotNull(genre); // checks all 33 genres are not null
            assertNotNull(genre.toString()); // checks all 33 genres in String format are not null
            assertEquals(Genre.fromString(genre.toString()), genre); // 'round-trip' validation (Genre -> String -> Genre)
        }
    }

    @Test
    void toStringRoundtripAllGenres() {
        // toString() → fromString() → original Genre
        // E.g., AUTOBIOGRAPHY -> "Autobiography" -> AUTOBIOGRAPHY
        for (Genre genre : Genre.getAllGenres()) {
            assertEquals(genre, Genre.fromString(genre.toString()));
        }
    }

    @Test
    void getAllOrderIsStable() {
        List<Genre> allGenres = Genre.getAllGenres();
        assertEquals(33, allGenres.size());
        assertEquals(Genre.ACTION, allGenres.get(0));
        assertEquals(Genre.CHILDREN, allGenres.get(5));
        assertEquals(Genre.YOUNG_ADULT, allGenres.get(32));
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