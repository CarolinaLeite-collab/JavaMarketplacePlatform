package MITELOVERS.persistence.mem;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-memory repository responsible for managing {@link ListOfItems} instances.
 */

@Repository
@Profile("mem")
public class MemListOfItemsRepo implements IListOfItemsRepo {

    private final Map<ListOfItemsId, ListOfItems> _data = new HashMap<>();

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

    @Override
    public List<ListOfItems> findListOfItemsByUserId(UserId userId){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteListOfItems(ListOfItemsId listOfItemsId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}