package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

public class GetDirectSaleItemsByAuthorController {

    private DirectSaleRepo _dsr;

    public GetDirectSaleItemsByAuthorController(DirectSaleRepo dsr, User buyer){

        _dsr = dsr;

    }

    public List<Item> getDirectSaleItemsByAuthor (Author author) {

        List<Item> directSaleItemsByAuthor = _dsr.getDirectSaleItemsByAuthor(author);

        return directSaleItemsByAuthor;

    }

}
