package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetPublicListsByGenreControllerTest {

    private GenreRepo _genreRepo;
    private UserRepo _userRepo;
    private ListOfPublicationsRepo _repo;

    private GetPublicListsByGenreController _controller;

    private Genre _action;
    private User _user1;

    @BeforeEach
    void setUp() {
        _genreRepo = new GenreRepo();
        _userRepo = new UserRepo();
        _repo = new ListOfPublicationsRepo(_genreRepo);

        _controller = new GetPublicListsByGenreController(_repo);

        _action = _genreRepo.create("Fiction");
        assertNotNull(_action);

        _user1 = _userRepo.registerNewUser("User One", "user1@mail.com");
        assertNotNull(_user1);

        ListOfPublications a = _repo.createListOfPublications(_user1, "List A", _action);
        assertNotNull(a);
        a.makePublic();
    }

    @Test
    void controllerShouldReturnPublicListsByGenre() {
        List<ListOfPublications> result = _controller.getPublicListsByGenre(_action);

        assertEquals(1, result.size());
        assertEquals("List A", result.get(0).getName());
        assertEquals(_user1, result.get(0).getUser());
    }

    @Test
    void controllerShouldThrowWhenGenreIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> _controller.getPublicListsByGenre(null)
        );
        assertEquals("Genre is mandatory", ex.getMessage());
    }
}