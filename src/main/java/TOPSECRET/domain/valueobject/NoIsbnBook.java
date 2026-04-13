package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

import java.util.UUID;

/**
 * Represents a placeholder identifier for book editions when no real identifier is available.
 * <p>
 * {@code NoIsbnBook} is a concrete implementation of {@link BookId} used in scenarios
 * where a book edition does not have a valid or assigned identifier (Books older then 1970).
 */

public class NoIsbnBook implements BookId, ValueObject {

    private final String _bookInternalId;

    public NoIsbnBook (String bookInternalId){
        if (bookInternalId == null || bookInternalId.isBlank()) {
            throw new IllegalArgumentException("Internal id cannot be null");
        }
        _bookInternalId = bookInternalId;
    }

    public static NoIsbnBook generate () {
        return new NoIsbnBook(UUID.randomUUID().toString());
    }

    @Override
    public String getIdentifier() {
        return _bookInternalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoIsbnBook other)) return false;
        return _bookInternalId.equals(other._bookInternalId);
    }

}
