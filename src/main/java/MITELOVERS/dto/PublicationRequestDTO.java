package MITELOVERS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to receive publication data from client requests.
 * </p>
 */

@Getter
@AllArgsConstructor
public class PublicationRequestDTO {

    private final String _title;
    private final String _authorId;
    private final int _releaseYear;
    private final String _genreId;

}
