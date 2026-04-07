package TOPSECRET.controller;

import TOPSECRET.domain.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.user.User;

import java.util.List;

/**
 * Controller responsible for retrieving auction items by a specific publishingCompany.
 * <p>
 * This controller uses the {@link IAuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link PublishingCompany}.
 * </p>
 */

public class GetAuctionItemsByPublishingCompanyController {

    private IAuctionRepo _iAuctionRepo;

    public GetAuctionItemsByPublishingCompanyController(IAuctionRepo iAuctionRepo, User buyer) {

        _iAuctionRepo = iAuctionRepo;
    }

    public List<Item> getAuctionItemsByPublishingCompany(PublishingCompany publisher) {

        return _iAuctionRepo.getAuctionItemsByPublishingCompany(publisher);
    }
}