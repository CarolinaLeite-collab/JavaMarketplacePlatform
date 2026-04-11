package TOPSECRET.controller;

import TOPSECRET.domain.repository.IDirectSaleRepo;
import TOPSECRET.domain.directsale.DirectSale;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing items in a user's library for direct sale.
 */
public class PublicationInLibraryForDirectSaleController {

    private final ILibraryRepo _iLibraryRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;
    private Library _library;

    public PublicationInLibraryForDirectSaleController(ILibraryRepo libraryRepo, IDirectSaleRepo directSaleRepo, UserId userId) {
        _iLibraryRepo = libraryRepo;
        _iDirectSaleRepo = directSaleRepo;
        _library = libraryRepo.findLibraryByUserId(userId);
    }

    public List<Item> getItemsInLibraryByUser(UserId userId) {

        Library userLibrary = _iLibraryRepo.findLibraryByUserId(userId);

        List<Item> items = userLibrary.getItemsInLibrary();
        return List.copyOf(items);

    }

    public DirectSale addItemForDirectSale(List<Item> items, Price price, Period timeLimit) {

        List<Item> itemsForSale = new ArrayList<>();
        for (Item item : items) {
            Item itemFromLibrary = _library.getItem(item);
            if (itemFromLibrary != null) {
                itemsForSale.add(itemFromLibrary);
            }
        }
        DirectSale directSale = _iDirectSaleRepo.addDirectSale(itemsForSale, price, timeLimit);

        for (Item item : items){
            item.setDirectSale(directSale);
        }

        return directSale;
    }
}