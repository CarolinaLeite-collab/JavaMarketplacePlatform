package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

import java.util.UUID;

/**
 * Represents a Stock Keeping Unit (SKU) for an item.
 * <p>
 * SKUs are automatically generated 10-character alphanumeric codes (A–F, 0–9) and cannot be created manually.
 * </p>
 */

public class SKU implements ValueObject {

    // SKU gerado automaticamente pela aplicação:
    // 10 caracteres alfanuméricos (A–F, 0–9)
    private static final int _length = 10;
    private static final String _format = "^[A-F0-9]{" + _length + "}$";

    private final String _value;

    public SKU() {
        _value = generateRandomSKU();
    }

    // Reconstruction from persisted value
    public SKU(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or blank.");
        }
        if (!value.matches(_format)) {
            throw new IllegalArgumentException("SKU must match format " + _format + ".");
        }
        _value = value;
    }

    // Internal and controlled creation
    private String generateRandomSKU() {
        String uuid = UUID.randomUUID().toString();   // hex + hífens
        String compact = uuid.replace("-", "");       // remove hífens
        return compact.substring(0, _length).toUpperCase();
    }

    public String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return _value;
    }

    // Value Object semantics
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

