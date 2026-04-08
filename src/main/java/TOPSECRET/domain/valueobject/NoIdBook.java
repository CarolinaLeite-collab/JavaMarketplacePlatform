package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

/**
 * Represents a placeholder identifier for book editions when no real identifier is available.
 * <p>
 * {@code NoIdBook} is a concrete implementation of {@link BookId} used in scenarios
 * where a book edition does not have a valid or assigned identifier (Books older then 1970).
 */

public class NoIdBook implements BookId, ValueObject {

    @Override
    public String getIdentifier() {
        return "";
    }

}
