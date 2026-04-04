package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems;
import TOPSECRET.domain.User.User;

import java.util.List;

/**
 * Controller responsible for retrieving public lists of publications filtered by genre.
 * <p>
 * This controller interacts with the {@link IListOfItemsRepo} to obtain
 * {@link ListOfItems} instances that are public and match a specific {@link Genre}.
 * </p>
 */

public class GetPublicListsByGenreController {
    private final IListOfItemsRepo _iListOfItemsRepo;

    public GetPublicListsByGenreController(IListOfItemsRepo iListOfPubRepo, User user) {
        _iListOfItemsRepo = iListOfPubRepo;
    }

    public List<ListOfItems> getPublicListsByGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre is mandatory");
        }
        return _iListOfItemsRepo.findPublicListsByGenre(genre);
    }
}