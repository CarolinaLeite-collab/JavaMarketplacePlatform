package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Data model object representing {@link MITELOVERS.domain.valueobject.Price} value object,
 * enabling its persistence as part of a JPA entity.
 */

@Generated
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDataModel {
    private double numericValue;
    private String currency;

}
