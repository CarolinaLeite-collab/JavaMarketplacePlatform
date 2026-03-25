package TOPSECRET.controller;

import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.valueobject.Author;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.MemoAuctionRepo;
import TOPSECRET.domain.User;

import java.util.List;
import java.util.Objects;

/**
 * Controller responsible for retrieving auction items by a specific author.
 * <p>
 * This controller uses the {@link MemoAuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link Author}.
 * </p>
 */

public class GetItemsOnAuctionOfAGivenAuthorController {

    private final IAuctionRepo _iAuctionRepo;

    /**
     * Ensures the controller has the repository it needs and validates the buyer reference.
     */
    public GetItemsOnAuctionOfAGivenAuthorController(MemoAuctionRepo iAuctionRepo, User buyer) {
        _iAuctionRepo = Objects.requireNonNull(iAuctionRepo, "auctionRepo");
        Objects.requireNonNull(buyer, "buyer"); // buyer kept for parity/validation
    }

    /**
     * Retrieves all active auction listings by a normalized author name.
     */
    public List<Item> getAuctionItemsByAuthor(Author author) {
        return _iAuctionRepo.getAuctionItemsByAuthor(author);
    }
}
