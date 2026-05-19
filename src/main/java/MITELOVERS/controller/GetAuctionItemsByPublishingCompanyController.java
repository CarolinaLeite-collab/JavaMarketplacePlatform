package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublishingCompanyRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublishingCompanyId;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving auction items by a specific publishingCompany.
 * <p>
 * This controller uses the {@link IAuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link PublishingCompanyId}.
 * </p>
 */

@Controller
public class GetAuctionItemsByPublishingCompanyController {

    private final IAuctionRepo _iAuctionRepo;
    private final IItemRepo _iItemRepo;
    private final IPublishingCompanyRepo _iPublishingCompanyRepo;
    private final IEditionRepo _iEditionRepo;

    public GetAuctionItemsByPublishingCompanyController(IPublishingCompanyRepo pcr,
                                                        IItemRepo ir,
                                                        IEditionRepo er,
                                                        IAuctionRepo auctionRepo) {
        _iPublishingCompanyRepo = pcr;
        _iItemRepo = ir;
        _iEditionRepo = er;
        _iAuctionRepo = auctionRepo;
    }

    public Iterable<PublishingCompanyId> findAllKeys(){
        Iterable<PublishingCompanyId> publishingCompanyIds = _iPublishingCompanyRepo.findAllKeys();

        return publishingCompanyIds;
    }

    public List<ItemId> getAuctionItemsByPublishingCompany(PublishingCompanyId publishingCompanyId) {

        Iterable<Auction> auctions = _iAuctionRepo.findAll();
        List<ItemId> listOfItemsOnAuctionByPublishingCompany = new ArrayList<>();

        for(Auction auction: auctions){
            List<ItemId> itemIds = auction.getItemsId();

            for(ItemId itemId: itemIds) {
                Item item = _iItemRepo.ofIdentity(itemId).orElseThrow(() -> new IllegalStateException("Item not found"));

                EditionId editionId = item.getEditionId();

                Edition edition = _iEditionRepo.ofIdentity(editionId).orElseThrow(() -> new IllegalStateException("Edition not found"));

                if (edition.isByPublishingCompanyId(publishingCompanyId)) {
                    listOfItemsOnAuctionByPublishingCompany.add(itemId);
                }
            }
        }
        return listOfItemsOnAuctionByPublishingCompany;
    }
}
