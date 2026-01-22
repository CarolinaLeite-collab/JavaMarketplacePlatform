package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.ListOfPublicationsRepo;

import java.util.List;

/**
 * Controller responsible for retrieving public lists of publications filtered by genre.
 * <p>
 * This controller interacts with the {@link ListOfPublicationsRepo} to obtain
 * {@link ListOfPublications} instances that are public and match a specific {@link Genre}.
 * </p>
 */

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