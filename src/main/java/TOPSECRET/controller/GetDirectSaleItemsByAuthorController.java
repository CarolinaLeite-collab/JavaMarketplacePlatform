package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;


/**
 * Controller responsible for retrieving items that are currently on direct sale
 * by a given author.
 * <p>
 * This controller acts as an application-layer entry point, delegating the
 * retrieval logic to the {@link DirectSaleRepo}.
 */

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
