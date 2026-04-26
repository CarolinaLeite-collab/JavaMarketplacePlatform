package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link MITELOVERS.domain.valueobject.Price} value object,
 * enabling its persistence as part of a JPA entity.
 */

@Getter
@NoArgsConstructor
@Embedded
public class PriceDataModel {
    private String value;
    private String currency;

    public PriceDataModel(String value, String currency){
        this.value = value;
        this.currency = currency;
    }

}
