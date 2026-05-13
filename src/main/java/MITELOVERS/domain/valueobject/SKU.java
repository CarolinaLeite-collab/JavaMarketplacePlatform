package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

import java.util.UUID;

/**
 * Represents a Stock Keeping Unit (SKU) for an item.
 * <p>
 * SKUs are automatically generated 10-character alphanumeric codes (A–F, 0–9).
 * A secondary constructor is provided for reconstruction from persistence.
 * </p>
 */
public class SKU implements ValueObject {

    private static final int _length = 10;
    private static final String _format = "^[A-F0-9]{" + _length + "}$";

    private final String _value;

    public SKU() {
        _value = generateRandomSKU();
    }

    // Used by the assembler — reconstructs from an existing SKU string
    public SKU(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("SKU cannot be null or blank.");
        if (!value.matches(_format))
            throw new IllegalArgumentException("SKU must match format " + _format + ".");
        _value = value;
    }

    private String generateRandomSKU() {
        String uuid = UUID.randomUUID().toString();
        String compact = uuid.replace("-", "");
        return compact.substring(0, _length).toUpperCase();
    }

    public String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return _value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SKU)) return false;
        SKU other = (SKU) o;
        return _value.equals(other._value);
    }

    @Override
    public int hashCode() {
        return _value.hashCode();
    }
}