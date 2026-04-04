package TOPSECRET.controller;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.IDirectSaleRepo;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.Price;
import TOPSECRET.domain.valueobject.UserId;

import java.time.Period;
import java.util.List;

/**
 * Controller responsible for managing items in a user's library for direct sale.
 */
public class PublicationInLibraryForDirectSaleController {

    private final ILibraryRepo _iLibraryRepo;
    private final IDirectSaleRepo _iDirectSaleRepo;

    public PublicationInLibraryForDirectSaleController(ILibraryRepo libraryRepo, IDirectSaleRepo directSaleRepo, UserId _userId) {
        _iLibraryRepo = libraryRepo;
        _iDirectSaleRepo = directSaleRepo;
    }

    public List<Item> getItemsInLibraryByUser(UserId userId) {

        return _iLibraryRepo.getItemsInLibraryByUserId(userId);

    }

    public DirectSale addItemForDirectSale(Item item, Price price, Period timeLimit) {

        DirectSale directSale = _iDirectSaleRepo.addDirectSale(item, price, timeLimit);
        item.setDirectSale(directSale);

        return directSale;
    }
}