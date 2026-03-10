package TOPSECRET.controller;

import TOPSECRET.domain.*;

import java.util.List;

public class AddPublicationToListController {

    private final ListOfPublicationsRepo _listRepo;
    private final LibraryRepo _libraryRepo;

    public AddPublicationToListController(ListOfPublicationsRepo listRepo, LibraryRepo libraryRepo) {
        if (listRepo == null) throw new IllegalArgumentException("ListOfPublicationsRepo is mandatory");
        if (libraryRepo == null) throw new IllegalArgumentException("LibraryRepo is mandatory");

        _listRepo = listRepo;
        _libraryRepo = libraryRepo;
    }

    public List<ListOfPublications> getMyLists(User user) {
        if (user == null) throw new IllegalArgumentException("User is mandatory");
        return _listRepo.findListsByUser(user);
    }

    public List<PublicationDetails> getPublicationsInMyLibrary(User user) {
        if (user == null) throw new IllegalArgumentException("User is mandatory");
        Library lib = _libraryRepo.findLibraryByUser(user); // throws if not found
        return lib.getPublicationsInLibrary();
    }

    public void addPublicationToList(User user, String listName, Genre genre, Identifier identifier) {

        if (user == null) throw new IllegalArgumentException("User is mandatory");
        if (listName == null || listName.isBlank()) throw new IllegalArgumentException("List name is mandatory");
        if (genre == null) throw new IllegalArgumentException("Genre is mandatory");
        if (identifier == null) throw new IllegalArgumentException("Identifier is mandatory");

        ListOfPublications myList = _listRepo.findByOwnerNameAndGenre(user, listName, genre);
        if (myList == null) {
            throw new IllegalStateException("List not found");
        }

        Library lib = _libraryRepo.findLibraryByUser(user);

        Publication publication = findPublicationByIdentifier(lib.getAllPublications(), identifier);
        if (publication == null) {
            throw new IllegalStateException("Publication not found in library");
        }

        myList.addPublication(publication);
    }

    private Publication findPublicationByIdentifier(List<Publication> publications, Identifier identifier) {
        for (Publication p : publications) {
            if (p.getIdentifier().equals(identifier)) {
                return p;
            }
        }
        return null;
    }
}