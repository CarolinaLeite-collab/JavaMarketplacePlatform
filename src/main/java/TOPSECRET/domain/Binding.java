package TOPSECRET.domain;

/**
 * Binding enum represents types of book/magazine bindings.
 */

public enum Binding {

    //Enum values can't have spaces; displayName provides a human-readable text
    PUR("PUR binding"),
    SADDLE_STITCH("Saddle stitch binding"),
    HARDCOVER("Hardcover binding"),
    SINGER_SEWN("Singer sewn binding"),
    SECTION_SEWN("Section sewn binding"),
    COPTIC_STITCH("Coptic stitch binding"),
    WIRO("Wiro binding"),
    INTERSCREW("Interscrew binding");

    //Each enum value stores its own human-readable name
    private final String _displayName;

    //Constructor assigns the display name
    Binding(String displayName) {
        _displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

    //Returns the display name for printing or logging
    @Override
    public String toString() {
        return _displayName;
    }
}