package TOPSECRET.controller;

import TOPSECRET.domain.*;
import java.time.Period;
import java.util.List;

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
