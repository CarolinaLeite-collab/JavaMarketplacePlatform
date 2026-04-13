package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.publishingcompany.PublishingCompany;
import TOPSECRET.domain.valueobject.UserId;


/**
 * Controller responsible for retrieving direct sale items by a specific publishing company.
 * <p>
 * This controller uses the {@link IDirectSaleRepo} to obtain a list of {@link Item}
 * instances that are currently on direct sale and were published by a given {@link PublishingCompany}.
 * </p>
 */

public class GetItemsOnDirectSaleOfAGivenPublishingCompanyController {

    private final IDirectSaleRepo _iDirectSaleRepo;

    public GetItemsOnDirectSaleOfAGivenPublishingCompanyController(IDirectSaleRepo directSaleRepo, UserId buyerId) {

        _iDirectSaleRepo = directSaleRepo;
    }
}
