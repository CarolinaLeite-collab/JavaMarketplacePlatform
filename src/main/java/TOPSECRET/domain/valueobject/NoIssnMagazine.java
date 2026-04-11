package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

import java.util.UUID;

/**
 * Represents a placeholder identifier for magazine editions when no real identifier is available.
 * <p>
 * {@code NoIssnMagazine} is a concrete implementation of {@link MagazineId} used in scenarios
 * where a magazine edition does not have a valid or assigned identifier (magazines older than 1976).
 * </p>
 */

public class NoIssnMagazine implements MagazineId, ValueObject {

    private final String _magazineInternalId;

    public NoIssnMagazine(String magazineInternalId) {
        if (magazineInternalId == null || magazineInternalId.isBlank()) {
            throw new IllegalArgumentException("Internal id cannot be null");
        }
        _magazineInternalId = magazineInternalId;
    }

    public static NoIssnMagazine generate() {
        return new NoIssnMagazine(UUID.randomUUID().toString());
    }

    @Override
    public String getIdentifier() {
        return _magazineInternalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoIssnMagazine other)) return false;
        return _magazineInternalId.equals(other._magazineInternalId);
    }

}