package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IAuctionRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.PublicationId;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving auction items filtered by publication.
 *
 * <p>Acts as a thin delegation layer between the UI and {@link IAuctionRepo},
 * following the Controller pattern (GRASP).</p>
 */

@Controller
public class GetAuctionItemsByPublicationController {
    private final IAuctionRepo _iAuctionRepo;
    private final IPublicationRepo _iPublicationRepo;
    private final IItemRepo _iItemRepo;
    private final IEditionRepo _iEditionRepo;

    public GetAuctionItemsByPublicationController(IAuctionRepo iAuctionRepo, IPublicationRepo iPublicationRepo,
                                                  IItemRepo iItemRepo, IEditionRepo iEditionRepo){

        _iAuctionRepo = iAuctionRepo;
        _iPublicationRepo = iPublicationRepo;
        _iItemRepo = iItemRepo;
        _iEditionRepo = iEditionRepo;
    }

    public Iterable<PublicationId> findAllKeys() {
        Iterable<PublicationId> publicationIds = _iPublicationRepo.findAllKeys();

        return publicationIds;
    }
    public List<ItemId> getAuctionItemsByPublicationId (PublicationId publicationId) {
        Iterable<Auction> auctions = _iAuctionRepo.findAll();
        List<ItemId> listOfItemsOnAuctionByPublication = new ArrayList<>();

        for(Auction auction: auctions){
            List<ItemId> itemIds = auction.getItemsId();

            for(ItemId itemId: itemIds) {
                Item item = _iItemRepo.ofIdentity(itemId).orElseThrow( () -> new IllegalStateException("Item not found"));

                EditionId editionId = item.getEditionId();

                Edition edition = _iEditionRepo.ofIdentity(editionId).orElseThrow( () -> new IllegalStateException("Edition not found"));

                if (edition.isByPublicationId(publicationId)) {
                    listOfItemsOnAuctionByPublication.add(itemId);
                }
            }
        }
        return listOfItemsOnAuctionByPublication;
    }
}
