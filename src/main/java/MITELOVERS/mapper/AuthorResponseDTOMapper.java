package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.dto.response.AuthorResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembles Author domain objects into AuthorResponseDTO instances.
 */

@Component
public class AuthorResponseDTOMapper implements RepresentationModelAssembler<Author, AuthorResponseDTO> {

    @Override
    public AuthorResponseDTO toModel(Author author) {
        return new AuthorResponseDTO(
                author.identity().toString(),
                author.getName().toString()
        );
    }
}
