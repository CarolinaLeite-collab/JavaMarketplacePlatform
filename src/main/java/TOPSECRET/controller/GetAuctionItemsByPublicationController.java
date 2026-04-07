package TOPSECRET.controller;

import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.User.User;

import java.util.List;

/**
 * Controller responsible for retrieving auction items filtered by publication.
 *
 * <p>Acts as a thin delegation layer between the UI and {@link IAuctionRepo},
 * following the Controller pattern (GRASP).</p>
 */

public class GetAuctionItemsByPublicationController {
    private final IAuctionRepo _iAuctionRepo;

    public GetAuctionItemsByPublicationController(IAuctionRepo iAuctionRepo, User buyer){

        _iAuctionRepo = iAuctionRepo;
    }

    public List<Item> getAuctionItemsByPublication(Publication publication) {

        List<Item> auctionItemsByPublication = _iAuctionRepo.getAuctionItemsByPublication(publication);

        return auctionItemsByPublication;

    }


}
