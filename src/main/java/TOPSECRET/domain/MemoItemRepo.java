package TOPSECRET.domain;

import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.publication.Publication;
import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.Description;
import TOPSECRET.domain.valueobject.EditionId;
import TOPSECRET.domain.valueobject.ItemId;

import java.util.*;

/**
 * <h3>Item Repository responsible for managing all {@link Item} instances in the domain </h3>
 * <p>
 * Responsible for creation, existence checking, and read-only retrieval of items.
 * Each {@link Publication} is only able to correspond to a single {@link Item}.
 * </p>
 *
 * <p>
 * This repository ensures the uniqueness of items based on their associated publications,
 * preventing duplicates, and ensures referential integrity between publications and items.
 * </p>
 */

public class MemoItemRepo implements IItemRepo{

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
        return Optional.ofNullable(DATA.get(id));
    }

    @Override
    public boolean containsOfIdentity(ItemId id){
        return DATA.containsKey(id);
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
