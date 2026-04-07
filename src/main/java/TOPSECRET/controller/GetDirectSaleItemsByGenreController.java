package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.GenreId;

import java.util.List;

/**
 * Controller responsible for retrieving direct sale items filtered by genre.
 * <p>
 * This controller interacts with the {@link IDirectSaleRepo} to fetch a list of
 * {@link Item} instances available in direct sales that match a specific {@link Genre}.
 * </p>
 */

public class GetDirectSaleItemsByGenreController {

    private final IDirectSaleRepo _iDirectSaleRepo;

    public GetDirectSaleItemsByGenreController(IDirectSaleRepo directSaleRepo, User buyer) {

        _iDirectSaleRepo = directSaleRepo;

    }

    public List<Item> getDirectSaleItemsByGenre(GenreId genreId) {

        List<Item> directSaleItemsByGenre = _iDirectSaleRepo.getDirectSaleItemsByGenre(genreId);

        return directSaleItemsByGenre;

    }

}
