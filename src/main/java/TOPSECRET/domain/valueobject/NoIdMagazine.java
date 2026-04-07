package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * Represents a placeholder identifier for magazine editions when no real identifier is available.
 * <p>
 * {@code NoIdMagazine} is a concrete implementation of {@link MagazineId} used in scenarios
 * where a magazine edition does not have a valid or assigned identifier (magazines older then 1976).
 */

public class NoIdMagazine implements MagazineId, ValueObject {

    @Override
    public String getIdentifier() {
        return "";
    }

}
