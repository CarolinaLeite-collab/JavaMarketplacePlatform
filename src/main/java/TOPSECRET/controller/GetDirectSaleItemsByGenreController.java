package TOPSECRET.controller;

import TOPSECRET.domain.DirectSaleRepo;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Item;

import java.util.List;

/**
 * Controller responsible for retrieving direct sale items filtered by genre.
 * <p>
 * This controller interacts with the {@link DirectSaleRepo} to fetch a list of
 * {@link Item} instances available in direct sales that match a specific {@link Genre}.
 * </p>
 */

public class GetDirectSaleItemsByGenreController {

    private DirectSaleRepo _directSaleRepo;

    public GetDirectSaleItemsByGenreController(DirectSaleRepo directSaleRepo) {
        _directSaleRepo = directSaleRepo;
    }

    public List<Item> getDirectSaleItemsByGenre(Genre genre) {
        return _directSaleRepo.getDirectSaleItemsByGenre(genre);
    }
}
