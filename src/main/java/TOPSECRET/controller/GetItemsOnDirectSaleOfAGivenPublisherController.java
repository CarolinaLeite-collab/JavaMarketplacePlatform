package TOPSECRET.controller;

import TOPSECRET.domain.DirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.User;

import java.util.List;

public class GetItemsOnDirectSaleOfAGivenPublisherController {

    private DirectSaleRepo _dsr;

    public GetItemsOnDirectSaleOfAGivenPublisherController(DirectSaleRepo dsr, User buyer) {

        _dsr = dsr;
    }

    public List<Item> getDirectSaleItemByPublisher(Publisher publisher) {

        List<Item> directSaleItemByPublisher = _dsr.getDirectSaleItemByPublisher(publisher);

        return directSaleItemByPublisher;
    }
}
