package MITELOVERS.mapper;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Maps {@link PublicationType} domain objects into
 * {@link PublicationTypeResponseDTO} instances.
 */

@Component
public class PublicationTypeResponseDTOMapper implements RepresentationModelAssembler<PublicationType, PublicationTypeResponseDTO> {

    @Override
    public PublicationTypeResponseDTO toModel(PublicationType publicationType) {

        return new PublicationTypeResponseDTO(
                publicationType.identity().toString()
        );
    }
}
