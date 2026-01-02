package TOPSECRET.domain;

public class Dimension {

    private double width;
    private double height;
    private double thickness;
    private double weight;

    public Dimension(double width, double height, double thickness, double weight) {
        if (width <= 0 || height <= 0 || thickness <= 0 || weight <= 0) {
            throw new IllegalArgumentException("All dimensions and weight must be greater than zero.");
        }
        this.width = width;
        this.height = height;
        this.thickness = thickness;
        this.weight = weight;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getThickness() {
        return thickness;
    }

    public double getWeight() {
        return weight;
    }

    public double calculateVolume() {
        return width * height * thickness;
    }

    @Override
    public String toString() {
        return String.format(
                "Dimensions: %.2f x %.2f x %.2f cm | Weight: %.2f kg",
                width, height, thickness, weight
        );
    }
}