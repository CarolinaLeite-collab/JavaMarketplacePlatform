package TOPSECRET.controller;

import TOPSECRET.domain.Library;
import TOPSECRET.domain.LibraryRepo;
import TOPSECRET.domain.User;

/**
 * Controller responsible for handling the creation of the personal {@link Library} of a {@link User}.
 *
 * <p>
 * It receives requests and delegates the
 * creation and persistence of a {@link Library} instance to the
 * {@link LibraryRepo}.
 * </p>
 */

public class CreateLibraryController {

    private LibraryRepo _libraryRepo;

    public CreateLibraryController(LibraryRepo lr, User user){

        _libraryRepo =lr;

    }

    public Library createMyLibrary(User user){

        Library library= _libraryRepo.createMyLibrary(user);

        return library;

    }

}
