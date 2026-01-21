package TOPSECRET.controller;

import TOPSECRET.domain.PublicList;
import TOPSECRET.domain.PublicListRepo;

import java.util.List;

public class GetPublicListsByGenreController {

    private final PublicListRepo repo;

    public GetPublicListsByGenreController(PublicListRepo repo) {
        this.repo = repo;
    }

    public List<PublicList> getPublicListsByGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre is mandatory");
        }

        return repo.findPublicListsPublishedByGenre(genre.trim());
    }
}