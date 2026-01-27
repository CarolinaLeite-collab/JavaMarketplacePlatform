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

    public LibraryRepo(){

        _libraries = new ArrayList<>();

    }

    public Library createMyLibrary(User user){

        //if user already has a library, throw exception (library will not be created)
        if (myLibraryExists(user)) {

            throw new IllegalStateException("User already has a library!");

        }

        //instantiate new Library
        Library myLibrary = new Library(user);

        //add to libraryRepo
        _libraries.add(myLibrary);

        //returns the library;
        return myLibrary;

    }

    private boolean myLibraryExists(User user){

        for  (Library lib : _libraries){

            if (lib.getUser().equals(user)){

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

    public Library findByUser(User user){
        for (Library lib : _libraries){
            if (lib.getUser().equals(user)){
                return lib;
            }
        }
        throw new IllegalStateException("Library not found for user: " + user.toString());
    }

}
