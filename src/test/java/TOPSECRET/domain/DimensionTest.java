package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensionTest {

    @Test
    void shouldCreateDimensionWithValidValues() {
        // Arrange
        double width = 10;
        double height = 20;
        double thickness = 5;
        DimensionUnit unit = DimensionUnit.CENTIMETERS;

        // Act
        Dimension dimension = new Dimension(width, height, thickness, unit);

        // Assert
        assertNotNull(dimension);
    }

    @Test
    void shouldThrowExceptionWhenWidthIsZero() {
        // Arrange
        double width = 0;
        double height = 20;
        double thickness = 5;
        DimensionUnit unit = DimensionUnit.CENTIMETERS;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Dimension(width, height, thickness, unit);
        });
    }

    @Test
    void shouldThrowExceptionWhenHeightIsZero() {
        // Arrange
        double width = 10;
        double height = 0;
        double thickness = 5;
        DimensionUnit unit = DimensionUnit.CENTIMETERS;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Dimension(width, height, thickness, unit);
        });
    }

    @Test
    void shouldThrowExceptionWhenThicknessIsZero() {
        // Arrange
        double width = 10;
        double height = 20;
        double thickness = 0;
        DimensionUnit unit = DimensionUnit.CENTIMETERS;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Dimension(width, height, thickness, unit);
        });
    }

    @Test
    void get_WidthShouldReturnValuePassedInConstructor() {
        // Arrange
        Dimension dimension = new Dimension(10, 20, 5, DimensionUnit.CENTIMETERS);

        // Act
        double result = dimension.get_width();

        // Assert
        assertEquals(10, result);
    }

    @Test
    void get_HeightShouldReturnValuePassedInConstructor() {
        // Arrange
        Dimension dimension = new Dimension(10, 20, 5, DimensionUnit.CENTIMETERS);

        // Act
        double result = dimension.get_height();

        // Assert
        assertEquals(20, result);
    }

    @Test
    void get_ThicknessShouldReturnValuePassedInConstructor() {
        // Arrange
        Dimension dimension = new Dimension(10, 20, 5, DimensionUnit.CENTIMETERS);

        // Act
        double result = dimension.get_thickness();

        // Assert
        assertEquals(5, result);
    }

    @Test
    void toStringShouldFormatValuesCorrectly() {
        // Arrange
        Dimension dimension = new Dimension(10, 20, 5, DimensionUnit.CENTIMETERS);
        String expected = "Dimensions: 10.00 centimeters, 20.00 centimeters, 5.00 centimeters";

        // Act
        String result = dimension.toString();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void toStringShouldRoundValuesToTwoDecimalPlaces() {
        // Arrange
        Dimension dimension = new Dimension(2.5, 1.234, 0.1, DimensionUnit.INCHES);
        String expected = "Dimensions: 2.50 inches, 1.23 inches, 0.10 inches";

        // Act
        String result = dimension.toString();

        // Assert
        assertEquals(expected, result);
    }
}