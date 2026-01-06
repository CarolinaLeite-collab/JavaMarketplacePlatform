package TOPSECRET.domain;

public class Weight {

    public enum WeightUnit {
        GRAMS("g"),
        KILOGRAMS("kg"),
        OUNCES("oz"),
        POUNDS("lb");

        private final String _abbreviation;
        WeightUnit(String abbreviation) {
            _abbreviation = abbreviation;
        }
        public String get_abbreviation() {
            return _abbreviation;
        }
    }
    private final double _value;
    private final WeightUnit _weightUnit;

    private void validate(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("weight cannot be negative.");
        }
    }

    public Weight(double value, WeightUnit weightUnit) {
        validate(value);
        _value = value;
        _weightUnit = weightUnit;
    }

    public double get_value() {
        return _value;
    }
    public WeightUnit get_weightUnit() {
        return _weightUnit;
    }

    @Override
    public String toString() {
        return "Weight: " + _value + " " + _weightUnit.get_abbreviation();
    }
}
