package MITELOVERS.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

/**
 * Data Transfer Object used to expose Dimension information in API requests and responses.
 */

@Getter
@AllArgsConstructor
@Generated
public class DimensionDTO {

    private final Double width;
    private final Double height;
    private final Double thickness;
    private final String unit;

}
