package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link MITELOVERS.domain.valueobject.Price} value object,
 * enabling its persistence as part of a JPA entity.
 */

@Getter
@NoArgsConstructor
@Embeddable
public class PriceDataModel {
    private double value;
    private String currency;

    public PriceDataModel(double value, String currency){
        this.value = value;
        this.currency = currency;
    }

}
