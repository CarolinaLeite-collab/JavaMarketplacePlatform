package TOPSECRET.controller;

import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.valueobject.AuthorId;

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

    public GetItemsOnAuctionOfAGivenAuthorController(IAuctionRepo iAuctionRepo, User buyer) {
        _iAuctionRepo = Objects.requireNonNull(iAuctionRepo, "auctionRepo");
        Objects.requireNonNull(buyer, "buyer"); // buyer kept for parity/validation
    }

    public List<Item> getAuctionItemsByAuthor(AuthorId authorId) {
        return _iAuctionRepo.getAuctionItemsByAuthor(authorId);
    }
}
