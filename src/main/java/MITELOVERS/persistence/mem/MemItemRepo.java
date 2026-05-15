package MITELOVERS.persistence.mem;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-memory implementation of {@link IItemRepo}.
 * <p>
 *   This repository is responsible for persisting {@link Item} instances
 *   during runtime, using a {@link HashMap} as storage, with the key being {@link ItemId}.
 *  </p>
 *  <p>
 *   It provides basic CRUD-like operations such as saving, retrieving by identity,
 *   checking existence, and listing all stored entities.
 *  </p>
 */

@Repository
@Profile("mem")

public class MemItemRepo implements IItemRepo {

    private final Map<ItemId, Item> DATA = new HashMap<ItemId, Item>();

    public MemItemRepo() {

    }

    @Override
    public Item save (Item item){
        DATA.put(item.identity(), item);
        return item;
    }

    @Override
    public Iterable<Item> findAll(){
        return DATA.values();
    }

    @Override
    public Optional<Item> ofIdentity(ItemId id){
        if (!containsOfIdentity(id)){

            return Optional.empty();
        }
        return Optional.of(DATA.get(id));
    }

    @Override
    public boolean containsOfIdentity(ItemId id){
        return DATA.containsKey(id);
    }

    @Override
    public List<ItemId> findAllKeys(){

        return new ArrayList<>(DATA.keySet());

    }

    @Override
    public List<Item> findByIdInOrderByDescriptionAsc(Collection<String> ids) {
        throw new UnsupportedOperationException();
    }
}
