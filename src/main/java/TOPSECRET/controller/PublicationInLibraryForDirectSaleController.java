package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.Price;

import java.time.Period;
import java.util.List;

/**
 * Controller responsible for managing items in a user's library for direct sale.
 */
public class PublicationInLibraryForDirectSaleController {

    private final ILibraryRepo _iLibraryRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;

    public PublicationInLibraryForDirectSaleController(ILibraryRepo libraryRepo, IDirectSaleRepo directSaleRepo, User _userID) {
        _iLibraryRepo = libraryRepo;
        _iDirectSaleRepo = directSaleRepo;
    }

    public List<Item> getItemsInLibraryByUser(User user) {

        return _iLibraryRepo.getItemsInLibraryByUser(user);

    }

    public DirectSale addItemForDirectSale(Item item, Price price, Period timeLimit) {

        DirectSale directSale = _iDirectSaleRepo.addDirectSale(item, price, timeLimit);
        item.setDirectSale(directSale);

        return directSale;
    }
}