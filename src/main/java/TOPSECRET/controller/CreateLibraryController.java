package TOPSECRET.controller;

import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.UserId;

/**
 * Controller responsible for handling the creation of the personal {@link Library} of a {@link UserId}.
 *
 * <p>
 * It receives requests and delegates the
 * creation and persistence of a {@link Library} instance to the
 * {@link ILibraryRepo}.
 * </p>
 */

public class CreateLibraryController {

    private final ILibraryRepo _iLibraryRepo;

    public CreateLibraryController(ILibraryRepo lr, UserId userId){

        _iLibraryRepo =lr;

    }

    public Library createLibrary(UserId userId){

        Library library= _iLibraryRepo.addLibrary(userId);

        return library;

    }

}
