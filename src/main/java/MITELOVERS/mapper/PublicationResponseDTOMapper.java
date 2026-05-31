package MITELOVERS.mapper;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.dto.response.PublicationResponseDTO;
import org.springframework.stereotype.Component;

/**
 * Assembles Publication domain objects into PublicationResponseDTO instances.
*/

@Component
public class PublicationResponseDTOMapper {

    public PublicationResponseDTO toResponseDTO(Publication publication,
                                                Author author,
                                                Genre genre) {

        return new PublicationResponseDTO(
                    publication.identity().toString(),
                    publication.getTitle().toString(),
                    author.getName().toString(),
                    publication.getReleaseYear().getValue(),
                    genre.getGenre()
        );

    }
}
