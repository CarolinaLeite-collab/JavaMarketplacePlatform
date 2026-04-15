package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for retrieving auction items filtered by genre.
 * <p>
 * This controller interacts with the {@link IAuctionRepo} to fetch a list of
 * {@link Item} instances available in auctions that match a specific {@link Genre}.
 * </p>
 */

public class GetAuctionItemsByGenreController {

    private IAuctionRepo _iAuctionRepo;

    public GetAuctionItemsByGenreController (IAuctionRepo iAuctionRepo, UserId buyerId){

        _iAuctionRepo = iAuctionRepo;
    }
}