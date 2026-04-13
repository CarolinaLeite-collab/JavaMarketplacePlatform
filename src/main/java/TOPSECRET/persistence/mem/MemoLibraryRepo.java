package TOPSECRET.persistence.mem;

import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.library.LibraryFactory;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.LibraryId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

public class MemoLibraryRepo implements ILibraryRepo {

    private final Map<LibraryId, Library> DATA = new HashMap<LibraryId, Library>();
    private LibraryFactory _libraryFactory;


    public MemoLibraryRepo(LibraryFactory libraryFactory) {
        _libraryFactory = libraryFactory;
    }

    @Override
    public Library save(Library myLibrary) {

        DATA.put(myLibrary.identity(), myLibrary);
        return myLibrary;

    }

    @Override
    public Iterable<Library> findAll() {

        return DATA.values();

    }

    @Override
    public Optional<Library> ofIdentity(LibraryId id) {

        if(!containsOfIdentity(id)) {

            return Optional.empty();

        } else {

            return Optional.of(DATA.get(id));

        }

    }

    @Override
    public boolean containsOfIdentity(LibraryId id) {

        return DATA.containsKey(id);

    }

    @Override
    public Library addLibrary(UserId userId){

        LibraryId libraryId = LibraryId.fromUserId(userId);

        if (containsOfIdentity(libraryId)){

            throw new IllegalStateException("User already has a library!");

        }

        Library myLibrary = _libraryFactory.createLibrary(libraryId);

        return save(myLibrary);

    }

    @Override
    public Library findLibraryByUserId(UserId userId) {

        LibraryId libraryID = LibraryId.fromUserId(userId);

        return ofIdentity(libraryID)
                .orElseThrow(() -> new IllegalStateException("Library not found for user!"));

    }

    @Override
    public List<Item> getItemsInLibraryByUserId(UserId userId) {

        LibraryId libraryID = LibraryId.fromUserId(userId);

        if (!containsOfIdentity(libraryID)) {

            throw new IllegalStateException("Library not found for user!");

        }

        return (DATA.get(libraryID)).getItemsInLibrary();

    }

}
