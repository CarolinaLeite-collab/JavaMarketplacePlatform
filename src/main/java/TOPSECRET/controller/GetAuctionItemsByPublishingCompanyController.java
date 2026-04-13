package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for retrieving auction items by a specific publishingCompany.
 * <p>
 * This controller uses the {@link IAuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link PublishingCompany}.
 * </p>
 */

public class GetAuctionItemsByPublishingCompanyController {

    private IAuctionRepo _iAuctionRepo;

    public GetAuctionItemsByPublishingCompanyController(IAuctionRepo iAuctionRepo, UserId buyerId) {

        _iAuctionRepo = iAuctionRepo;
    }
}