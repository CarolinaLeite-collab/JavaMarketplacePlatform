package MITELOVERS.mapper;

import MITELOVERS.domain.publicationtype.PublicationType;
import MITELOVERS.dto.response.PublicationTypeResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Assembles PublicationType domain objects into PublicationTypeResponseDTO instances.
 */

@Component
public class PublicationTypeResponseDTOMapper {

    public PublicationTypeResponseDTO toResponseDTO(PublicationType publicationType){

        return new PublicationTypeResponseDTO(
                publicationType.identity().toString()
        );


    }
}
