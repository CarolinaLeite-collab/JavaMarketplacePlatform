package TOPSECRET.controller;

import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Controller responsible for retrieving auction items filtered by genre.
 * <p>
 * This controller interacts with the {@link IAuctionRepo} to fetch a list of
 * {@link Item} instances available in auctions that match a specific {@link Genre}.
 * </p>
 */

public class GetAuctionItemsByGenreController {

    private IAuctionRepo _iAuctionRepo;

    public GetAuctionItemsByGenreController (IAuctionRepo iAuctionRepo, User buyer){

        _iAuctionRepo = iAuctionRepo;
    }

    public List<Item> getAuctionItemsByGenre(Genre genre) {

        List<Item> auctionItemsByGenre = _iAuctionRepo.getAuctionItemsByGenre(genre);

        return auctionItemsByGenre;
   }
}