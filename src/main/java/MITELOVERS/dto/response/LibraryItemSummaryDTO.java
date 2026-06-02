package MITELOVERS.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose a summary of an item in the user's library.
 * Contains only the title and picture for initial display purposes.
 * Includes a self link pointing to the full item details endpoint.
 */

@Getter
@AllArgsConstructor
public class LibraryItemSummaryDTO extends RepresentationModel<LibraryItemSummaryDTO> {

    private String itemId;
    private String title;
    private String picture;
}
