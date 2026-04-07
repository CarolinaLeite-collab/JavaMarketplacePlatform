package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * Represents an ISSN (International Standard Serial Number) for a publication.
 * <p>
 * Implements the {@link EditionId} interface and ensures that the ISSN follows the standard
 * format (four digits, a hyphen, three digits, and a check digit which may be 'X').
 * </p>
 */

public class ISSN implements MagazineId, ValueObject {

    private final String _issn;

    public ISSN(String value) {
        if (value == null || !value.matches("\\d{4}-\\d{3}[\\dX]$")) {
            throw new IllegalArgumentException("Invalid ISSN format");
        }
        _issn = value;
    }

    public String get_issn() {
        return _issn;
    }

    @Override
    public String toString() {
        return _issn;
    }

    @Override
    public String getIdentifier() {
        return _issn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISSN other)) return false;
        return _issn.equals(other._issn);
    }

    @Override
    public int hashCode() {
        return _issn.hashCode();
    }
}
