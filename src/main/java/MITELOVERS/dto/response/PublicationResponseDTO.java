package MITELOVERS.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose publication information in API responses.
 * </p>
 */

@Getter
@AllArgsConstructor
public class PublicationResponseDTO extends RepresentationModel<PublicationResponseDTO> {

    @JsonIgnore
    private String publicationId;
    private String title;
    private String authorName;
    private int releaseYear;
    private String genreName;

}
