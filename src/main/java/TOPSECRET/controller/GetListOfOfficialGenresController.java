package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.IGenreRepo;
import TOPSECRET.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving the list of official genres.
 * <p>
 * This controller acts as an intermediary between the user interface
 * and the domain layer, delegating the retrieval of official genres
 * to the {@link IGenreRepo}.
 * </p>
 */

public class GetListOfOfficialGenresController {

    private IGenreRepo _igr;

    public GetListOfOfficialGenresController(IGenreRepo igr, User user) {

        _igr = igr;

    }

    public List<Genre> getListOfOfficialGenres(){

        List<Genre> listOfOfficialGenres = _igr.getListOfOfficialGenres();

        return listOfOfficialGenres;

    }

}
