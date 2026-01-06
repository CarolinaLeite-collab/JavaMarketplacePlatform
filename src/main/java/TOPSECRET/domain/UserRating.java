package TOPSECRET.domain;


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

