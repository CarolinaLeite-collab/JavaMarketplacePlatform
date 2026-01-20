package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemRepo {

    private final List<Item> items = new ArrayList<>();

    public boolean exists(Publication publication) {
        if (publication == null) return false;

        for (Item item : items) {
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

        Item item = new Item(publication, condition);
        items.add(item);
        return item;
    }

    public List<Item> getAll() {
        return Collections.unmodifiableList(items);
    }
}

