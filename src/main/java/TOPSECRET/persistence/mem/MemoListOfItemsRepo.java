package TOPSECRET.persistence.mem;

import TOPSECRET.domain.repository.IListOfItemsRepo;
import TOPSECRET.domain.listofitems.ListOfItems;
import TOPSECRET.domain.listofitems.ListOfItemsFactory;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.ListOfItemsId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository responsible for managing {@link ListOfItems} instances.
 * <p>
 * This class handles the storage, and retrieval of private lists of publications.
 * It ensures that duplicate lists (same user, name, and genre) are not allowed.
 * </p>
 */

public class MemoListOfItemsRepo implements IListOfItemsRepo {

    private final Map<ListOfItemsId, ListOfItems> _data;
    private final ListOfItemsFactory _factory;

    public MemoListOfItemsRepo() {
        this(new ListOfItemsFactory());
    }

    public MemoListOfItemsRepo(ListOfItemsFactory factory) {
        _data = new HashMap<>();
        _factory = factory;
    }

    @Override
    public ListOfItems addListOfItems(UserId userId, String name, GenreId genreId) {
        ListOfItems newList = _factory.createListOfItems(userId, name, genreId);
        ListOfItemsId _id = newList.identity();

        if (_data.containsKey(_id))
            return null;

        _data.put(_id, newList);
        return newList;
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

    // -----------------------------------------------------------------
    // Temporary solution for US requirements before DTO implementation
    // -----------------------------------------------------------------

    @Override
    public Map<ListOfItemsId, String> getIdNameMap() {
        return _data.values().stream()
                .collect(Collectors.toUnmodifiableMap(
                        ListOfItems::identity,
                        ListOfItems::getName
                ));
    }

}
