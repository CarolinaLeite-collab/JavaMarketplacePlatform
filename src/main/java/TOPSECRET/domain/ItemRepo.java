package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class ItemRepo {

    private final List<Item> _items = new ArrayList<>();
    private ItemFactory _itemFactory;

    public ItemRepo() {
        _itemFactory = new ItemFactory();
    }

    public boolean exists(Publication publication) {
        if (publication == null) return false;

        for (Item item : _items) {
            if (item.getPublication().equals(publication)) {
                return true;
            }
        }
        return false;
    }

    public Item createItem(Publication publication, Condition condition) {
        if (exists(publication)) {
            throw new IllegalArgumentException("Item for this publication already exists!");
        }

        Item item = _itemFactory.createItem(publication, condition);
        _items.add(item);
        return item;
    }

    public List<Item> getAll() {
        return Collections.unmodifiableList(_items);
    }

    public List<Item> getDifferentOf(List<Item> existentItems) {
        List<Item> result = new ArrayList<>();
        for (Item item : _items) {
            if (!existentItems.contains(item)) {
                result.add(item);
            }
        }
        return List.copyOf(result);
    }
}

