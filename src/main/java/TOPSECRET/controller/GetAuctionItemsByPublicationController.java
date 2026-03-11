package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

public class GetAuctionItemsByPublicationController {
    private final AuctionRepo _ar;

    public GetAuctionItemsByPublicationController(AuctionRepo auctionRepo, User buyer){

        _ar = auctionRepo;
    }

    public List<Item> getAuctionItemsByPublication(Publication publication) {

        List<Item> auctionItemsByPublication = _ar.getAuctionItemsByPublication(publication);

        return auctionItemsByPublication;

    }


}
