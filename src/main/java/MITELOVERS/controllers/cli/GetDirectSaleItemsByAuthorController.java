package MITELOVERS.controllers.cli;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublicationId;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller responsible for retrieving items that are currently on direct sale
 * by a given author.
 * <p>
 * This controller acts as an application-layer entry point, delegating the
 * retrieval logic to the {@link IDirectSaleRepo}.
 */
@Controller
public class GetDirectSaleItemsByAuthorController {

    private final IDirectSaleRepo _iDirectSaleRepo;
    private final IItemRepo _iItemRepo;
    private final IAuthorRepo _iAuthorRepo;
    private final IEditionRepo _iEditionRepo;
    private final IPublicationRepo _iPublicationRepo;

    public GetDirectSaleItemsByAuthorController(IAuthorRepo ar, IItemRepo ir, IEditionRepo er, IPublicationRepo pr, IDirectSaleRepo dsr){

        _iAuthorRepo = ar;
        _iItemRepo = ir;
        _iPublicationRepo = pr;
        _iEditionRepo = er;
        _iDirectSaleRepo = dsr;

    }

    public Iterable<AuthorId> findAllKeys(){

        return _iAuthorRepo.findAllKeys();
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

        List<String> ids = listOfItemsOnDirectSaleByAuthor.stream().map(ItemId::getValue).toList();

        List<Item> itemsOrderByDescription = _iItemRepo.findByIdInOrderByDescriptionAsc(ids);


        return listOfItemsOnDirectSaleByAuthor;
    }

    public List<Item> getDirectSaleItemsByAuthorIdSortedByDescription(AuthorId authorId) {
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

        List<String> ids = listOfItemsOnDirectSaleByAuthor.stream().map(ItemId::getValue).toList();


        return _iItemRepo.findByIdInOrderByDescriptionAsc(ids);
    }

}
