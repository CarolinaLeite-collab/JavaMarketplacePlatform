package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.UserId;


/**
 * Controller responsible for retrieving direct sale items filtered by genre.
 * <p>
 * This controller interacts with the {@link IDirectSaleRepo} to fetch a list of
 * {@link Item} instances available in direct sales that match a specific {@link Genre}.
 * </p>
 */

public class GetDirectSaleItemsByGenreController {

    private final IDirectSaleRepo _iDirectSaleRepo;

    public GetDirectSaleItemsByGenreController(IDirectSaleRepo directSaleRepo, UserId buyerId) {

        _iDirectSaleRepo = directSaleRepo;

    }
}
