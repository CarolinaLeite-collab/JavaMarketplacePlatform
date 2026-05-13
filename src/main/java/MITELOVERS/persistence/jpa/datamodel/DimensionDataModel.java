package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.valueobject.Dimension;
import jakarta.persistence.Embeddable;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link Dimension} value object,
 * enabling its persistence as part of a JPA entity.
 */

@Generated
@Getter
@NoArgsConstructor
@Embeddable
public class DimensionDataModel {

    private double width;
    private double height;
    private double thickness;
    private String unit;

    public DimensionDataModel(double width, double height, double thickness, String unit) {
        this.width = width;
        this.height = height;
        this.thickness = thickness;
        this.unit = unit;
    }
}
