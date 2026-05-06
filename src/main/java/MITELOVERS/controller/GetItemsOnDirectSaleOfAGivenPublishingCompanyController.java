package MITELOVERS.controller;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publishingcompany.PublishingCompany;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller responsible for retrieving direct sale items by a specific publishing company.
 * <p>
 * This controller uses the {@link IDirectSaleRepo} to obtain a list of {@link Item}
 * instances that are currently on direct sale and were published by a given {@link PublishingCompany}.
 * </p>
 */

@Controller
public class GetItemsOnDirectSaleOfAGivenPublishingCompanyController {

    private final IDirectSaleRepo _iDirectSaleRepo;
    private final IItemRepo _iItemRepo;
    private final IPublishingCompanyRepo _iPublishingCompanyRepo;
    private final IEditionRepo _iEditionRepo;

    public GetItemsOnDirectSaleOfAGivenPublishingCompanyController(IPublishingCompanyRepo pcr, IItemRepo ir, IEditionRepo er, IDirectSaleRepo directSaleRepo) {
        _iPublishingCompanyRepo = pcr;
        _iItemRepo = ir;
        _iEditionRepo = er;
        _iDirectSaleRepo = directSaleRepo;
    }

    public Iterable<PublishingCompanyId> findAllKeys(){
        Iterable<PublishingCompanyId> publishingCompanyIds = _iPublishingCompanyRepo.findAllKeys();

        return publishingCompanyIds;
    }

    public List<ItemId> getDirectSaleItemsByPublishingCompany(PublishingCompanyId publishingCompanyId) {

        Iterable<DirectSale> directSales = _iDirectSaleRepo.findAll();
        List<ItemId> listOfItemsOnDirectSaleByPublishingCompany = new ArrayList<>();

        for(DirectSale directSale: directSales){
            List<ItemId> itemIds = directSale.getItemsId();

            for(ItemId itemId: itemIds) {
                Item item = _iItemRepo.ofIdentity(itemId).orElseThrow(() -> new IllegalStateException("Item not found"));

                EditionId editionId = item.getEditionId();

                Edition edition = _iEditionRepo.ofIdentity(editionId).orElseThrow(() -> new IllegalStateException("Edition not found"));

                if (edition.isByPublishingCompanyId(publishingCompanyId)) {
                    listOfItemsOnDirectSaleByPublishingCompany.add(itemId);
                }
            }
        }
        return listOfItemsOnDirectSaleByPublishingCompany;
    }
}
