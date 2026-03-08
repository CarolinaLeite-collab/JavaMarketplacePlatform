package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetPublicListsByGenreControllerTest {

    private GenreFactory _genreFactory;
    private GenreRepo _genreRepo;
    private UserRepo _userRepo;
    private ListOfPublicationsRepo _listOfPubsRepo;
    private ListOfPublicationsFactory _listOfPubsFactory;

    private GetPublicListsByGenreController _controller;

    private Genre _action;
    private User _user1;

    @BeforeEach
    void setUp() {
        _genreFactory = new GenreFactory();
        _genreRepo = new GenreRepo(_genreFactory);
        _userRepo = new UserRepo();
        _listOfPubsRepo = new ListOfPublicationsRepo();
        _listOfPubsFactory = new ListOfPublicationsFactory();

        _controller = new GetPublicListsByGenreController(_listOfPubsRepo);

        _action = _genreRepo.addGenre("Fiction");
        assertNotNull(_action);

        _user1 = _userRepo.registerNewUser("User One", "user1@mail.com");
        assertNotNull(_user1);

        ListOfPublications a = _listOfPubsFactory.createListOfPublications(_user1, "List A", _action);
        assertNotNull(a);
        a.makePublic();

        // save in repo so controller can see it
        _listOfPubsRepo.addListOfPublications(a);
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