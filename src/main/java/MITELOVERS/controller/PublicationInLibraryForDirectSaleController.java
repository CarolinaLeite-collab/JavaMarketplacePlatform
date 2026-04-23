package MITELOVERS.controller;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.directsale.DirectSaleFactory;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.Price;
import MITELOVERS.domain.valueobject.SaleStatus;
import MITELOVERS.domain.valueobject.UserId;

import java.time.Period;
import java.util.List;

/**
 * Controller responsible for managing items in a user's library for direct sale.
 */
public class PublicationInLibraryForDirectSaleController {

    private final ILibraryRepo _iLibraryRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;
    private final Library _library;
    private final IItemRepo _iItemRepo;
    private final DirectSaleFactory _directSaleFactory;

    public PublicationInLibraryForDirectSaleController(DirectSaleFactory directSaleFactory, ILibraryRepo libraryRepo, IDirectSaleRepo directSaleRepo, IItemRepo iItemRepo, UserId userId) {
        _iLibraryRepo = libraryRepo;
        _iDirectSaleRepo = directSaleRepo;
        _iItemRepo = iItemRepo;
        _library = libraryRepo.findLibraryByUserId(userId);
        _directSaleFactory = directSaleFactory;
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

        DirectSale directSale = addDirectSale(
                itemsId, price, timeLimit
        );

        for (ItemId itemId : itemsId) {

            Item item = _iItemRepo.ofIdentity(itemId).get();
            item.markAsDirectSale();
        }

        return directSale;
    }

    private DirectSale addDirectSale(List<ItemId> itemsId, Price price, Period timeLimit) {

        DirectSale directSale = _directSaleFactory.createDirectSale(itemsId, price, timeLimit);

        if (_iDirectSaleRepo.containsOfIdentity(directSale.identity())) {

            throw new IllegalStateException("Direct sale already exists!");

        }

        return _iDirectSaleRepo.save(directSale);
    }
}
