package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

import java.util.Locale;

/**
 * Represents the physical dimensions of an object, including width, height, and thickness.
 * <p>
 * Ensures that all dimension values are greater than zero and allows specification of a {@link DimensionUnit}.
 * Provides getters for each dimension and a formatted string representation via {@link #toString()}.
 * </p>
 */

public class Dimension implements ValueObject {

    private double _width;
    private double _height;
    private double _thickness;
    private DimensionUnit _unit;


    public Dimension(double width, double height, double thickness, DimensionUnit unit) {
        if (width <= 0 || height <= 0 || thickness <= 0) {
            throw new IllegalArgumentException("All dimensions must be greater than zero.");
        }
        _width = width;
        _height = height;
        _thickness = thickness;
        _unit = unit;
    }

    public double get_width() {
        return _width;
    }

    public double get_height() {
        return _height;
    }

    public double get_thickness() {
        return _thickness;
    }

    @Override
    public String toString() {
        return String.format(
                Locale.US, //garantir que o separador decimal seja sempre (.) independente da região.
                "Dimensions: %.2f %s, %.2f %s, %.2f %s",
                _width, _unit,
                _height, _unit,
                _thickness, _unit
        );
    }
}