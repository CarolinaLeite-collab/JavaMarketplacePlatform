package MITELOVERS.domain.user;

import MITELOVERS.domain.valueobject.Address;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.Phone;

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
