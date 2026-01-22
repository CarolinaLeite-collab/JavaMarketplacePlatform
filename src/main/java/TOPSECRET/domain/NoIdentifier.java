package TOPSECRET.domain;

/**
 * Represents a placeholder identifier when no actual identifier is available.
 * <p>
 * Implements the {@link Identifier} interface and always returns an empty string for {@link #getIdentifier()}.
 * </p>
 */

public class NoIdentifier implements Identifier {
    @Override
    public String getIdentifier() {
        return "";
    }
}
