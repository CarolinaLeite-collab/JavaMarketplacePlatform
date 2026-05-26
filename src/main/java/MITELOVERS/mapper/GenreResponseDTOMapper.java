package MITELOVERS.mapper;

import MITELOVERS.controllers.rest.GenreRestController;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.dto.GenreResponseDTO;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembles Genre domain objects into GenreResponseDTO instances.
 */

@Component
public class GenreResponseDTOMapper {

    public GenreResponseDTO toResponseDTO(Genre genre) {
        GenreResponseDTO dto = new GenreResponseDTO(
                genre.identity().toString(),
                genre.getGenre()
        );

        dto.add(
                linkTo(
                        methodOn(GenreRestController.class)
                                .getGenreById(
                                        genre.identity().toString()
                                )
                ).withSelfRel()
        );

        return dto;
    }
}
