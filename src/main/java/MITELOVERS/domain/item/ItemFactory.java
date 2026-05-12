package MITELOVERS.domain.item;

import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link Item} instances.
 * <p>
 * This class encapsulates the instantiation logic of {@code Item},
 * centralizing object creation and isolating clients from constructor
 * details. Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

@Component
public class ItemFactory {
    public Item createItem(EditionId editionId, Condition condition, Description description) {
        return new Item(editionId, condition, description);
    }

    public Item createItem(ItemId itemId, EditionId editionId, Condition condition,
                           Description description, SaleStatus saleStatus) {

        return new Item(itemId, editionId, condition, description, saleStatus);

    }

}
