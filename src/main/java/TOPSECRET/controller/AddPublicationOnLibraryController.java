package TOPSECRET.controller;

import TOPSECRET.domain.*;
import java.util.List;

/**
 * Controller responsible for handling the addition of publications to a user's library.
 * <p>
 * This controller interacts with the {@link PublicationRepo} and {@link LibraryRepo}
 * to retrieve available publications and to add selected publications to a user's library.
 * </p>
 */

public class AddPublicationOnLibraryController {
    private final PublicationRepo _publicationRepo;
    private final LibraryRepo _libraryRepo;

    public AddPublicationOnLibraryController(PublicationRepo publicationRepo,  LibraryRepo libraryRepo) {
        _publicationRepo = publicationRepo;
        _libraryRepo = libraryRepo;
    }

    public Library getMyLibrary(User user) {
        return _libraryRepo.findByUser(user);
    }

    public List<Publication> getListOfAvailablePublications(User user) {
        Library myLibrary = getMyLibrary(user);
        List<Publication> existentPublication = myLibrary.getAllPublications();
        return _publicationRepo.getDifferentOf(existentPublication);
    }

    public boolean addPublicationToLibrary(Publication selectedPublication, User user) {
        Library myLibrary = getMyLibrary(user);
        return myLibrary.addPublicationToLibrary(selectedPublication);
    }
}
