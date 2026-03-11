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

    /**
     * @param directSaleRepo repository holding direct sale items
     */
    public GetItemsOnDirectSaleOfAGivenPublishingCompanyController(DirectSaleRepo directSaleRepo) {

        _directSaleRepo = Objects.requireNonNull(directSaleRepo, "directSaleRepo");
    }

    /**
     * Delegates the retrieval of direct sale items for a publisher to the repository.
     */
    public List<Item> getDirectSaleItemByPublisher(PublishingCompany publisher) {

        if (publisher == null) {
            throw new IllegalArgumentException("publisher");
        }
        return _directSaleRepo.getDirectSaleItemByPublisher(publisher);
    }
}
