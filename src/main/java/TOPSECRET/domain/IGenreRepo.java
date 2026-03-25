package TOPSECRET.domain;

import java.util.List;

public interface IGenreRepo {

    Genre addGenre(String genreName) throws IllegalArgumentException;
    List<Genre> getListOfOfficialGenres();
}
