package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Number identifying the {@link MITELOVERS.domain.edition.Edition} (e.g., first edition, second edition)
 */

public class EditionNumber implements ValueObject {
    private final int value;

    public EditionNumber(int value) {
        if (value <= 0) throw new IllegalArgumentException("Edition number must be positive");
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
