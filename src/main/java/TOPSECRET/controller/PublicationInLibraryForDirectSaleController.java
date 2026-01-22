package TOPSECRET.controller;

import TOPSECRET.domain.*;
import java.time.Period;
import java.util.List;

/**
 * Controller responsible for managing publications in a user's library for direct sale.
 * <p>
 * This controller interacts with {@link LibraryRepo}, {@link PublicationRepo}, {@link ItemRepo},
 * and {@link DirectSaleRepo} to retrieve publications from a user's library and to add
 * selected publications for direct sale with specific conditions, price, and time limits.
 * All created {@link DirectSale} are stored in {@link DirectSaleRepo}.
 * </p>
 */

public class PublicationInLibraryForDirectSaleController {

    private final LibraryRepo libraryRepo;
    private final PublicationRepo publicationRepo;
    private final ItemRepo itemRepo;
    private final DirectSaleRepo directSaleRepo;

    public PublicationInLibraryForDirectSaleController(
            LibraryRepo libraryRepo,
            PublicationRepo publicationRepo,
            ItemRepo itemRepo,
            DirectSaleRepo directSaleRepo
    ) {
        this.libraryRepo = libraryRepo;
        this.publicationRepo = publicationRepo;
        this.itemRepo = itemRepo;
        this.directSaleRepo = directSaleRepo;
    }

    public List<PublicationDetails> getPublicationsList(User user) {

        Library userLibrary = libraryRepo.findByUser(user);

        return userLibrary.getPublicationsInLibrary();
    }

    public boolean addPublicationForDirectSale(
            Publication publication,
            Condition condition,
            Price price,
            Period timeLimit
    ) {
        Publication pub = publicationRepo.getPublication(publication);
        Item item = itemRepo.createItem(pub, condition);
        DirectSale directSale = directSaleRepo.createDirectSale(item, price, timeLimit);

        item.setDirectSale(directSale);
        return true;
    }
}
