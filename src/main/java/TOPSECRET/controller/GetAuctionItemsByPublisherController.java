package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;
import java.util.Objects;


public class GetAuctionItemsByPublisherController {
    private User _buyer;
    private Publisher _publisher;
    private AuctionRepo _auctionRepo;

    public GetAuctionItemsByPublisherController(Publisher publisher, AuctionRepo auctionRepo, User buyer) {
        _publisher = publisher;
        _auctionRepo = auctionRepo;
        Objects.requireNonNull(buyer, "Buyer must not be null");
    }

    public List getAuctionItemsByPublisher (Publisher publisher) {
        List itemList = _auctionRepo.getAuctionItemsByPublisher(publisher);

        return itemList;
    }
}