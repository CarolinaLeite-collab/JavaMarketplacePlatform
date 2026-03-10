package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link Library} entities.
 * <p>
 * This class provides management mechanisms for
 * {@link Library} objects.
 * <p>
 * It encapsulates all data access
 * operations related to libraries and isolates the domain and controller
 * layers from persistence concerns.
 * </p>
 */

public class LibraryRepo {

    private List<Library> _libraries;
    private LibraryFactory _libraryFactory;


    public LibraryRepo(LibraryFactory libraryFactory) {
        _libraryFactory = libraryFactory;
        _libraries = new ArrayList<>();
    }

    public Library addLibrary(User user){
        if (libraryExists(user)) {

            throw new IllegalStateException("User already has a library!");

        }
        Library myLibrary = _libraryFactory.createLibrary(user);

        _libraries.add(myLibrary);

        return myLibrary;
    }

    private boolean libraryExists(User user){

        for  (Library lib : _libraries) {

            if (lib.belongsTo(user)) {

                return true;
            }

        }
        return false;
    }

    /**
     * Finds and returns the Library associated with the given user ID.
     *
     * @param user the unique identifier of the user whose library is being searched
     * @return the Library instance belonging to the specified user
     * @throws IllegalStateException if no Library exists for the given userID
     */

    public Library findLibraryByUser(User user){
        for (Library lib : _libraries){
            if (lib.belongsTo(user)){
                return lib;
            }
        }
        throw new IllegalStateException("Library not found for user: " + user.toString());
    }

}
