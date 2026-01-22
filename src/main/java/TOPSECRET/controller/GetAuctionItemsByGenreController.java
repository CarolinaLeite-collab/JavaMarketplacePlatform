package TOPSECRET.controller;
import TOPSECRET.domain.AuctionRepo;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Controller responsible for retrieving auction items filtered by genre.
 * <p>
 * This controller interacts with the {@link AuctionRepo} to fetch a list of
 * {@link Item} instances available in auctions that match a specific {@link Genre}.
 * </p>
 */

public class GetAuctionItemsByGenreController {

    private AuctionRepo _auctionRepo;

    public GetAuctionItemsByGenreController (AuctionRepo auctionRepo, User buyer){

        _auctionRepo = auctionRepo;
    }

    public List<Item> getAuctionItemsByGenre(Genre genre) {

        List<Item> auctionItemsByGenre = _auctionRepo.getAuctionItemsByGenre(genre);

        return auctionItemsByGenre;
   }
}