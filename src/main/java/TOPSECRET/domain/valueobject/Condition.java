package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * Represents the physical condition of a book or magazine.
 * This enum provides a standardized rating system for assessing the state
 * of publications, ranging from LIKE_NEW (best) to POOR (worst).
 */

public enum Condition implements ValueObject {
    LIKE_NEW("Used but in perfect condition"),
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



