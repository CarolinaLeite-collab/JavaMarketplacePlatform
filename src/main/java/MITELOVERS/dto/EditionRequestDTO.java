package MITELOVERS.dto;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;

/**
 * Data Transfer Object used to expose Edition information in API requests.
 */

@Getter
@Generated
@Builder
public class EditionRequestDTO  {

    private final String  publicationTypeId;
    private final String  publishingCompanyId;
    private final Integer publishingYear;
    private final String  language;
    // optional fields
    private final String identifier;
    private final DimensionDTO dimension;
    private final WeightDTO weight;
    private final Integer numberOfPages;
    private final Integer editionNumber;
    private final String  binding;

}
