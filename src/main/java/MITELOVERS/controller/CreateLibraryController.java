package MITELOVERS.controller;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.repository.ILibraryRepo;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Controller;

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

@Controller
public class CreateLibraryController {

    private final ILibraryRepo _libraryRepo;
    private final LibraryFactory _libraryFactory;

    public CreateLibraryController(ILibraryRepo libraryRepo, LibraryFactory libraryFactory){

        _libraryRepo = libraryRepo;
        _libraryFactory = libraryFactory;

    }

    public boolean createLibrary(UserId userId){

        Library myLibrary = _libraryFactory.createLibrary(userId);

        if (_libraryRepo.containsOfIdentity(myLibrary.identity())) {

            throw new IllegalStateException("User already has a library!");

        }

        _libraryRepo.save(myLibrary);

        return true;

    }

}
