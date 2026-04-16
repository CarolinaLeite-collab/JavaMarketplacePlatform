package TOPSECRET.controller;

import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.SaleStatus;
import TOPSECRET.domain.valueobject.UserId;

import java.time.Period;
import java.util.List;

/**
 * Controller responsible for managing items in a user's library for direct sale.
 */
public class PublicationInLibraryForDirectSaleController {

    private final ILibraryRepo _iLibraryRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;
    private Library _library;
    private final IItemRepo _iItemRepo;

    public PublicationInLibraryForDirectSaleController(ILibraryRepo libraryRepo, IDirectSaleRepo directSaleRepo, IItemRepo iItemRepo, UserId userId) {
        _iLibraryRepo = libraryRepo;
        _iDirectSaleRepo = directSaleRepo;
        _iItemRepo = iItemRepo;
        _library = libraryRepo.findLibraryByUserId(userId);
    }

    public List<ItemId> getItemsInLibraryByUser(UserId userId) {

        Library userLibrary = _iLibraryRepo.findLibraryByUserId(userId);

        List<ItemId> itemIds = userLibrary.getItemsIdInLibrary();
        return List.copyOf(itemIds);

    }

    public DirectSale putItemIdOnDirectSale (List<ItemId> itemsId, Price price, Period timeLimit) {

        for (ItemId itemId : itemsId) {

            Item item = _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

            if (item.getSaleStatus() != SaleStatus.NotOnSale) {
                throw new IllegalStateException(itemId + " is already on sale!");
            }
        }

        DirectSale directSale = _iDirectSaleRepo.addDirectSale(
                itemsId, price, timeLimit
        );

        for (ItemId itemId : itemsId) {

            Item item = _iItemRepo.ofIdentity(itemId).get();
            item.markAsDirectSale();
        }

        return directSale;
    }
}
