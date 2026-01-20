package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class GenreRepo {
    private final List<Genre> _genres;

    public GenreRepo() {
        _genres = new ArrayList<>();
    }

    public boolean existsGenre(String genreName) {
        Genre genre = new Genre(genreName);
        boolean exists = _genres.contains(genre);
        return exists;
    }

    public Genre create(String genreName) {
        if (existsGenre(genreName)) {
            return null;
        }

        Genre genre = new Genre(genreName);
        _genres.add(genre);
        return genre;
    }

}

