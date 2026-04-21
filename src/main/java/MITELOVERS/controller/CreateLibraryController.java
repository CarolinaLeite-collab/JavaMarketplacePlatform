package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.UserId;

/**
 * Controller responsible for handling the creation of a user's personal {@link Library}.
 * <p>
 * This controller acts as an application layer entry point that delegates the creation
 * and persistence of a {@link Library} to the {@link ILibraryRepo}.
 * </p>
 *
 * <p>
 * It ensures that a library can be created for a given {@link UserId}, coordinating
 * the request between the domain and persistence layers.
 * </p>
 */

public class CreateLibraryController {

    private final ILibraryRepo _iLibraryRepo;

    public CreateLibraryController(ILibraryRepo lr, UserId userId){

        _iLibraryRepo =lr;

    }

    public boolean createLibrary(UserId userId){

        _iLibraryRepo.addLibrary(userId);

        return true;

    }

}
