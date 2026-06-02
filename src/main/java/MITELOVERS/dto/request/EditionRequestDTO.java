package MITELOVERS.dto.request;

import MITELOVERS.dto.DimensionDTO;
import MITELOVERS.dto.WeightDTO;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * Data Transfer Object used to expose Edition information in API requests.
 */
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Getter
@Generated
@Builder
public class EditionRequestDTO {

    private String  publicationTypeId;
    private String  publishingCompanyId;
    private Integer publishingYear;
    private String  language;
    // optional fields
    private String identifier;
    private DimensionDTO dimension;
    private WeightDTO weight;
    private Integer numberOfPages;
    private Integer editionNumber;
    private String  binding;

}
