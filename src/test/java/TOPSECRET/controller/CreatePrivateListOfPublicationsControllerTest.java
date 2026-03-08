package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePrivateListOfPublicationsControllerTest {

    private User _user1;
    private Genre _actionGenre;
    private Genre _poetryGenre;
    private GenreRepo _genreRepo;
    private GenreFactory _genreFactory = new GenreFactory();
    private ListOfPublicationsRepo _repo;
    private CreatePrivateListOfPublicationsController _controller;

    @BeforeEach
    void setUp() {
        _user1 = new User(new Name("Joaquim"), new Email("test@isep.com"));

        _genreRepo = new GenreRepo(_genreFactory);
        _actionGenre = _genreRepo.addGenre("Action");
        _poetryGenre = _genreRepo.addGenre("poetry");

        _repo = new ListOfPublicationsRepo();
        _controller = new CreatePrivateListOfPublicationsController(_repo, _genreRepo, _user1);
    }

    @Test
    void shouldcreateListSuccessfully() {
        // Arrange & Act
        ListOfPublications list = _controller.createListOfPublications(_user1, "My List", _actionGenre);

        // Assert
        assertNotNull(list);
        assertEquals(1, _repo.getListOfListOfPublications().size());
    }

    @Test
    void shouldNotCreateDuplicateList() {
        // Arrange
        _controller.createListOfPublications(_user1, "My List", _actionGenre);

        // Act
        ListOfPublications duplicate = _controller.createListOfPublications(_user1, "My List", _actionGenre);

        // Assert
        assertNull(duplicate);
        assertEquals(1, _repo.getListOfListOfPublications().size());
    }

    @Test
    void getListOfOfficialGenresReturnsUnmodifiableList() {
        List<Genre> officialGenres = _controller.getListOfOfficialGenres();

        assertThrows(UnsupportedOperationException.class, () -> officialGenres.add(new Genre("Horror")));
    }

    @Test
    void getListOfOfficialGenresReturnsCorrectList() {
        List<Genre> officialGenres = _controller.getListOfOfficialGenres();

        assertNotNull(officialGenres);
        assertEquals(2, officialGenres.size());
        assertTrue(officialGenres.contains(_actionGenre));
        assertTrue(officialGenres.contains(_poetryGenre));
    }




}