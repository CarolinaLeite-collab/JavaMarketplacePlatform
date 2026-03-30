package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * Represents a placeholder identifier when no actual identifier is available.
 * <p>
 * Implements the {@link EditionId} interface and always returns an empty string for {@link #getIdentifier()}.
 * </p>
 */

public class NoEditionId implements EditionId, ValueObject {
    @Override
    public String getIdentifier() {
        return "";
    }
}
