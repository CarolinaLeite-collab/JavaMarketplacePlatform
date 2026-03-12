package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

/**
 * Controller responsible for retrieving auction items filtered by publication.
 *
 * <p>Acts as a thin delegation layer between the UI and {@link AuctionRepo},
 * following the Controller pattern (GRASP).</p>
 */

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
