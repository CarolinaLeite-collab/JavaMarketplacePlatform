package TOPSECRET.controller;

import TOPSECRET.domain.DirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.PublishingCompany;

import java.util.List;
import java.util.Objects;

/**
 * Controller that delegates direct sale item queries to the repository.
 */
public class GetItemsOnDirectSaleOfAGivenPublishingCompanyController {

    private final DirectSaleRepo _directSaleRepo;

    public GetItemsOnDirectSaleOfAGivenPublishingCompanyController(DirectSaleRepo directSaleRepo) {

        _directSaleRepo = Objects.requireNonNull(directSaleRepo, "directSaleRepo");
    }

    public List<Item> getDirectSaleItemByPublisher(PublishingCompany PublishingCompany) {

        if (PublishingCompany == null) {
            throw new IllegalArgumentException("publisher");
        }
        return _directSaleRepo.getDirectSaleItemsByPublisher(PublishingCompany);
    }
}
