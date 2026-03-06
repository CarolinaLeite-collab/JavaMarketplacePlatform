package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddGenreControllerTest {

    //Set up test objects
    private User _admin;
    private GenreRepo _genreRepo;
    private AddGenreController _addGenreController;
    private Country _country;
    private CountryFactory _countryFactory;

    @BeforeEach
    void setUp() {
        _countryFactory = new CountryFactory();
        _country = _countryFactory.createClass("Portugal");
        _admin = new User(
                new Name ("Maria"),
                new Address ("Rua senhor de matosinhos", "81", Address.BuildingType.HOUSE, "Matosinhos", "Porto", _country, "4300-111", null ),
                new Email ("test@gmail.com"),
                new Phone( new PhonePrefix("+351"),"911234567"));

        _genreRepo = new GenreRepo();
        _addGenreController = new AddGenreController(_genreRepo, _admin);
    }

    @Test
    void contructorControllerAddGenre() {
        new AddGenreController(_genreRepo, _admin);
    }

    @Test
    void genreIsCreated() {
        Genre genre = _addGenreController.addGenre("Action");
        assertNotNull(genre);
        assertEquals("Action", genre.getGenre());
    }

    @Test
    void genreIsNullWhenExistsInRepo() {
        _addGenreController.addGenre("Action");
        Genre genre = _addGenreController.addGenre("Action");
        assertNull(genre);
    }

}
