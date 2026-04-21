package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Binding enum represents types of book/magazine bindings.
 */

public enum Binding implements ValueObject {

    PUR("PUR binding"),
    SADDLE_STITCH("Saddle stitch binding"),
    HARDCOVER("Hardcover binding"),
    SINGER_SEWN("Singer sewn binding"),
    SECTION_SEWN("Section sewn binding"),
    COPTIC_STITCH("Coptic stitch binding"),
    WIRO("Wiro binding"),
    INTERSCREW("Interscrew binding");


    private final String _displayName;

    Binding(String displayName) {
        _displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

    @Override
    public String toString() {
        return _displayName;
    }
}
