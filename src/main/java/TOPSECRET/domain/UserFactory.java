package TOPSECRET.domain;


import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.valueobject.Phone;

/**
 * Factory responsible for creating {@link User} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link NullPointerException}.
 */


public class UserFactory {

    public User createUser(ValueObject.Name name, Email email) {
        return new User(name, email);
    }

    public User createUserTypeB(ValueObject.Name name, Address address, Email email, Phone phone) {
        return new User (name, address, email, phone);
    }
}