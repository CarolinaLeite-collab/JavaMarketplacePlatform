package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    void validGenre() {
        Genre g = new Genre("Science Fiction");
        assertEquals("Science Fiction", g.getGenre());
    }

    @Test
    void genreIsTrimmed() {
        Genre g = new Genre(" Science Fiction  ");
        assertEquals("Science Fiction", g.getGenre());
    }

    @Test
    void emptyGenre() {
        assertThrows(IllegalArgumentException.class, () -> new Genre("  "));
    }

    @Test
    void nullGenre() {
        assertThrows(IllegalArgumentException.class, () -> new Genre(null));
    }

    @Test
    void genreNotEqualsToNull() {
        Genre g = new Genre("Science Fiction");
        assertNotEquals(g, null);
    }

    @Test
    void genreWithSameName() {
        Genre g = new Genre("Romance ");
        Genre g1 = new Genre("ROMANCE");
        assertEquals(g, g1);
    }

    @Test
    void genreEqualsItself() {
        Genre g = new Genre("Science Fiction");
        assertEquals(g, g);
    }

    @Test
    void genreNotEqualsToDifferentType() {
        Genre g = new Genre("Science Fiction");
        assertNotEquals(g, "Science Fiction");
    }

    @Test
    void differentGenresAreNotEqual() {
        Genre g = new Genre("Science Fiction");
        Genre g1 = new Genre("ROMANCE");
        assertNotEquals(g, g1);
    }

    @Test
    void genreWithSameNameHaveSameHashCode() {
        Genre g = new Genre("Romance ");
        Genre g1 = new Genre("ROMANCE");
        assertEquals(g.hashCode(), g1.hashCode());
    }

    @Test
    void genreWithDifferentNameHaveDifferentHashCode() {
        Genre g = new Genre("Science Fiction");
        Genre g1 = new Genre("ROMANCE");
        assertNotEquals(g.hashCode(), g1.hashCode());
    }

    @Test
    void genreToString() {
        Genre g = new Genre("Science Fiction");
        assertEquals("Science Fiction", g.toString());
    }

}