package TOPSECRET.controller;

import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.repository.*;
import TOPSECRET.domain.valueobject.*;
import TOPSECRET.persistence.mem.MemoAuthorRepo;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller responsible for retrieving items that are currently on direct sale
 * by a given author.
 * <p>
 * This controller acts as an application-layer entry point, delegating the
 * retrieval logic to the {@link IDirectSaleRepo}.
 */

public class GetDirectSaleItemsByAuthorController {

    private final IDirectSaleRepo _iDirectSaleRepo;
    private final IItemRepo _iItemRepo;
    private final IAuthorRepo _iAuthorRepo;
    public final IEditionRepo _iEditionRepo;
    public final IPublicationRepo _iPublicationRepo;

    public GetDirectSaleItemsByAuthorController(IAuthorRepo ar, IItemRepo ir, IEditionRepo er, IPublicationRepo pr, IDirectSaleRepo dsr, UserId buyerId){

        _iAuthorRepo = ar;
        _iItemRepo = ir;
        _iPublicationRepo = pr;
        _iEditionRepo = er;
        _iDirectSaleRepo = dsr;

    }

    public Iterable<AuthorId> findAllKeys(){
        Iterable<AuthorId> authorIds = _iAuthorRepo.findAllKeys();

        return authorIds;
    }

    public List<ItemId> getDirectSaleItemsByAuthorId(AuthorId authorId) {

        Iterable<DirectSale> directSales = _iDirectSaleRepo.findAll();
        List<ItemId> listOfItemsOnDirectSaleByAuthor = new ArrayList<>();

        for(DirectSale directSale: directSales){
            List<ItemId> itemIds = directSale.getItemsId();

            for(ItemId itemId: itemIds) {
                Item item = _iItemRepo.ofIdentity(itemId).orElseThrow(() -> new IllegalStateException("Item not found"));

                EditionId editionId = item.getEditionId();

                Edition edition = _iEditionRepo.ofIdentity(editionId).orElseThrow(() -> new IllegalStateException("Edition not found"));

                PublicationId publicationId = edition.getPublicationId();

                Publication publication = _iPublicationRepo.ofIdentity(publicationId).orElseThrow(() -> new IllegalStateException("Publication not found"));

                if (publication.isByAuthorId(authorId)) {
                    listOfItemsOnDirectSaleByAuthor.add(itemId);
                }
            }
        }
        return listOfItemsOnDirectSaleByAuthor;
    }

}
