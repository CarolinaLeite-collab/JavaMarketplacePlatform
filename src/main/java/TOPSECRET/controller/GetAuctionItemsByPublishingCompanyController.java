package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;
import java.util.Objects;


public class GetAuctionItemsByPublishingCompanyController {
    private User _buyer;
    private PublishingCompany _publisher;
    private AuctionRepo _auctionRepo;

    public GetAuctionItemsByPublishingCompanyController(PublishingCompany publisher, AuctionRepo auctionRepo, User buyer) {
        _publisher = publisher;
        _auctionRepo = auctionRepo;
        Objects.requireNonNull(buyer, "Buyer must not be null");
    }

    public List getAuctionItemsByPublisher (PublishingCompany publisher) {
        List itemList = _auctionRepo.getAuctionItemsByPublisher(publisher);

        return itemList;
    }
}