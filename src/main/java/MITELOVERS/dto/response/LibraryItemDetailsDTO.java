package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object representing detailed information about an item.
 *
 * <p>
 * This DTO aggregates display-ready information about a library item,
 * including its author name, publication type, and unique identifier.
 * It is used to transfer item data from the application layer to presentation
 * layers without exposing domain objects.
 * </p>
 */

@Getter
@Generated
@AllArgsConstructor
public class LibraryItemDetailsDTO extends RepresentationModel<LibraryItemDetailsDTO> {

    private final String authorName;
    private final String identifier;
    private final String publicationType;

    @Override
    public String toString() {
        return publicationType + ":  by " + authorName + ", " + identifier;
    }
}

