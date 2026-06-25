package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Represents the physical condition of a book or magazine.
 * This enum provides a standardized rating system for assessing the state
 * of publications, ranging from MINT (best) to POOR (worst).
 */

public enum Condition implements ValueObject {
    MINT("Used but in perfect condition"),
    GOOD("Minor imperfections"),
    FAIR("Visible imperfections but readable"),
    POOR("Damaged or incomplete");

    private final String _description;

    Condition(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }


}



