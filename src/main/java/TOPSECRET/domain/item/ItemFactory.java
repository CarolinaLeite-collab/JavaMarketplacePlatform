package TOPSECRET.domain.item;

import TOPSECRET.domain.valueobject.Condition;
import TOPSECRET.domain.valueobject.Description;
import TOPSECRET.domain.valueobject.EditionId;

/**
 * Factory responsible for creating {@link Item} instances.
 * <p>
 * This class encapsulates the instantiation logic of {@code Item},
 * centralizing object creation and isolating clients from constructor
 * details. Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

public class ItemFactory {
    public Item createItem(EditionId editionId, Condition condition, Description description) {
        return new Item(editionId, condition, description);
    }
}
