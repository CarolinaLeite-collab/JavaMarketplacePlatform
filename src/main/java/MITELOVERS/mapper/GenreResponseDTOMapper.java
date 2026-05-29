package MITELOVERS.mapper;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.dto.GenreResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembles Genre domain objects into GenreResponseDTO instances.
 */

@Component
public class GenreResponseDTOMapper implements RepresentationModelAssembler<Genre, GenreResponseDTO> {

    @Override
    public GenreResponseDTO toModel(Genre genre) {
        return new GenreResponseDTO(
                genre.identity().toString(),
                genre.getGenre()
        );
    }
}
