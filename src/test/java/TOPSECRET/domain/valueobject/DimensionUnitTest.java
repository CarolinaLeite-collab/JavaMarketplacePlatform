package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensionUnitTest {

    @Test
    void allDimensionUnitConstantsExist(){
        DimensionUnit[] dimensionUnits = DimensionUnit.values();
        assertEquals(2, dimensionUnits.length);

        for (DimensionUnit dimensionUnit : dimensionUnits){
            assertNotNull(dimensionUnit);
            assertNotNull(dimensionUnit.toString());
            assertEquals(DimensionUnit.valueOf(dimensionUnit.name()), dimensionUnit);
        }

    }

    @Test
    void toStringRoundtripAllDimensionUnits() {
        // toString() → fromString() → original DimensionUnit
        // E.g., INCHES -> "inches" -> INCHES
        for (DimensionUnit dimensionUnit: DimensionUnit.values()) {
            assertEquals(dimensionUnit, DimensionUnit.fromString(dimensionUnit.toString()));
        }
    }


    @Test
    void differentDimensionUnitsAreNotEqual() {
        assertNotEquals(DimensionUnit.CENTIMETERS, DimensionUnit.INCHES);
    }

    @Test
    void fromStringHandlesDifferentValidInputs() {
        assertEquals(DimensionUnit.CENTIMETERS, DimensionUnit.fromString(" centimetres "));
        assertEquals(DimensionUnit.CENTIMETERS, DimensionUnit.fromString("CEnt"));
        assertEquals(DimensionUnit.INCHES, DimensionUnit.fromString("INchEs"));
        assertEquals(DimensionUnit.INCHES, DimensionUnit.fromString("ins"));
    }

    @Test
    void fromStringAcceptsInputsWithDot() {
        assertEquals(DimensionUnit.CENTIMETERS, DimensionUnit.fromString("cm."));
        assertEquals(DimensionUnit.INCHES, DimensionUnit.fromString("in."));
    }

    @Test
    void fromStringInvalidInputs(){
        assertThrows(IllegalArgumentException.class, () -> DimensionUnit.fromString(""));
        assertThrows(IllegalArgumentException.class, () -> DimensionUnit.fromString(" "));
        assertThrows(IllegalArgumentException.class, () -> DimensionUnit.fromString(null));
        assertThrows(IllegalArgumentException.class, () -> DimensionUnit.fromString("invalid"));
        assertThrows(IllegalArgumentException.class, () -> DimensionUnit.fromString("millimeters"));
    }

}