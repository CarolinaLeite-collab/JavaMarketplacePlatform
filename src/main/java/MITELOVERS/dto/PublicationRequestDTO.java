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

    private final String title;
    private final String authorId;
    private final int releaseYear;
    private final String genreId;

}
