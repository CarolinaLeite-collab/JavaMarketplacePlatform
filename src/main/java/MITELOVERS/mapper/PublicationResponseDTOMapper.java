package MITELOVERS.mapper;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.dto.response.PublicationResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Maps {@link Publication} domain objects into
 * {@link PublicationResponseDTO} instances.
 */

@Component
public class PublicationResponseDTOMapper implements RepresentationModelAssembler<Publication, PublicationResponseDTO> {

    public PublicationResponseDTO toModel(Publication publication) {

        return new PublicationResponseDTO(
                publication.identity().toString(),
                publication.getTitle().toString(),
                publication.getAuthorId().toString(),
                publication.getReleaseYear().getValue(),
                publication.getGenreId().toString()
        );

    }
}
