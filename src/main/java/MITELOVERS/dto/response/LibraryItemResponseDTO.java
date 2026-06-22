package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object representing a library item exposed via the REST API.
 *
 * <p>
 * Carries the display fields for a library item: title, author, publication
 * type, identifier and cover picture. Used as the response shape for both
 * the library list and item detail endpoints.
 * </p>
 */

@Getter
@Generated
@AllArgsConstructor
public class LibraryItemResponseDTO extends RepresentationModel<LibraryItemResponseDTO> {

    private String itemId;
    private final String title;
    private final String authorName;
    private final String publicationType;
    private final String identifier;
    private final String picture;


}
