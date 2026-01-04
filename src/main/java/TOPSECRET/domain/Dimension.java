package TOPSECRET.domain;

public class Dimension {

    private double _width;
    private double _height;
    private double _thickness;
    private double _weight;

    public Dimension(double width, double height, double thickness, double weight) {
        if (width <= 0 || height <= 0 || thickness <= 0 || weight <= 0) {
            throw new IllegalArgumentException("All dimensions and weight must be greater than zero.");
        }
        this._width = width;
        this._height = height;
        this._thickness = thickness;
        this._weight = weight;
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

    public double get_weight() {
        return _weight;
    }

    public double calculateVolume() {
        return _width * _height * _thickness;
    }
}