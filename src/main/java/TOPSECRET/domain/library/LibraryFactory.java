package TOPSECRET.domain.library;

import TOPSECRET.domain.valueobject.LibraryId;

/**
 * Factory responsible for creating {@link Library} instances.
 * <p>
 * @throws IllegalArgumentException if the user is null, as enforced by {@link Library}'s constructor.
 */
public class LibraryFactory {

    public Library createLibrary(LibraryId libraryId) {
        return new Library(libraryId);
    }
}
