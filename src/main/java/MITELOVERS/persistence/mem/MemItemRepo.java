package MITELOVERS.persistence.mem;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
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
    private  final ItemFactory _itemFactory;

    public MemItemRepo(ItemFactory itemFactory) {
        _itemFactory = itemFactory;
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
    public ItemId addItem(EditionId editionId, Condition condition, Description description) {

        Item item = _itemFactory.createItem(editionId, condition, description);

        if (containsOfIdentity(item.identity())) {
            throw new IllegalArgumentException("Item already exists");
        }

        return save(item).identity();
    }

    @Override
    public List<ItemId> getDifferentOf(List<ItemId> existentItemIds) {

        if (existentItemIds == null) {
            return List.copyOf(DATA.keySet());
        }

        List<ItemId> result = new ArrayList<>();
        for (ItemId itemId : DATA.keySet()) {
            if (!existentItemIds.contains(itemId)) {
                result.add(itemId);
            }
        }
        return List.copyOf(result);
    }
}
