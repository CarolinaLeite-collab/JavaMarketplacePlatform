package MITELOVERS.persistence.mem;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.ItemId;

import java.util.*;

/**
 * Repository responsible for managing {@link Item} entities.
 * <p>
 * This class provides management mechanisms for
 * {@link Item} objects.
 *  <p>
 * Each edition is uniquely identified by its {@link ItemId}.
 *<p>
 */

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
}
