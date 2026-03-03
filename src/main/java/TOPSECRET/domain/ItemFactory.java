package TOPSECRET.domain;
/**
 * Factory responsible for creating {@link Item} instances.
 * <p>
 * This class encapsulates the instantiation logic of {@code Item},
 * centralizing object creation and isolating clients from constructor
 * details. Any exception thrown during the creation process is wrapped
 * into an {@link InstantiationException}.
 */

public class ItemFactory {
    public Item create(Publication publication, Condition condition) throws InstantiationException {
        try {
            return new Item(publication, condition);
        }
        catch (final Exception e) {
            throw new InstantiationException("Unable to instantiate Item: " + e.getMessage());
        }
    }
}
