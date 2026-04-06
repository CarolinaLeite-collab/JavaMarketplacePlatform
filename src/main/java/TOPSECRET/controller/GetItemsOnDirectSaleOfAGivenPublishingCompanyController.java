package TOPSECRET.controller;

import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.PublishingCompany.PublishingCompany;
import TOPSECRET.domain.valueobject.UserId;

import java.util.List;
import java.util.Objects;

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

        _iDirectSaleRepo = Objects.requireNonNull(directSaleRepo, "directSaleRepo");
    }

    public List<Item> getDirectSaleItemByPublisher(PublishingCompany PublishingCompany) {

        if (PublishingCompany == null) {
            throw new IllegalArgumentException("publisher");
        }
        return _iDirectSaleRepo.getDirectSaleItemsByPublisher(PublishingCompany);
    }
}
