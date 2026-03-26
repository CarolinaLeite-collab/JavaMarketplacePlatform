package TOPSECRET.domain;

import java.util.List;

public interface IGenreRepo {

    Genre addGenre(String genreName);

    List<Genre> getListOfOfficialGenres();
}
