package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.valueobject.Weight;
import jakarta.persistence.Embeddable;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link Weight} value object,
 * enabling its persistence as part of a JPA entity.
 */

@Generated
@Getter
@NoArgsConstructor
@Embeddable
public class WeightDataModel {

    private double value;
    private String weightUnit;

    public WeightDataModel(double value, String weightUnit) {
        this.value = value;
        this.weightUnit = weightUnit;
    }
}
