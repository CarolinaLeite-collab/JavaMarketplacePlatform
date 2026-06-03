package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;


/**
 * Assembles PublicationType domain objects into PublicationTypeResponseDTO instances.
 */

@Generated
@Getter
@AllArgsConstructor
public class PublicationTypeResponseDTO  extends RepresentationModel<PublicationResponseDTO>  {

    private String publicationTypeId;

}
