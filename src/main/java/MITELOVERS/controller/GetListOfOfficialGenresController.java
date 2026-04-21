package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving the list of official genres.
 * <p>
 * This controller acts as an intermediary between the user interface
 * and the domain layer, delegating the retrieval of official genres
 * to the {@link IGenreRepo}.
 * </p>
 */

public class GetListOfOfficialGenresController {

    private final IGenreRepo _iGenreRepo;
    private final IListOfItemsRepo _iListOfItemsRepo;


    public GetListOfOfficialGenresController(IGenreRepo iGenreRepo, IListOfItemsRepo iListOfItemsRepo, UserId userId) {

        _iGenreRepo = iGenreRepo;
        _iListOfItemsRepo = iListOfItemsRepo;

    }

    public Iterable<GenreId> findAllKeys() {
        Iterable<GenreId> genreIds = _iGenreRepo.findAllKeys();

        return genreIds;
    }

    public List<ItemId> getPublicListsByGenreId(GenreId genreId) {
        Iterable<ListOfItems> allList = _iListOfItemsRepo.findAll();
        List<ItemId> publicListOfItemsByGenreId = new ArrayList<>();

        for(ListOfItems listOfItems: allList) {
            boolean publicList = !listOfItems.isPrivate();
            boolean sameGenre = listOfItems.getGenreId().equals(genreId);

            if (publicList && sameGenre) {
                publicListOfItemsByGenreId.addAll(listOfItems.getItemIds());
            }
        }

        return publicListOfItemsByGenreId;
    }
}
