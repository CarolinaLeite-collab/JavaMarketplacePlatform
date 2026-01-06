package TOPSECRET.domain;

public class Weight {

    public enum WeightUnit {
        GRAMS("g"),
        KILOGRAMS("kg"),
        OUNCES("oz"),
        POUNDS("lb");

        private final String _abbreviation;
        WeightUnit(String abbreviation) {
            this._abbreviation = abbreviation;
        }
        public String getAbbreviation() {
            return _abbreviation;
        }
    }
    private final double _value;
    private final WeightUnit _weightUnit;

    private void validate(double _value) {
        if (_value < 0) {
            throw new IllegalArgumentException("weight cannot be negative.");
        }
    }

    public Weight(double value, WeightUnit weightUnit) {
        this.validate(_value);
        this._value = value;
        this._weightUnit = weightUnit;
    }

    public double getValue() {
        return _value;
    }
    public WeightUnit getWeightUnit() {
        return _weightUnit;
    }

    @Override
    public String toString() {
        return "Weight: " + _value + " " + _weightUnit.getAbbreviation();
    }
}
