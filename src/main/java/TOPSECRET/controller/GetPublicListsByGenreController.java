package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.ListOfPublicationsRepo;

import java.util.List;

public class GetPublicListsByGenreController {

    private final ListOfPublicationsRepo _repo;

    public GetPublicListsByGenreController(ListOfPublicationsRepo repo) {
        _repo = repo;
    }

    public List<ListOfPublications> getPublicListsByGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre is mandatory");
        }
        return _repo.findPublicListsByGenre(genre);
    }
}