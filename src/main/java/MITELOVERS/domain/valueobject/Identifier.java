package MITELOVERS.domain.valueobject;

/**
 * Identifier interface:
 * <p>
 * may be an {@link ISBN} (for BOOK`s published during or after 1970) or an {@link ISSN} (for MAGAZINEs)
 * </p>
 */

public interface Identifier {
    public String getIdentifier();
}
