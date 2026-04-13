package TOPSECRET.domain.user;

import TOPSECRET.domain.valueobject.*;

/**
 * Factory responsible for creating {@link User} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link NullPointerException}.
 */


public class UserFactory {

    public User createUser(Name name, Address address, Email email, Phone phone) {

        return new User(name, address, email, phone);
    }
}