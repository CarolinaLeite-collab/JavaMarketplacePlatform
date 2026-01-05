package TOPSECRET.domain;
/**
 * Represents the physical condition of a book or magazine.
 * This enum provides a standardized rating system for assessing the state
 * of publications, ranging from LIKE_NEW (best) to POOR (worst).
 */

public enum Condition {
    LIKE_NEW("Used but in perfect condition"),
    GOOD("Minor imperfections"),
    FAIR("Visible imperfections but readable"),
    POOR("Damaged or incomplete");

    private final String description;

    Condition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}



