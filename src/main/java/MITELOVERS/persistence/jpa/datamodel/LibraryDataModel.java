package MITELOVERS.persistence.jpa.datamodel;

import java.util.List;

/**
 * Data Transfer Object representing a user's Library.
 *
 * <p>
 * This DTO exposes a library's identifier and the list of item identifiers
 * it contains. It is used to transfer library data from the application layer
 * to presentation layers without exposing domain objects.
 * </p>
 */

public class LibraryDataModel {

    private final String _libraryId;
    private final List<String> _itemIds;

    public LibraryDataModel(String libraryId, List<String> itemIds) {
        _libraryId = libraryId;
        _itemIds = List.copyOf(itemIds);
    }

    public String getLibraryId() {
        return _libraryId;
    }

    public List<String> getItemIds() {
        return _itemIds;
    }

    @Override
    public String toString() {
        return "LibraryDTO{id=" + _libraryId + ", items=" + _itemIds + "}";
    }
}
