package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.valueobject.UserId;



/**
 * Controller responsible for retrieving items that are currently on direct sale
 * by a given author.
 * <p>
 * This controller acts as an application-layer entry point, delegating the
 * retrieval logic to the {@link IDirectSaleRepo}.
 */

public class GetDirectSaleItemsByAuthorController {

    private IDirectSaleRepo _iDirectSaleRepo;

    public GetDirectSaleItemsByAuthorController(IDirectSaleRepo dsr, UserId buyerId){

        _iDirectSaleRepo = dsr;

    }

}
