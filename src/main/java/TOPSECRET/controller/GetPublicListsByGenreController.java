package TOPSECRET.controller;

import TOPSECRET.domain.PublicList;
import TOPSECRET.domain.PublicListRepo;

import java.util.List;

public class GetPublicListsByGenreController {

    private final PublicListRepo _repo;

    public GetPublicListsByGenreController(PublicListRepo repo) {
        _repo = repo;
    }

    public List<PublicList> getPublicListsByGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre is mandatory");
        }

        return _repo.findPublicListsPublishedByGenre(genre.trim());
    }
}