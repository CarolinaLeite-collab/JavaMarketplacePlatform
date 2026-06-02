package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose PublishingCompany information in API responses.
 */

@Getter
@Generated
@AllArgsConstructor
public class PublishingCompanyResponseDTO extends RepresentationModel<PublishingCompanyResponseDTO> {

    private String publishingCompanyId;
    private String publishingCompanyName;

}