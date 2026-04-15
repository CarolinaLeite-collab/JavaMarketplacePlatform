package TOPSECRET.controller;

import TOPSECRET.domain.author.Author;
import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for retrieving auction items by a specific author.
 * <p>
 * This controller uses the {@link IAuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link Author}.
 * </p>
 */

public class GetItemsOnAuctionOfAGivenAuthorController {

    private final IAuctionRepo _iAuctionRepo;

    public GetItemsOnAuctionOfAGivenAuthorController(IAuctionRepo iAuctionRepo, UserId buyerId) {
        _iAuctionRepo = iAuctionRepo;
    }
}
