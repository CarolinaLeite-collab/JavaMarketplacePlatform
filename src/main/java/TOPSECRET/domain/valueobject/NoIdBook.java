package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * Represents a placeholder identifier for Books when no actual identifier is available.
 * <p>
 * Implements the {@link BookId,} interface and always returns an empty string for {@link #getIdentifier()}.
 * </p>
 */

public class NoIdBook implements BookId, ValueObject {

    @Override
    public String getIdentifier() {
        return "";
    }
}
