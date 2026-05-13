package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.DomainId;

import java.util.UUID;

/**
 * Represents a unique internal identity for an {@link MITELOVERS.domain.edition.Edition}.
 * <p>
 * {@code EditionId} is a {@link DomainId} generated automatically and used as the
 * aggregate identity. It is independent of external identifiers such as ISBN or ISSN,
 * which are modeled separately as {@link Identifier}.
 */

public class EditionId implements DomainId {

    private final String _eId;

    public EditionId(){
        _eId = "E-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    }

    public EditionId(String id) {

        if (id == null) {
            throw new IllegalArgumentException("EditionId cannot be null");
        }

        _eId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EditionId other)) return false;
        return _eId.equals(other._eId);
    }

    @Override
    public int hashCode() {
        return _eId.hashCode();
    }

    @Override
    public String toString() {
        return _eId;
    }

}
