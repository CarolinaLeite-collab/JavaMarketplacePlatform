package MITELOVERS.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

/**
 * Data Transfer Object used to expose Weight information in API responses and requests.
 */

@Getter
@Generated
@AllArgsConstructor
public class WeightDTO {

    private final Double value;
    private final String unit;

}
