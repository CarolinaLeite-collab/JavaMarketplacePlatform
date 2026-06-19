package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose a summary of an item in the user's library.
 * Contains the display and sorting fields needed by the library list.
 * Includes a self link pointing to the full item details endpoint.
 */

@Getter
@AllArgsConstructor
public class LibraryItemSummaryDTO extends RepresentationModel<LibraryItemSummaryDTO> {

    private String itemId;
    private String title;
    private String authorName;
    private String publicationType;
    private String identifier;
    private String picture;

    public LibraryItemSummaryDTO(String itemId, String title, String picture) {
        this(itemId, title, null, null, null, picture);
    }
}
