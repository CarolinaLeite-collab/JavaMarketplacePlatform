package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenreRepoTest {
    @Test
    void constructorGenreRepo(){
        GenreRepo gr = new GenreRepo();

        assertFalse(gr.existsGenre("Action"));
    }

    @Test
    void genreExistsInRepo() {
        GenreRepo gr = new GenreRepo();
        gr.create("Science Fiction");
        assertTrue(gr.existsGenre("Science Fiction"));
    }

    @Test
    void genreRepoCreateNewGenre() {
        GenreRepo gr = new GenreRepo();
        gr.create("Action");
        assertTrue(gr.existsGenre("Action"));
        assertNotNull(gr);
    }

    @Test
    void genreAlreadyExistsInRepo() {
        GenreRepo gr = new GenreRepo();
        gr.create("Science Fiction");
        Genre result = gr.create("Science Fiction");
        assertNull(result);
    }

    @Test
    void genreIsAddedToRepo() {
        GenreRepo gr = new GenreRepo();
        assertFalse(gr.existsGenre("Science Fiction"));

        Genre g = gr.create("Science Fiction");
        assertNotNull(g);
        assertTrue(gr.existsGenre("Science Fiction"));
    }

    // Test for mutation ignore 'existsGenre'
    @Test
    void genreIsNotAddedInDuplicateInRepo() {
        GenreRepo gr = new GenreRepo();
        gr.create("Science Fiction");
        gr.create("Science Fiction");
        assertTrue(gr.existsGenre("Science Fiction"));
    }
}
