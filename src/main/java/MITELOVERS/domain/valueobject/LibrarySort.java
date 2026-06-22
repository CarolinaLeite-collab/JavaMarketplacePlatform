package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

/**
 * Defines the supported sorting criteria for items in a user's library.
 *
 * <p>{@link #NONE} represents the absence of a sorting criterion.</p>
 */

public enum LibrarySort implements ValueObject {

    TITLE,
    AUTHOR,
    PUBLICATION_TYPE,
    IDENTIFIER,
    NONE;
}
