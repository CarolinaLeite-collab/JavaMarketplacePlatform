package TOPSECRET.controller;
import TOPSECRET.domain.AuctionRepo;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.User;

import java.util.List;

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