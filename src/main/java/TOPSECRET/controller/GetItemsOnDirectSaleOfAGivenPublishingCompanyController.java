package TOPSECRET.controller;

import TOPSECRET.domain.DirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.PublishingCompany;
import TOPSECRET.domain.User;

import java.util.List;

public class GetItemsOnDirectSaleOfAGivenPublishingCompanyController {

    private DirectSaleRepo _dsr;

    public GetItemsOnDirectSaleOfAGivenPublishingCompanyController(DirectSaleRepo dsr, User buyer) {

        _dsr = dsr;
    }

    public List<Item> getDirectSaleItemByPublisher(PublishingCompany publisher) {

        List<Item> directSaleItemByPublisher = _dsr.getDirectSaleItemByPublisher(publisher);

        return directSaleItemByPublisher;
    }
}
