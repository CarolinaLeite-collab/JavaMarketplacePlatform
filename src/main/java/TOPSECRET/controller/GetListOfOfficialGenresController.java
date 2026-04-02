package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.IGenreRepo;

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

    private IGenreRepo _iGenreRepo;

    public GetListOfOfficialGenresController(IGenreRepo igr) {

        _iGenreRepo = igr;

    }

    public Iterable<Genre> getListOfOfficialGenres(){

        Iterable<Genre> listOfOfficialGenres = _iGenreRepo.findAll();

        return listOfOfficialGenres;

    }

}
