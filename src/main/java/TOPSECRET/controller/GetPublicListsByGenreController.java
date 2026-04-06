package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.UserId;

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

    public GetPublicListsByGenreController(IListOfItemsRepo iListOfPubRepo, UserId userId) {
        _iListOfItemsRepo = iListOfPubRepo;
    }

    public List<ListOfItems> getPublicListsByGenre(GenreId genreId) {
        if (genreId == null) {
            throw new IllegalArgumentException("Genre is mandatory");
        }
        return _iListOfItemsRepo.findPublicListsByGenre(genreId);
    }
}