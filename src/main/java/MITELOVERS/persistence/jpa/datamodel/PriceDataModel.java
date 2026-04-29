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
    private double numericValue;
    private String currency;

    public PriceDataModel(double numericValue, String currency){
        this.numericValue = numericValue;
        this.currency = currency;
    }
}
