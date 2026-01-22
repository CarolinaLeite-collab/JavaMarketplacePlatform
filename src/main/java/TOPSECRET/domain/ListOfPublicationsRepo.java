package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link ListOfPublications} instances.
 * <p>
 * This class handles the creation, storage, and retrieval of private lists of publications.
 * It ensures that duplicate lists (same user, name, and genre) are not allowed.
 * </p>
 */
public class ListOfPublicationsRepo {
    private final List<ListOfPublications> _listsOfListOfPublications;
    private final GenreRepo _genreRepo;

    public ListOfPublicationsRepo(GenreRepo genreRepo) {
        if (genreRepo == null) {
            throw new IllegalArgumentException("GenreRepo cannot be null");
        }
        _listsOfListOfPublications = new ArrayList<>();
        _genreRepo = genreRepo;
    }

    public ListOfPublications createListOfPublications(User user, String name, Genre genre){

        ListOfPublications newList = new ListOfPublications(user, name, genre);

        if (existsListOfPublications(newList))
            return null;
        _listsOfListOfPublications.add(newList);
        return newList;
    }

    private boolean existsListOfPublications(ListOfPublications listOfPublications){
        for (ListOfPublications lp1 : _listsOfListOfPublications) {
            if (lp1.equals(listOfPublications)) {
                return true;
                }
            }
        return false;
    }

    /**
     * Returns a list of all ListOfPublications, created for unit tests.
     */
    public List<ListOfPublications> getListOfListOfPublications(){
        return List.copyOf(_listsOfListOfPublications);
    }
}


