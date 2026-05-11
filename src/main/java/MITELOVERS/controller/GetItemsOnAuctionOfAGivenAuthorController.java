package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving auction items by a specific author.
 * <p>
 * This controller uses the {@link IAuctionRepo} to obtain a list of {@link Item}
 * instances that are currently on auction and were created by a given {@link Author}.
 * </p>
 */

@Controller
public class GetItemsOnAuctionOfAGivenAuthorController {


    private final IAuctionRepo _iAuctionRepo;
    private final IItemRepo _iItemRepo;
    private final IAuthorRepo _iAuthorRepo;
    private final IEditionRepo _iEditionRepo;
    private final IPublicationRepo _iPublicationRepo;

    public GetItemsOnAuctionOfAGivenAuthorController(IAuthorRepo ar,
                                                     IItemRepo ir,
                                                     IEditionRepo er,
                                                     IPublicationRepo pr,
                                                     IAuctionRepo auctionRepo){

        _iAuthorRepo = ar;
        _iItemRepo = ir;
        _iPublicationRepo = pr;
        _iEditionRepo = er;
        _iAuctionRepo = auctionRepo;

    }

    public Iterable<AuthorId> findAllKeys(){
        Iterable<AuthorId> authorIds = _iAuthorRepo.findAllKeys();

        return authorIds;
    }

    public List<ItemId> getAuctionItemsByAuthorId(AuthorId authorId) {

        Iterable<Auction> auctions = _iAuctionRepo.findAll();
        List<ItemId> listOfItemsOnAuctionByAuthor = new ArrayList<>();

        for(Auction auction: auctions){
            List<ItemId> itemIds = auction.getItemsId();

            for(ItemId itemId: itemIds) {
                Item item = _iItemRepo.ofIdentity(itemId).orElseThrow(() -> new IllegalStateException("Item not found"));

                EditionId editionId = item.getEditionId();

                Edition edition = _iEditionRepo.ofIdentity(editionId).orElseThrow(() -> new IllegalStateException("Edition not found"));

                PublicationId publicationId = edition.getPublicationId();

                Publication publication = _iPublicationRepo.ofIdentity(publicationId).orElseThrow(() -> new IllegalStateException("Publication not found"));

                if (publication.isByAuthorId(authorId)) {
                    listOfItemsOnAuctionByAuthor.add(itemId);
                }
            }
        }
        return listOfItemsOnAuctionByAuthor;
    }

}
