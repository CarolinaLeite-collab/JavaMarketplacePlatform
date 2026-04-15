package TOPSECRET.persistence.mem;

import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.library.LibraryFactory;
import TOPSECRET.domain.repository.ILibraryRepo;
import TOPSECRET.domain.valueobject.ItemId;
import TOPSECRET.domain.valueobject.LibraryId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.*;

/**
 * In-memory repository responsible for managing {@link Library} aggregates.
 *
 * <p>
 * This implementation of {@link ILibraryRepo} stores {@link Library} instances
 * in a {@link java.util.HashMap}, using {@link LibraryId} as the key.
 * It acts as a persistence adapter that isolates the domain and controller
 * layers from storage concerns.
 * </p>
 *
 * <p>
 * The repository supports basic CRUD-style operations, as well as domain-specific
 * queries such as retrieving a library by {@link UserId}, checking whether an
 * {@link ItemId} exists in any library, and retrieving all items for a given user.
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

    public ArrayList<LibraryId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

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

        Library myLibrary = _libraryFactory.createLibrary(userId);

        if (containsOfIdentity(myLibrary.identity())) {

            throw new IllegalStateException("User already has a library!");

        }

        return save(myLibrary);

    }

    @Override
    public Library findLibraryByUserId(UserId userId) {

        LibraryId libraryID = LibraryId.fromUserId(userId);

        return ofIdentity(libraryID)
                .orElseThrow(() -> new IllegalStateException("Library not found for user!"));

    }

    @Override
    public List<ItemId> getItemsInLibraryByUserId(UserId userId) {

        LibraryId libraryID = LibraryId.fromUserId(userId);

        if (!containsOfIdentity(libraryID)) {

            throw new IllegalStateException("Library not found for user!");

        }

        return (DATA.get(libraryID)).getItemsIdInLibrary();

    }

    @Override
    public boolean existsItemIdInAnyLibrary(ItemId itemId) {

        for (Library library : findAll()) {

            if (library.getItemsIdInLibrary().contains(itemId)) {
                return true;
            }
        }

        return false;
    }

}
