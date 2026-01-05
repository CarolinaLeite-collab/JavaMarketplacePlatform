package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void constructor_validKilograms() {
        assertNotNull(fiveKg);
        assertEquals(5.0, fiveKg.getValue());
        assertEquals(Weight.WeightUnit.KILOGRAMS, fiveKg.getWeightUnit());
    }
    @Test
    void constructor_validGrams() {
        Weight grams = new Weight(1000.0, Weight.WeightUnit.GRAMS);
        assertEquals(1000.0, grams.getValue());
        assertEquals(Weight.WeightUnit.GRAMS, grams.getWeightUnit());
    }
    @Test
    void constructor_validOunces() {
        Weight ounces = new Weight(10.5, Weight.WeightUnit.OUNCES);
        assertEquals(10.5, ounces.getValue());
        assertEquals(Weight.WeightUnit.OUNCES, ounces.getWeightUnit());
    }
    @Test
    void constructor_validPounds() {
        Weight pounds = new Weight(11.0, Weight.WeightUnit.POUNDS);
        assertEquals(11.0, pounds.getValue());
        assertEquals(Weight.WeightUnit.POUNDS, pounds.getWeightUnit());
    }
    @Test
    void constructor_zeroWeight() {
        assertEquals(0.0, zeroGrams.getValue());
    }

    // Constructor tests - INVALID
    @Test
    void constructor_negativeKilograms() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Weight(-5.0, Weight.WeightUnit.KILOGRAMS);
        });
        assertEquals("weight cannot be negative.", exception.getMessage());
    }
    @Test
    void constructor_negativeGrams() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Weight(-100.0, Weight.WeightUnit.GRAMS);
        });
        assertEquals("weight cannot be negative.", exception.getMessage());
    }
    @Test
    void constructor_negativeOunces() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Weight(-20.0, Weight.WeightUnit.OUNCES);
        });
        assertEquals("weight cannot be negative.", exception.getMessage());
    }
    @Test
    void constructor_negativePounds() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Weight(-0.5, Weight.WeightUnit.POUNDS);
        });
        assertEquals("weight cannot be negative.", exception.getMessage());
    }

    // toString() tests
    @Test
    void toString_kilograms() {
        String expected = "Weight: 5.0 kg";
        assertEquals(expected, fiveKg.toString());
    }
    @Test
    void toString_grams() {
        Weight grams = new Weight(1000.0, Weight.WeightUnit.GRAMS);
        assertEquals("Weight: 1000.0 g", grams.toString());
    }
    @Test
    void toString_ounces() {
        Weight ounces = new Weight(10.5, Weight.WeightUnit.OUNCES);
        assertEquals("Weight: 10.5 oz", ounces.toString());
    }
    @Test
    void toString_pounds() {
        Weight pounds = new Weight(11.02, Weight.WeightUnit.POUNDS);
        assertEquals("Weight: 11.02 lb", pounds.toString());
    }

    @Test
    void toString_zeroWeight() {
        assertEquals("Weight: 0.0 g", zeroGrams.toString());
    }

    // Getter tests
    @Test
    void getValueCorrectly() {
        assertEquals(5.0, fiveKg.getValue());
    }
    @Test
    void getWeightUnitCorrectly() {
        assertEquals(Weight.WeightUnit.KILOGRAMS, fiveKg.getWeightUnit());
    }

    // Enum abbreviation tests
    @Test
    void weightUnit_abbreviation_returnsCorrectValue() {
        assertEquals("kg", fiveKg.getWeightUnit().getAbbreviation());
        assertEquals("g", zeroGrams.getWeightUnit().getAbbreviation());
        assertEquals("oz", tenOunces.getWeightUnit().getAbbreviation());
        assertEquals("lb", threePounds.getWeightUnit().getAbbreviation());
    }
}
