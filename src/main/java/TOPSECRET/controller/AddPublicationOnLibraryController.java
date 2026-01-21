package TOPSECRET.controller;

import TOPSECRET.domain.*;
import java.util.List;


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
