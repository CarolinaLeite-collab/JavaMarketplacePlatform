package MITELOVERS.dto;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose genre information in API responses.
 */
@Getter
public class GenreResponseDTO extends RepresentationModel<GenreResponseDTO> {

    private final String _genreId;
    private final String _genreName;

    public GenreResponseDTO(String genreId, String genreName) {
        this._genreId = genreId;
        this._genreName = genreName;
    }
}