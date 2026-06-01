package MITELOVERS.dto.response;

import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose author information in API responses.
 */

@Generated
@Getter
public class AuthorResponseDTO extends RepresentationModel<AuthorResponseDTO> {

    private final String authorId;
    private final String authorName;

    public AuthorResponseDTO(String authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }
}

