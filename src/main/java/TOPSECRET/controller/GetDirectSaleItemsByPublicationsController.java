package TOPSECRET.controller;

import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.user.User;

import java.util.List;

/**
 * Controller responsible for retrieving the list of items that are currently
 * on direct sale for a given publication.
 *
 * <p>
 * This controller belongs to the application layer and acts as an intermediary
 * between the user interface and the domain layer. It coordinates the request
 * to obtain direct sale items without containing business logic.
 * </p>
 *
 * <p>
 * The controller delegates the retrieval of data to {@link IDirectSaleRepo},
 * ensuring a clear separation of concerns and keeping the controller stateless.
 * </p>
 */


public class GetDirectSaleItemsByPublicationsController {
    private IDirectSaleRepo _iDirectSaleRepo;

    public GetDirectSaleItemsByPublicationsController(IDirectSaleRepo dsr, User buyer){
        _iDirectSaleRepo = dsr;
    }

    public List<Item> getDirectSaleItemsByPublication (Publication publication) {

        List<Item> directSaleItemsByPublication = _iDirectSaleRepo.getDirectSaleItemsByPublication(publication);

        return directSaleItemsByPublication;

    }
}
