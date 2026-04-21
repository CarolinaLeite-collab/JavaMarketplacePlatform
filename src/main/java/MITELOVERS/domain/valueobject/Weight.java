package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

import java.util.Objects;

/**
 * Represents the weight of an object along with its unit of measurement.
 * <p>
 * Supports {@link WeightUnit} enum with grams, kilograms, ounces, and pounds.
 * Ensures that the weight value is non-negative and provides getters and a string representation.
 * </p>
 */

public class Weight implements ValueObject {

    public enum WeightUnit {
        GRAMS("g"),
        KILOGRAMS("kg"),
        OUNCES("oz"),
        POUNDS("lb");

        private final String _abbreviation;

        WeightUnit(String abbreviation) {
            _abbreviation = abbreviation;
        }

        public String getAbbreviation() {
            return _abbreviation;
        }

        @Override
        public String toString() {
            return _abbreviation;
    }
    }

    private final double _value;
    private final WeightUnit _weightUnit;

    public Weight(double value, WeightUnit weightUnit) {
        validateValue(value);
        _value = value;
        _weightUnit = Objects.requireNonNull(weightUnit, "weightUnit is required.");
    }

    private static void validateValue(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(value + " is not a finite number");
        }
        if (value < 0) {
            throw new IllegalArgumentException("weight cannot be negative.");
        }
    }


    public double getValue() {
        return _value;
    }

    public WeightUnit getWeightUnit() {
        return _weightUnit;
    }

    @Override
    public String toString() {
        return "Weight: " + _value + " " + _weightUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weight weight)) return false;
        return Double.compare(weight._value, _value) == 0 &&
                _weightUnit == weight._weightUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_value, _weightUnit);
    }

}
