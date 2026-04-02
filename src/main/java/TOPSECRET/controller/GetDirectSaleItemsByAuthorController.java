package TOPSECRET.controller;

import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.User;
import TOPSECRET.domain.Author.Author;

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
