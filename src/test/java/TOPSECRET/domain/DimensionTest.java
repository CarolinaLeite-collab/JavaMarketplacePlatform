package TOPSECRET.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensionTest {
    @Test
    void testDimensions() {
        // arrange
        Dimension dim = new Dimension(10, 20, 3, 0.5);

        // act
        double width = dim.get_width();
        double height = dim.get_height();
        double thickness = dim.get_thickness();
        double weight = dim.get_weight();

        // assert
        assertEquals(10, width);
        assertEquals(20, height);
        assertEquals(3, thickness);
        assertEquals(0.5, weight);
    }

    @Test
    void negative_width() {
        // arrange
        double width = -1;

        // act & assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(width, 20, 3, 0.5));
    }

    @Test
    void zero_width() {
        // arrange
        double width = 0;

        // act & assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(width, 20, 3, 0.5));
    }

    @Test
    void negative_height() {
        // arrange
        double height = -5;

        // act & assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(10, height, 3, 0.5));
    }

    @Test
    void zero_height() {
        // arrange
        double height = 0;

        // act & assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(10, height, 3, 0.5));
    }

    @Test
    void negative_thickness() {
        // arrange
        double thickness = -2;

        // act & assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(10, 20, thickness, 0.5));
    }

    @Test
    void zero_thickness() {
        // arrange
        double thickness = 0;

        // act & assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(10, 20, thickness, 0.5));
    }

    @Test
    void negative_weight() {
        // arrange
        double weight = -0.1;

        // act & assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(10, 20, 3, weight));
    }

    @Test
    void zero_weight() {
        // arrange
        double weight = 0;

        // act & assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(10, 20, 3, weight));
    }
}