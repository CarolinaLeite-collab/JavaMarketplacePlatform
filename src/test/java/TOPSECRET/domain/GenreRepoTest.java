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
        Genre SciFi = new Genre("Science Fiction");

        //act
        List<Genre> listOfOfficialGenres = gr.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);
        assertEquals(2, listOfOfficialGenres.size());
        assertFalse(listOfOfficialGenres.isEmpty());
        assertTrue(listOfOfficialGenres.contains(SciFi));

    }

    @Test
    void test_get_list_of_genres_should_return_list_of_genres() {
        GenreRepo gr = new GenreRepo();

        List<Genre> listOfOfficialGenres = gr.getListOfOfficialGenres();

        //assert
        assertNotNull(listOfOfficialGenres);
        assertTrue(listOfOfficialGenres.isEmpty());


    }

}
