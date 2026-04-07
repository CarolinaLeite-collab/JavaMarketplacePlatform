package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;
import TOPSECRET.domain.library.Library;

/**
 * Represents the unique identifier of a {@link Library}.
 * <p>
 * The identifier is derived from the user's {@link Email} address,
 * since each library belongs to exactly one user and a user can only
 * have one library. The format is:
 * <pre>
 * user@example.com → LibraryId("user@example.com")
 * </pre>
 * </p>
 *
 * <p><b>Construction:</b> Can be instantiated directly from an {@link Email},
 * or derived from a {@link UserId} via {@link #fromUserId(UserId)}.</p>
 *
 * <p><b>Equality:</b> Two {@code LibraryId} instances are equal if they
 * wrap the same underlying {@link Email} value.</p>
 */

public final class LibraryId implements DomainId {

    private final Email _email;

    public LibraryId(Email email){

        _email = email;

    }

    public static LibraryId fromUserId(UserId userId){

        return new LibraryId(userId.getEmail());

    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof LibraryId)) return false;
        LibraryId libraryId = (LibraryId) o;
        return _email.equals(libraryId._email);

    }

    @Override
    public int hashCode() {
        return _email.hashCode();
    }

    @Override
    public String toString() {

        return _email.toString();

    }

}
