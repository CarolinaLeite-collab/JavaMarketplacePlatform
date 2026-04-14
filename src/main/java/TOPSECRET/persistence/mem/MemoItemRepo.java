package TOPSECRET.persistence.mem;

import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.repository.IItemRepo;
import TOPSECRET.domain.item.Item;
import TOPSECRET.domain.item.ItemFactory;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.Description;
import TOPSECRET.domain.valueobject.EditionId;
import TOPSECRET.domain.valueobject.ItemId;

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

public class MemoItemRepo implements IItemRepo {

    private final Map<ItemId, Item> DATA = new HashMap<ItemId, Item>();
    private  final ItemFactory _itemFactory;

    public MemoItemRepo(ItemFactory itemFactory) {
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

    public List<ItemId> findAllKeys(){

        List<ItemId> itemIds = new ArrayList<>(DATA.keySet());

        return itemIds;
    }

    @Override
    public Item addItem(EditionId editionId, Condition condition, Description description) {

        Item item = _itemFactory.createItem(editionId, condition, description);

        if (containsOfIdentity(item.identity())) {
            throw new IllegalArgumentException("Item already exists");
        }

        return save(item);
    }

    @Override
    public List<Item> getDifferentOf(List<Item> existentItems) {

        if (existentItems == null) {
            return List.copyOf(DATA.values());
        }

        List<Item> result = new ArrayList<>();
        for (Item item : DATA.values()) {
            if (!existentItems.contains(item)) {
                result.add(item);
            }
        }
        return List.copyOf(result);
    }
}
