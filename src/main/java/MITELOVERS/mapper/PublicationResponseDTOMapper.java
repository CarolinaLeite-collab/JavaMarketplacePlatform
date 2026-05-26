package MITELOVERS.mapper;

import MITELOVERS.controllers.rest.PublicationRestController;
import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.dto.PublicationResponseDTO;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembles Publication domain objects into PublicationResponseDTO instances.
*/

@Component
public class PublicationResponseDTOMapper {

    public PublicationResponseDTO toResponseDTO(Publication publication,
                                                Author author,
                                                Genre genre) {

        PublicationResponseDTO dto = new PublicationResponseDTO(
                    publication.getTitle().toString(),
                    author.getName().toString(),
                    publication.getReleaseYear().getValue(),
                    genre.getGenre()
        );

        dto.add(
                linkTo(
                        methodOn(PublicationRestController.class)
                                .getPublicationById(
                                        publication.identity().toString()
                                )
                ).withSelfRel()
        );
        return dto;
    }
}
