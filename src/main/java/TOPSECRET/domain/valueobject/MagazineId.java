package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * Represents a domain-specific identifier for magazine editions.
 * <p>
 * {@code MagazineId} is a specialization of {@link EditionId}, used to uniquely
 * identify editions of type magazine within the domain.
 * </p>
 * <p>
 * Implementing classes must provide the identifier value through
 * {@link #getIdentifier()}, as defined in {@link EditionId}.
 */

public interface MagazineId extends EditionId, ValueObject {

}
 */

public interface MagazineId extends EditionId, ValueObject {

}
