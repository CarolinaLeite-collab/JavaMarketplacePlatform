package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;
import java.util.Set;

/**
 * Enumeration representing units of measurement for dimensions.
 * <p>
 * Supports {@link #CENTIMETERS} and {@link #INCHES}, with a {@link #fromString(String)} method
 * that converts various string representations to the corresponding {@link DimensionUnit}.
 * </p>
 */

public enum DimensionUnit implements ValueObject {
    CENTIMETERS("centimeters"),
    INCHES("inches");

    private final String _dimensionUnit;

    DimensionUnit(String dimensionUnit) {
        _dimensionUnit = dimensionUnit;
    }

    @Override
    public String toString() {
        return _dimensionUnit;
    }

    private static final Set<String> CENTIMETER_VARIANTS = Set.of(
            "CM", "CENT", "CENTIMETER", "CENTIMETERS",
            "CENTIMETRE", "CENTIMETRES"
    );

    private static final Set<String> INCH_VARIANTS = Set.of(
            "IN", "INS", "INCH", "INCHES"
    );

    public static DimensionUnit fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Dimension unit cannot be null or empty");
        }

        String normalized = input.trim().replace(".", "").toUpperCase();

        if (CENTIMETER_VARIANTS.contains(normalized)) {
            return CENTIMETERS;
        }
        if (INCH_VARIANTS.contains(normalized)) {
            return INCHES;
        }

        throw new IllegalArgumentException(
                "Dimension unit '" + input + "' is not a valid dimension unit. Please use inches or centimeters."
        );

    }

}