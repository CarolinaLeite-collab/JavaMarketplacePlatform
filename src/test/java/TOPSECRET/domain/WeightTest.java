package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class WeightTest {

    private Weight fiveKg;
    private Weight zeroGrams;
    private Weight tenOunces;
    private Weight threePounds;

    @BeforeEach
    void setUp() {
        fiveKg = new Weight(5.0, Weight.WeightUnit.KILOGRAMS);
        zeroGrams = new Weight(0.0, Weight.WeightUnit.GRAMS);
        tenOunces = new Weight(10.0, Weight.WeightUnit.OUNCES);
        threePounds = new Weight(3.0, Weight.WeightUnit.POUNDS);
    }

    // Constructor tests - VALID

    @Test
    void constructorZeroWeight() {
        assertEquals(0.0, zeroGrams.getValue());
        assertEquals(Weight.WeightUnit.GRAMS, zeroGrams.getWeightUnit());
    }

    @ParameterizedTest
    @EnumSource(Weight.WeightUnit.class)
    void constructorValidWeightWithAllUnitsCreatesWeight(Weight.WeightUnit unit) {
        Weight weight = new Weight(1.5, unit);
        assertEquals(1.5, weight.getValue());
        assertEquals(unit, weight.getWeightUnit());
    }

    // Constructor tests - INVALID
    @Test
    void constructorNegativeWeightThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Weight(-5.0, Weight.WeightUnit.KILOGRAMS);
        });
        assertEquals("weight cannot be negative.", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(Weight.WeightUnit.class)
    void constructorNegativeWeightWithAnyUnitThrowsIllegalArgumentException(Weight.WeightUnit unit) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Weight(-1.0, unit));
        assertEquals("weight cannot be negative.", exception.getMessage());
    }
    @Test
    void constructorNanValueThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Weight(Double.NaN, Weight.WeightUnit.KILOGRAMS));
        assertEquals("NaN is not a finite number", exception.getMessage());
    }
    @Test
    void constructorPositiveInfinityThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Weight(Double.POSITIVE_INFINITY, Weight.WeightUnit.GRAMS));

        assertEquals("Infinity is not a finite number", exception.getMessage());
    }
    @Test
    void constructorNegativeInfinityThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Weight(Double.NEGATIVE_INFINITY, Weight.WeightUnit.POUNDS));
        assertEquals("-Infinity is not a finite number", exception.getMessage());
    }
    @Test
    void constructorNullWeightUnitThrowsNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new Weight(5.0, null));

        assertEquals("weightUnit is required.", exception.getMessage());
    }

    // Getters
    @Test
    void getValueReturnsCorrectValue() {
        assertEquals(5.0, fiveKg.getValue());
        assertEquals(0.0, zeroGrams.getValue());
        assertEquals(10.0, tenOunces.getValue());
        assertEquals(3.0, threePounds.getValue());
    }

    @Test
    void getWeightUnitReturnsCorrectUnit() {
        assertEquals(Weight.WeightUnit.KILOGRAMS, fiveKg.getWeightUnit());
        assertEquals(Weight.WeightUnit.GRAMS, zeroGrams.getWeightUnit());
        assertEquals(Weight.WeightUnit.OUNCES, tenOunces.getWeightUnit());
        assertEquals(Weight.WeightUnit.POUNDS, threePounds.getWeightUnit());
    }

    // toString() tests
    @Test
    void toStringKilograms() {
        String expected = "Weight: 5.0 kg";
        assertEquals(expected, fiveKg.toString());
    }
    @Test
    void toStringGrams() {
        Weight grams = new Weight(1000.0, Weight.WeightUnit.GRAMS);
        assertEquals("Weight: 1000.0 g", grams.toString());
    }
    @Test
    void toStringOunces() {
        Weight ounces = new Weight(10.5, Weight.WeightUnit.OUNCES);
        assertEquals("Weight: 10.5 oz", ounces.toString());
    }
    @Test
    void toStringPounds() {
        Weight pounds = new Weight(11.02, Weight.WeightUnit.POUNDS);
        assertEquals("Weight: 11.02 lb", pounds.toString());
    }

    @Test
    void toStringZeroWeight() {
        assertEquals("Weight: 0.0 g", zeroGrams.toString());
    }

    // WeightUnit behaviour
    @Test
    void getAbbreviationReturnsCorrectValueForAllUnits() {
        assertEquals("kg", Weight.WeightUnit.KILOGRAMS.getAbbreviation());
        assertEquals("g", Weight.WeightUnit.GRAMS.getAbbreviation());
        assertEquals("oz", Weight.WeightUnit.OUNCES.getAbbreviation());
        assertEquals("lb", Weight.WeightUnit.POUNDS.getAbbreviation());
    }
    @Test
    void weightUnitToStringReturnsAbbreviationForAllUnits() {
        assertEquals("kg", Weight.WeightUnit.KILOGRAMS.toString());
        assertEquals("g", Weight.WeightUnit.GRAMS.toString());
        assertEquals("oz", Weight.WeightUnit.OUNCES.toString());
        assertEquals("lb", Weight.WeightUnit.POUNDS.toString());
    }

    // equals
    @Test
    void equalsSameReferenceReturnsTrue() {
        assertEquals(fiveKg, fiveKg);
    }
    @Test
    void equalsSameValueAndSameUnitReturnsTrue() {
        Weight other = new Weight(5.0, Weight.WeightUnit.KILOGRAMS);
        assertEquals(fiveKg, other);
        assertEquals(other, fiveKg);
    }
    @Test
    void equalsDifferentValueReturnsFalse() {
        Weight other = new Weight(6.0, Weight.WeightUnit.KILOGRAMS);
        assertNotEquals(fiveKg, other);
    }
    @Test
    void equalsDifferentUnitReturnsFalse() {
        Weight other = new Weight(5.0, Weight.WeightUnit.GRAMS);
        assertNotEquals(fiveKg, other);
    }
    @Test
    void equalsNullReturnsFalse() {
        assertNotEquals(fiveKg, null);
    }
    @Test
    void equalsDifferentTypeReturnsFalse() {
        assertNotEquals(fiveKg, "Weight: 5.0 kg");
    }

    // hashCode
    @Test
    void equalWeightsHaveSameHashCode() {
        Weight w1 = new Weight(5.0, Weight.WeightUnit.KILOGRAMS);
        Weight w2 = new Weight(5.0, Weight.WeightUnit.KILOGRAMS);
        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
    }
    @Test
    void hashCodeDifferentValueReturnsDifferentHashCode() {
        Weight other = new Weight(6.0, Weight.WeightUnit.KILOGRAMS);
        assertNotEquals(fiveKg, other);
        assertNotEquals(fiveKg.hashCode(), other.hashCode());
    }
    @Test
    void hashCodeDifferentUnitReturnsDifferentHashCode() {
        Weight other = new Weight(5.0, Weight.WeightUnit.GRAMS);

        assertNotEquals(fiveKg, other);
        assertNotEquals(fiveKg.hashCode(), other.hashCode());
    }
}
