package TOPSECRET.domain;

import TOPSECRET.domain.user.User;

/**
 * Factory responsible for creating {@link Library} instances.
 * <p>
 * @throws IllegalArgumentException if the user is null, as enforced by {@link Library}'s constructor.
 */
public class LibraryFactory {

    public Library createLibrary(User user) {
        return new Library(user);
    }
}
