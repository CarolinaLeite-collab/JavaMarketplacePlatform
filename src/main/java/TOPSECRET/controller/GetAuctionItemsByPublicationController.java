package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for retrieving auction items filtered by publication.
 *
 * <p>Acts as a thin delegation layer between the UI and {@link IAuctionRepo},
 * following the Controller pattern (GRASP).</p>
 */

public class GetAuctionItemsByPublicationController {
    private final IAuctionRepo _iAuctionRepo;

    public GetAuctionItemsByPublicationController(IAuctionRepo iAuctionRepo, UserId buyerId){

        _iAuctionRepo = iAuctionRepo;
    }
}
