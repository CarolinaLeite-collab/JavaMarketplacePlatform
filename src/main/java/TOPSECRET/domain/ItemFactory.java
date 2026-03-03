package TOPSECRET.domain;

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
