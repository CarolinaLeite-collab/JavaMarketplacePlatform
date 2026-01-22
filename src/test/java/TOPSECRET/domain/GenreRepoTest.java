package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenreRepoTest {

    @Test
    void test_a_constructor_genre_repo(){

        //act
        new GenreRepo();

    }

    @Test
    void testAddGenreCreateNewGenre() {
        GenreRepo gr = new GenreRepo();
        Genre g = gr.addGenre("Action");

        assertEquals("Action", g.getGenre());   // correct content
        assertNotNull(g);   // created
    }

    @Test
    void testAddGenreIfGenreAlreadyExistsInRepo() {
        GenreRepo gr = new GenreRepo();
        gr.addGenre("Science Fiction");
        Genre g = gr.addGenre("Science Fiction");
        assertNull(g);
    }

    @Test
    void test_get_list_of_genres_should_return_list_with_genres() {

        //arrange
        GenreRepo gr = new GenreRepo();
        gr.addGenre("Science Fiction");
        gr.addGenre("Romance");

        //act
        List<Genre> ListOfOfficialGenres = gr.getListOfOfficialGenres();

        //assert
        assertNotNull(ListOfOfficialGenres);

    }

}
