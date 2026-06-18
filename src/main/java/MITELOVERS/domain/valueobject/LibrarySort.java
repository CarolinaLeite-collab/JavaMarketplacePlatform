package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Defines the supported sorting criteria for items in a user's library.
 *
 * <p>It converts HTTP sorting values and their aliases into domain values.
 * {@link #NONE} represents the absence of a valid sorting criterion.</p>
 */

public enum LibrarySort implements ValueObject {

    TITLE,
    AUTHOR,
    PUBLICATION_TYPE,
    IDENTIFIER,
    NONE;


    public static LibrarySort from(String value) {
        if (value == null || value.isBlank()) {
            return NONE;
        }

        return switch (value.toLowerCase()) {

            case "title" -> TITLE;
            case "author", "authorname" -> AUTHOR;
            case "publicationtype", "type", "publication_type" -> PUBLICATION_TYPE;
            case "identifier", "isbn", "issn", "isbn-issn" -> IDENTIFIER;
            default -> NONE;
        };
    }
}
