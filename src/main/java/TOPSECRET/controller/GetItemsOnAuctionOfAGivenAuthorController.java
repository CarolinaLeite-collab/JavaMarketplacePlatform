package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.UserId;

import java.util.List;
import java.util.Objects;

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
        _iAuctionRepo = Objects.requireNonNull(iAuctionRepo, "auctionRepo");
        Objects.requireNonNull(buyerId, "buyer"); // buyer kept for parity/validation
    }

    public List<Item> getAuctionItemsByAuthor(Author author) {
        return _iAuctionRepo.getAuctionItemsByAuthor(author);
    }
}
