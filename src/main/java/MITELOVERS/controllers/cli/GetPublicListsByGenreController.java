package MITELOVERS.controllers.cli;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for retrieving public lists of publications filtered by genre.
 * <p>
 * This controller interacts with {@link IGenreRepo} to retrieve the available genres,
 * and with {@link IListOfItemsRepo} to obtain {@link ListOfItems} instances
 * that are public and match a specific {@link Genre}.
 * </p>
 */

@Controller
public class GetPublicListsByGenreController {

    private final IGenreRepo _iGenreRepo;
    private final IListOfItemsRepo _iListOfItemsRepo;

    public GetPublicListsByGenreController(IGenreRepo iGenreRepo, IListOfItemsRepo iListOfItemsRepo) {

        _iGenreRepo = iGenreRepo;
        _iListOfItemsRepo = iListOfItemsRepo;

    }

    public Iterable<GenreId> findAllKeys() {

        return _iGenreRepo.findAllKeys();

    }

    public List<ListOfItems> getPublicListsByGenre(GenreId genreId) {

        if (genreId == null) {
            throw new IllegalArgumentException("Genre is mandatory");
        }
        return findPublicListsByGenre(genreId);
    }

    public List<ListOfItems> findPublicListsByGenre(GenreId genreId) {

        Iterable<ListOfItems> all = _iListOfItemsRepo.findAll();

        List<ListOfItems> result = new ArrayList<>();

        for (ListOfItems list : all) {
            if (!list.isPrivate() && genreId.equals(list.getGenreId())) {
                result.add(list);
            }
        }

        return result;
    }
}
