package MITELOVERS.persistence.mem;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.*;

/**
 * Repository responsible for managing {@link ListOfItems} instances.
 * <p>
 * This class handles the storage, and retrieval of private lists of publications.
 * It ensures that duplicate lists (same user, name, and genre) are not allowed.
 * </p>
 */

public class MemListOfItemsRepo implements IListOfItemsRepo {

    private final Map<ListOfItemsId, ListOfItems> _data;

    public MemListOfItemsRepo() {
        _data = new HashMap<>();
    }

    // ------------------------
    // Generic Repo operations
    // ------------------------

    @Override
    public ListOfItems save(ListOfItems entity) {

        _data.put(entity.identity(), entity);
        return entity;
    }

    @Override
    public Iterable<ListOfItems> findAll() {

        return List.copyOf(_data.values());
    }

    @Override
    public List<ListOfItemsId> findAllKeys() {

        return new ArrayList<>(_data.keySet());
    }

    @Override
    public Optional<ListOfItems> ofIdentity(ListOfItemsId id) {

        return Optional.ofNullable(_data.get(id));
    }

    @Override
    public boolean containsOfIdentity(ListOfItemsId id) {

        return _data.containsKey(id);
    }

    // ------------------------
    // Domain-specific queries
    // ------------------------

    @Override
    public List<ListOfItems> findPublicListsByGenre(GenreId genreId) {

        return _data.values().stream()
                .filter(l -> !l.isPrivate() && l.getGenreId().equals(genreId))
                .toList();
    }

    @Override
    public List<ListOfItems> findListsByUserId(UserId userId) {

        return _data.values().stream()
                .filter(l -> l.getUserId().equals(userId))
                .toList();
    }

    @Override
    public ListOfItems findByOwnerNameAndGenre(UserId userId, String name, GenreId genreId) {

        String normalizedName = name.trim();

        return _data.values().stream()
                .filter(l -> l.getUserId().equals(userId))
                .filter(l -> l.getName().equalsIgnoreCase(normalizedName))
                .filter(l -> l.getGenreId().equals(genreId))
                .findFirst()
                .orElse(null);
    }
}
