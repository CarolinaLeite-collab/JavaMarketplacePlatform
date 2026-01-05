package TOPSECRET.domain;

public class Weight {

    public enum WeightUnit {
        GRAMS("g"),
        KILOGRAMS("kg"),
        OUNCES("oz"),
        POUNDS("lb");

        private final String abbreviation;
        WeightUnit(String abbreviation) {
            this.abbreviation = abbreviation;
        }
        public String getAbbreviation() {
            return abbreviation;
        }
    }
    private final double value;
    private final WeightUnit weightUnit;

    private void validate(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("weight cannot be negative.");
        }
    }

    public Weight(double value, WeightUnit weightUnit) {
        this.validate(value);
        this.value = value;
        this.weightUnit = weightUnit;
    }

    public double getValue() {
        return value;
    }
    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    @Override
    public String toString() {
        return "Weight: " + value + " " + weightUnit.getAbbreviation();
    }
}
