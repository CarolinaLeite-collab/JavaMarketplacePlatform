package MITELOVERS.controllers.cli;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.util.List;

/**
 * Controller responsible for managing items in a user's library for direct sale.
 */

@Controller
public class PublicationInLibraryForDirectSaleController {

    private final ILibraryRepo _iLibraryRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;
    private final IItemRepo _iItemRepo;
    private final DirectSaleFactory _directSaleFactory;

    public PublicationInLibraryForDirectSaleController(DirectSaleFactory directSaleFactory, ILibraryRepo libraryRepo, IDirectSaleRepo directSaleRepo, IItemRepo iItemRepo) {
        _iLibraryRepo = libraryRepo;
        _iDirectSaleRepo = directSaleRepo;
        _iItemRepo = iItemRepo;
        _directSaleFactory = directSaleFactory;

    }

    public List<ItemId> getItemsIdInLibraryByUserId(UserId userId) {

        LibraryId libraryID = LibraryId.fromUserId(userId);

        Library userLibrary = _iLibraryRepo.ofIdentity(libraryID)
                .orElseThrow(() -> new IllegalStateException("Library not found for user!"));

        List<ItemId> itemIds = userLibrary.getItemsIdInLibrary();
        return List.copyOf(itemIds);

    }

    public DirectSale putItemIdOnDirectSale (List<ItemId> itemIds, UserId sellerId, Price price, Duration timeLimit) {


        DirectSale directSale =  _directSaleFactory.createDirectSale(itemIds, sellerId, price, timeLimit);

        if (_iDirectSaleRepo.containsOfIdentity(directSale.identity())) {

            throw new IllegalStateException("Direct sale already exists!");

        }

        for (ItemId itemId : itemIds) {
            Item item = _iItemRepo.ofIdentity(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

            if (item.getSaleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException(itemId + " is already on sale!");
            }

            item.markAsDirectSale();
        }

        _iDirectSaleRepo.save(directSale);

        return directSale;
    }

}
