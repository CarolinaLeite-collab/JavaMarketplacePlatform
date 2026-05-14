package MITELOVERS.controller;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving auction items filtered by genre.
 * <p>
 * This controller interacts with the {@link IAuctionRepo} to fetch a list of
 * {@link ItemId} instances available in auctions that match a specific {@link GenreId}.
 * </p>
 */

@Controller
public class GetAuctionItemsByGenreController {

    private final IAuctionRepo _iAuctionRepo;
    private final IGenreRepo _iGenreRepo;
    private final IItemRepo _iItemRepo;
    private final IPublicationRepo _iPublicationRepo;
    private final IEditionRepo _iEditionRepo;
    public GetAuctionItemsByGenreController (IAuctionRepo iAuctionRepo, IItemRepo iItemRepo, IPublicationRepo iPublicationRepo,
                                             IEditionRepo iEditionRepo, IGenreRepo iGenreRepo){

        _iAuctionRepo = iAuctionRepo;
        _iItemRepo = iItemRepo;
        _iPublicationRepo = iPublicationRepo;
        _iEditionRepo = iEditionRepo;
        _iGenreRepo = iGenreRepo;
    }

    public Iterable<GenreId> findAllKeys() {
        Iterable<GenreId> genreIds = _iGenreRepo.findAllKeys();

        return genreIds;
    }
    public List<ItemId> getAuctionItemsByGenreId (GenreId genreId) {
        Iterable<Auction> auctions = _iAuctionRepo.findAll();
        List<ItemId> listOfItemsOnAuctionByGenre = new ArrayList<>();

        for(Auction auction: auctions){
            List<ItemId> itemIds = auction.getItemsId();

            for(ItemId itemId: itemIds) {
                Item item = _iItemRepo.ofIdentity(itemId).orElseThrow( () -> new IllegalStateException("Item not found"));

                EditionId editionId = item.getEditionId();

                Edition edition = _iEditionRepo.ofIdentity(editionId).orElseThrow( () -> new IllegalStateException("Edition not found"));

                PublicationId publicationId = edition.getPublicationId();

                Publication publication = _iPublicationRepo.ofIdentity(publicationId).orElseThrow( () -> new IllegalStateException("Publication not found"));;

                if (publication.isByGenreId(genreId)) {
                    listOfItemsOnAuctionByGenre.add(itemId);
                }
            }

        }

        return listOfItemsOnAuctionByGenre;

    }
}
