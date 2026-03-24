package TOPSECRET.controller;

import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.PublishingCompany;

import java.util.List;
import java.util.Objects;

/**
 * Controller that delegates direct sale item queries to the repository.
 */
public class GetItemsOnDirectSaleOfAGivenPublishingCompanyController {

    private final IDirectSaleRepo _iDirectSaleRepo;

    public GetItemsOnDirectSaleOfAGivenPublishingCompanyController(IDirectSaleRepo directSaleRepo) {

        _iDirectSaleRepo = Objects.requireNonNull(directSaleRepo, "directSaleRepo");
    }

    public List<Item> getDirectSaleItemByPublisher(PublishingCompany PublishingCompany) {

        if (PublishingCompany == null) {
            throw new IllegalArgumentException("publisher");
        }
        return _iDirectSaleRepo.getDirectSaleItemsByPublisher(PublishingCompany);
    }
}
