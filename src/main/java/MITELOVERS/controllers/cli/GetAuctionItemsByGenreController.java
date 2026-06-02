package MITELOVERS.controllers.cli;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.*;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving auction items filtered by genre.
 * <p>
 * This controller interacts with the {@link IAuctionRepo} to fetch a list of
 * {@link ItemId} instances available in auctions that match a specific {@link GenreId}.
 * </p>
 * It also sorts the retrieved {@link ItemId} ascendingly by auction deadline.
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
        return _iGenreRepo.findAllKeys();
    }

    public List<ItemId> getAuctionItemsByGenreId(GenreId genreId) {

        // Items of a given genre
        List<ItemId> itemIdsOfGenre = new ArrayList<>();

        for (ItemId itemId : _iItemRepo.findAllKeys()) {
            Item item = _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalStateException("Item not found"));

            Edition edition = _iEditionRepo.ofIdentity(item.getEditionId())
                    .orElseThrow(() -> new IllegalStateException("Edition not found"));

            Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                    .orElseThrow(() -> new IllegalStateException("Publication not found"));

            if (publication.isByGenreId(genreId)) {
                itemIdsOfGenre.add(itemId);
            }
        }

        if (itemIdsOfGenre.isEmpty()) {
            return List.of();
        }

        // Look for auctions that contain these items, ordered by deadline
        return _iAuctionRepo.findByItemsIdSorted(itemIdsOfGenre);

    }
}
