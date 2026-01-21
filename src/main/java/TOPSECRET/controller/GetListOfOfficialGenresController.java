package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreRepo;
import TOPSECRET.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving the list of official genres.
 * <p>
 * This controller acts as an intermediary between the user interface
 * and the domain layer, delegating the retrieval of official genres
 * to the {@link GenreRepo}.
 * </p>
 */

public class GetListOfOfficialGenresController {

    private GenreRepo _gr;

    public GetListOfOfficialGenresController(GenreRepo gr, User user) {

        _gr = gr;

    }

    public List<Genre> getListOfOfficialGenres(){

        List<Genre> ListOfOfficialGenres = _gr.getListOfOfficialGenres();

        return ListOfOfficialGenres;

    }

}
