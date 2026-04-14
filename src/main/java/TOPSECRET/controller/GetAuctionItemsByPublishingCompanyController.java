package TOPSECRET.controller;

import TOPSECRET.domain.repository.IAuctionRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.valueobject.PublishingCompanyId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.List;

/**
 * Controller responsible for retrieving auction items by a specific publishingCompany.
 * <p>
 * This controller uses the {@link IAuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link PublishingCompanyId}.
 * </p>
 */

public class GetAuctionItemsByPublishingCompanyController {

    private IAuctionRepo _iAuctionRepo;

    public GetAuctionItemsByPublishingCompanyController(IAuctionRepo iAuctionRepo, UserId buyerId) {

        _iAuctionRepo = iAuctionRepo;
    }

    public List<Item> getAuctionItemsByPublishingCompany(PublishingCompanyId publishingCompanyId) {

        return _iAuctionRepo.getAuctionItemsByPublishingCompanyId(publishingCompanyId);
    }
}