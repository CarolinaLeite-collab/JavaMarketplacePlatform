package MITELOVERS.controller;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
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
    private final IItemRepo _iItemRepo;
    private final IPublicationRepo _iPublicationRepo;
    private final IEditionRepo  _iEditionRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;

    public GetDirectSaleItemsByPublicationsController(IPublicationRepo pr, IItemRepo ir, IEditionRepo er, IDirectSaleRepo dsr, UserId buyerId){
        _iPublicationRepo = pr;
        _iItemRepo = ir;
        _iEditionRepo = er;
        _iDirectSaleRepo = dsr;
    }

    public Iterable<PublicationId> findAllKeys(){
        Iterable<PublicationId> publicationIds = _iPublicationRepo.findAllKeys();

        return publicationIds;
    }

    public List<ItemId> getDirectSaleItemsByPublication(PublicationId publicationId) {

        Iterable<DirectSale> directSales = _iDirectSaleRepo.findAll();
        List<ItemId> listOfItemsOnDirectSaleByPublication = new ArrayList<>();

        for(DirectSale directSale: directSales){
            List<ItemId> itemIds = directSale.getItemsId();

            for(ItemId itemId: itemIds) {
                Item item = _iItemRepo.ofIdentity(itemId).orElseThrow(() -> new IllegalStateException("Item not found"));

                EditionId editionId = item.getEditionId();

                Edition edition = _iEditionRepo.ofIdentity(editionId).orElseThrow(() -> new IllegalStateException("Edition not found"));

                if (edition.isByPublicationId(publicationId)) {
                    listOfItemsOnDirectSaleByPublication.add(itemId);
                }
            }
        }
        return listOfItemsOnDirectSaleByPublication;
    }

}
