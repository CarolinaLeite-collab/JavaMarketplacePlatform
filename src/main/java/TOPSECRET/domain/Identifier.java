package TOPSECRET.domain;

/**
 * Represents a generic identifier for a publication or related entity.
 * <p>
 * Implementing classes must provide the {@link #getIdentifier()} method to return the identifier value.
 * </p>
 */

public interface Identifier {

    public String getIdentifier();
}
