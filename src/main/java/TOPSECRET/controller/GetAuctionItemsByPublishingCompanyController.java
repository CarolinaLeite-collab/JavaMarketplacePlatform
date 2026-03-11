package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;
import java.util.Objects;

/**
 * Controller responsible for retrieving auction items by a specific publishingCompany.
 * <p>
 * This controller uses the {@link AuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link PublishingCompany}.
 * </p>
 */

public class GetAuctionItemsByPublishingCompanyController {

    private AuctionRepo _auctionRepo;

    public GetAuctionItemsByPublishingCompanyController( AuctionRepo auctionRepo, User buyer) {

        _auctionRepo = auctionRepo;
    }

    public List<Item> getAuctionItemsByPublishingCompany(PublishingCompany publisher) {

        return _auctionRepo.getAuctionItemsByPublishingCompany(publisher);
    }
}