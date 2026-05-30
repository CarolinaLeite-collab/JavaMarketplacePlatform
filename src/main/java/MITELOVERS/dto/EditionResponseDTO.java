package MITELOVERS.dto;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose Edition information in API responses.
 */

@Getter
@Generated
@Builder
public class EditionResponseDTO extends RepresentationModel<EditionResponseDTO> {


    private final String publicationTypeId;
    private final String identifier;
    private final String publicationId;
    private final String publishingCompanyId;
    private final int publishingYear;
    private final String language;
    private final String editionId;
    // optional fields
    private final DimensionDTO dimension;
    private final WeightDTO weight;
    private final Integer numberOfPages;
    private final Integer editionNumber;
    private final String binding;

}
