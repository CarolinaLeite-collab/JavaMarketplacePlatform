package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object used to expose item information in API responses.
 * Includes denormalised fields from Edition and Publication
 * so the client has full context without additional requests.
 */

@Getter
@AllArgsConstructor
public class ItemResponseDTO extends RepresentationModel<ItemResponseDTO> {

    // Item fields
    private String itemId;
    private String condition;
    private String description;
    private String saleStatus;

    // Edition fields
    private String editionId;
    private String identifier;
    private String language;
    private int publishingYear;
    private String publicationTypeName;

    // Publication fields (via Edition → publicationId)
    private String title;
    private String authorName;
    private int releaseYear;
    private String genreName;

}
