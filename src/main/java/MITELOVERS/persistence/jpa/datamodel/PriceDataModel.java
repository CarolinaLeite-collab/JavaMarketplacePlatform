package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link MITELOVERS.domain.valueobject.Price} value object,
 * enabling its persistence as part of a JPA entity.
 */
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDataModel {
    private double numericValue;
    private String currency;

}
