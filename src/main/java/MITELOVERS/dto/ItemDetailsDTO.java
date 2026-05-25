package MITELOVERS.dto;

import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object representing detailed information about an item.
 *
 * <p>
 * This DTO aggregates display-ready information about a library item,
 * including its title, author name, publication type, and unique identifier.
 * It is used to transfer item data from the application layer to presentation
 * layers without exposing domain objects.
 * </p>
 */

public class ItemDetailsDTO extends RepresentationModel<ItemDetailsDTO> {

    private final String _title;
    private final String _authorName;
    private final String _publicationType;
    private final String _identifier;

    public ItemDetailsDTO(String Title, String authorName, String publicationType, String identifier) {

        _title = Title;
        _authorName = authorName;
        _publicationType = publicationType;
        _identifier = identifier;

    }

    public String getTitle() { return _title; }
    public String getAuthorName() { return _authorName; }
    public String getPublicationType() { return _publicationType; }
    public String getIdentifier() { return _identifier; }

    @Override
    public String toString() {
        return _publicationType + ": " + _title + ", by " + _authorName + ", " + _identifier;
    }
}
