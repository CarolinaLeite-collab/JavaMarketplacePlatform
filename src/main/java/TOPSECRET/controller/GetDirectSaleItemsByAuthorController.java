package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.valueobject.Author;

import java.util.List;


/**
 * Controller responsible for retrieving items that are currently on direct sale
 * by a given author.
 * <p>
 * This controller acts as an application-layer entry point, delegating the
 * retrieval logic to the {@link IDirectSaleRepo}.
 */

public class GetDirectSaleItemsByAuthorController {

    private IDirectSaleRepo _iDirectSaleRepo;

    public GetDirectSaleItemsByAuthorController(IDirectSaleRepo dsr, User buyer){

        _iDirectSaleRepo = dsr;

    }

    public List<Item> getDirectSaleItemsByAuthor (Author authorName) {

        List<Item> directSaleItemsByAuthor = _iDirectSaleRepo.getDirectSaleItemsByAuthor(authorName);

        return directSaleItemsByAuthor;

    }

}
