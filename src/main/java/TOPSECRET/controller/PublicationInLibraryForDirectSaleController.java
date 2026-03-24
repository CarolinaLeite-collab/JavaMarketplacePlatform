package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.time.Period;
import java.util.List;

/**
 * Controller responsible for managing items in a user's library for direct sale.
 */
public class PublicationInLibraryForDirectSaleController {

    private final LibraryRepo _libraryRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;

    public PublicationInLibraryForDirectSaleController(LibraryRepo libraryRepo, IDirectSaleRepo directSaleRepo, User _userID) {
        _libraryRepo = libraryRepo;
        _iDirectSaleRepo = directSaleRepo;
    }

    public List<Item> getItemsInLibraryByUser(User user) {

        return _libraryRepo.getItemsInLibraryByUser(user);

    }

    public DirectSale addItemForDirectSale(Item item, Price price, Period timeLimit) {

        DirectSale directSale = _iDirectSaleRepo.addDirectSale(item, price, timeLimit);
        item.setDirectSale(directSale);

        return directSale;
    }
}