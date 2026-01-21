package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.GenreRepo;
import TOPSECRET.domain.User;

import java.util.ArrayList;
import java.util.List;

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
