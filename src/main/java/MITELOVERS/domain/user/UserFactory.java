package MITELOVERS.domain.user;

import MITELOVERS.domain.valueobject.*;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link User} instances.
 * <p>
 * Any exception thrown during the creation process is wrapped
 * into an {@link NullPointerException}.
 */

@Component
public class UserFactory {

    public User createUser(Name name, Address address, Email email, Phone phone) {

        return new User(name, address, email, phone);
    }

    public User createUser(UserId userId, Name name, Address address, Email email, Phone phone) {

        return new User(userId, name, address, email, phone );
    }
}
