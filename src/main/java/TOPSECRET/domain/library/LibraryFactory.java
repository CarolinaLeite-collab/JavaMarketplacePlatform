package TOPSECRET.domain.library;

import TOPSECRET.domain.valueobject.UserId;

/**
 * Factory responsible for creating instances of {@link Library}.
 *
 * <p>
 * This factory encapsulates the creation logic of {@link Library} aggregates,
 * ensuring that they are instantiated in a consistent and valid state.
 * </p>
 *
 * <p>
 * A {@link Library} is created based on a {@link UserId}, and validation rules
 * (such as non-null constraints) are enforced by the {@link Library} constructor.
 * </p>
 */
public class LibraryFactory {

    public Library createLibrary(UserId userId) {

        return new Library(userId);

    }
}
