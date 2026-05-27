package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;
import MITELOVERS.ddd.ValueObject;

/**
 * Value object representing the URL of an item's cover image.
 * Optional — an item may not have a cover image.
 */

public class Picture implements ValueObject{

    private final String _value;

    public Picture(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("CoverUrl cannot be empty");
        _value = value;
    }

    public String getValue() { return _value; }

    @Override
    public String toString() { return _value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture other)) return false;
        return _value.equals(other._value);
    }

    @Override
    public int hashCode() {
        return _value.hashCode();
    }

}
