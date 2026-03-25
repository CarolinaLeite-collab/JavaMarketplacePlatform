package TOPSECRET.controller;

import TOPSECRET.domain.ILibraryRepo;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.MemoLibraryRepo;
import TOPSECRET.domain.User;

/**
 * Controller responsible for handling the creation of the personal {@link Library} of a {@link User}.
 *
 * <p>
 * It receives requests and delegates the
 * creation and persistence of a {@link Library} instance to the
 * {@link MemoLibraryRepo}.
 * </p>
 */

public class CreateLibraryController {

    private final ILibraryRepo _libraryRepo;

    public CreateLibraryController(ILibraryRepo lr, User user){

        _libraryRepo =lr;

    }

    public Library createLibrary(User user){

        Library library= _libraryRepo.addLibrary(user);

        return library;

    }

}
