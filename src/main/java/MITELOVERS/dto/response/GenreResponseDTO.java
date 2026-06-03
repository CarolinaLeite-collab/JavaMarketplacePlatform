package MITELOVERS.dto.response;

import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose genre information in API responses.
 */
@Generated
@Getter
public class GenreResponseDTO extends RepresentationModel<GenreResponseDTO> {

    private final String genreId;
    private final String genreName;

    public GenreResponseDTO(String genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }
}