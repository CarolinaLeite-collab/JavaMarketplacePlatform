package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.AuctionRepo;
import TOPSECRET.domain.User;

import java.util.List;
import java.util.Objects;

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

