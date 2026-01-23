package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

public class GetDirectSaleItemsByPublicationsController {
    private DirectSaleRepo _dsr;

    public GetDirectSaleItemsByPublicationsController(DirectSaleRepo dsr, User buyer){
        _dsr = dsr;
    }

    public List<Item> getDirectSaleItemsByPublication (Publication publication) {

        List<Item> directSaleItemsByPublication = _dsr.getDirectSaleItemsByPublication(publication);

        return directSaleItemsByPublication;

    }
}
