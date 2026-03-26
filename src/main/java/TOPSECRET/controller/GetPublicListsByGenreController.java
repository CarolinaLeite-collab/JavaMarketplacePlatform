package TOPSECRET.controller;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.IListOfPublicationsRepo;

import java.util.List;

/**
 * Controller responsible for retrieving public lists of publications filtered by genre.
 * <p>
 * This controller interacts with the {@link IListOfPublicationsRepo} to obtain
 * {@link ListOfPublications} instances that are public and match a specific {@link Genre}.
 * </p>
 */

public class GetPublicListsByGenreController {
    private final IListOfPublicationsRepo _iListOfPubRepo;

    public GetPublicListsByGenreController(IListOfPublicationsRepo iListOfPubRepo) {
        _iListOfPubRepo = _iListOfPubRepo;
    }

    public List<ListOfPublications> getPublicListsByGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre is mandatory");
        }
        return _iListOfPubRepo.findPublicListsByGenre(genre);
    }
}