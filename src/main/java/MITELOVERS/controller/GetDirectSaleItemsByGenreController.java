package MITELOVERS.controller;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IPublicationRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving items available in direct sales filtered by genre.
 * <p>
 * This controller coordinates multiple repositories to traverse the domain model:
 * {@link IDirectSaleRepo}, {@link IItemRepo}, {@link IEditionRepo}, and {@link IPublicationRepo}
 * in order to determine which items in direct sales belong to publications of a given genre.
 * </p>
 *
 * <p>
 * The filtering process follows the domain hierarchy:
 * Item → Edition → Publication → Genre.
 * </p>
 */

public class GetDirectSaleItemsByGenreController {

    private final IDirectSaleRepo _iDirectSaleRepo;
    private final IItemRepo _iItemRepo;
    private final IEditionRepo _iEditionRepo;
    private final IPublicationRepo _iPublicationRepo;

    public GetDirectSaleItemsByGenreController(IDirectSaleRepo iDirectSaleRepo,
                                               IItemRepo iItemRepo,
                                               IEditionRepo iEditionRepo,
                                               IPublicationRepo iPublicationRepo,
                                               UserId buyerId) {

        _iDirectSaleRepo = iDirectSaleRepo;
        _iItemRepo = iItemRepo;
        _iEditionRepo = iEditionRepo;
        _iPublicationRepo = iPublicationRepo;

    }

    public List<ItemId> getDirectSaleItemsByGenre(GenreId genreId) {

        Iterable<DirectSale> directSales = _iDirectSaleRepo.findAll();
        List<ItemId> directSaleItemsByGenre = new ArrayList<>();

        for (DirectSale directSale : directSales) {

            List<ItemId> ItemIdsInDS = directSale.getItemsId();

            for (ItemId itemId : ItemIdsInDS) {

                Item item = _iItemRepo.ofIdentity(itemId)
                        .orElseThrow(() -> new IllegalStateException("Item not found!"));

                Edition edition = _iEditionRepo.ofIdentity(item.getEditionId())
                        .orElseThrow(() -> new IllegalStateException("Edition not found!"));

                Publication publication = _iPublicationRepo.ofIdentity(edition.getPublicationId())
                        .orElseThrow(() -> new IllegalStateException("Publication not found!"));;

                if (publication.isByGenreId(genreId)) {
                    directSaleItemsByGenre.add(itemId);
                }

            }
        }

        return directSaleItemsByGenre;

    }

}
