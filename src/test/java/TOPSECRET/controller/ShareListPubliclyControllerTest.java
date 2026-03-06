package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShareListPubliclyControllerTest {

    private ShareListPubliclyController _controller;
    private ListOfPublications _listOfPublications;
    private ListOfPublicationsRepo _listOfPublicationsRepo;
    private GenreRepo _genreRepo;
    private Genre _genrePoetry;
    private Genre _genreAction;
    private User _user;
    private CountryFactory _countryFactory;
    private Country _country;

    @BeforeEach
    void setUp() {
        _genreRepo = new GenreRepo();
        _genrePoetry = _genreRepo.addGenre("Poetry");
        _genreAction = _genreRepo.addGenre("Action");

        _listOfPublicationsRepo = new ListOfPublicationsRepo();
        _controller = new ShareListPubliclyController(_listOfPublicationsRepo);
        _countryFactory = new CountryFactory();
        _country = _countryFactory.createClass("Portugal");
        _user = new User(new Name("Maria"),
                new Address("Rua Vasco da Gama", "123", Address.BuildingType.HOUSE, "Lisboa",
                        "Lisboa", _country, "1000-205", null),
                new Email("maria123@hotmail.com"),
                new Phone(new PhonePrefix("+351"), "918902632"));
    }

    @Test
    void returnListFromRepo() {
        // getListOfLists() – returns lists stored in the repository for the given user
        ListOfPublications list = new ListOfPublications(_user, "MyActionList", _genreAction);
        _listOfPublicationsRepo.addListOnRepo(list);

        List<ListOfPublications> result = _controller.getListOfLists(_user);

        assertEquals(1, result.size());
        assertEquals(list, result.get(0));
    }

    @Test
    void returnFalseWhenSelectedListIsNull() {
        // shareListPublicly() – returns false when selected list is null
        boolean result = _controller.shareListPublicly(null);
        assertFalse(result);
    }

    @Test
    void makesListPublicWhenInitiallyPrivate() {
        // shareListPublicly() – changes list visibility from private to public
        ListOfPublications list = new ListOfPublications(_user, "MyPoetryList", _genrePoetry);
        _listOfPublicationsRepo.addListOnRepo(list);

        assertTrue(list.isPrivate());

        boolean result = _controller.shareListPublicly(list);

        assertTrue(result);
        assertFalse(list.isPrivate());
    }

    @Test
    void makesListPublicAndChangeBackToPrivate() {
        // shareListPublicly() – toggles list visibility back to private when already public
        ListOfPublications list = new ListOfPublications(_user, "MyActionList", _genreAction);
        _listOfPublicationsRepo.addListOnRepo(list);

        _controller.shareListPublicly(list); // turn public
        assertFalse(list.isPrivate());

        _controller.shareListPublicly(list); // back to private
        assertTrue(list.isPrivate());
    }
}
