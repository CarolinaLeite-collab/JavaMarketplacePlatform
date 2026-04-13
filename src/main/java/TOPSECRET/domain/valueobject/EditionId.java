package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;
import TOPSECRET.ddd.ValueObject;

/**
 * Represents a generic identifier for an edition.
 * <p>
 * Implementing classes must provide the {@link #getIdentifier()} method to return the identifier value.
 * </p>
 */

public interface EditionId extends ValueObject, DomainId {

    String getIdentifier();

}