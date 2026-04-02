package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.IListOfPublicationsRepo;
import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Controller responsible for retrieving public lists of publications filtered by genre.
 * <p>
 * This controller interacts with the {@link IListOfPublicationsRepo} to obtain
 * {@link ListOfPublications} instances that are public and match a specific {@link Genre}.
 * </p>
 */

public class GetPublicListsByGenreController {
    private final IListOfPublicationsRepo _iListOfPublicationsRepo;

    public GetPublicListsByGenreController(IListOfPublicationsRepo iListOfPubRepo, User user) {
        _iListOfPublicationsRepo = iListOfPubRepo;
    }

    public List<ListOfPublications> getPublicListsByGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre is mandatory");
        }
        return _iListOfPublicationsRepo.findPublicListsByGenre(genre);
    }
}