package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.time.Period;
import java.util.List;

/**
 * Controller responsible for managing items in a user's library for direct sale.
 */
public class PublicationInLibraryForDirectSaleController {

    private final LibraryRepo libraryRepo;
    private final DirectSaleRepo directSaleRepo;

    public PublicationInLibraryForDirectSaleController(
            LibraryRepo libraryRepo,
            DirectSaleRepo directSaleRepo
    ) {
        this.libraryRepo = libraryRepo;
        this.directSaleRepo = directSaleRepo;
    }

    public List<Item> getItemsInLibrary(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User required");
        }

        Library userLibrary = libraryRepo.findLibraryByUser(user);
        return userLibrary.getItemsInLibrary();
    }

    public DirectSale addItemForDirectSale(
            Item item,
            Price price,
            Period timeLimit
    ) {
        if (item == null) {
            throw new IllegalArgumentException("Item required");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price required");
        }

        DirectSale directSale = directSaleRepo.createDirectSale(item, price, timeLimit);
        item.setDirectSale(directSale);

        return directSale;
    }
}