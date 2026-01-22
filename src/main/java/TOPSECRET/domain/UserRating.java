package TOPSECRET.domain;

/**
 * Represents a user's rating using a star-based system from one to five stars.
 * <p>
 * Each enum constant maps to a visual representation of stars for display purposes.
 * </p>
 */

public enum UserRating {
    ONE_STAR("★"),
    TWO_STARS("★★"),
    THREE_STARS("★★★"),
    FOUR_STARS("★★★★"),
    FIVE_STARS("★★★★★");

    private final String _stars;

    UserRating(String stars) {
        _stars = stars;
    }

    //Override to obtain a String with stars only
    @Override
    public String toString() {
        return _stars;
    }
}

