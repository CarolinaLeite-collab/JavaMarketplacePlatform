package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.AuctionRepo;
import TOPSECRET.domain.User;

import java.util.List;
import java.util.Objects;

/**
 * Controller responsible for retrieving auction items by a specific author.
 * <p>
 * This controller uses the {@link AuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link Author}.
 * </p>
 */

public class GetItemsOnAuctionOfAGivenAuthorController {

    private final AuctionRepo _auctionRepo;

    public GetItemsOnAuctionOfAGivenAuthorController(AuctionRepo auctionRepo, User buyer) {
        _auctionRepo = Objects.requireNonNull(auctionRepo, "auctionRepo");
        Objects.requireNonNull(buyer, "buyer"); // buyer kept for parity/validation
    }

    public List<Item> getAuctionItemsByAuthor(Author author) {
        return _auctionRepo.getAuctionItemsByAuthor(author);
    }
}

