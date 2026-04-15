package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.valueobject.UserId;


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

    public GetDirectSaleItemsByPublicationsController(IDirectSaleRepo dsr, UserId buyerId){
        _iDirectSaleRepo = dsr;
    }

}
