package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GenreRepoTest {
    @Test
    void genreRepoCreateNewGenre() {
        GenreRepo gr = new GenreRepo();
        Genre g = gr.create("Action");

        assertEquals("Action", g.getGenre());   // correct content
        assertNotNull(g);   // created
    }

    @Test
    void genreAlreadyExistsInRepo() {
        GenreRepo gr = new GenreRepo();
        gr.create("Science Fiction");
        Genre g = gr.create("Science Fiction");
        assertNull(g);
    }

}
