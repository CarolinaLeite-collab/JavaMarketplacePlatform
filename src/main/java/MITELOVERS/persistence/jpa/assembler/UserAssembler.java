package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.persistence.jpa.datamodel.UserDataModel;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link User} domain objects
 * and {@link UserDataModel} persistence objects.
 */

@Component
@AllArgsConstructor

public class UserAssembler {

    private final UserFactory _userFactory;


    public UserDataModel domain2DM(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        return new UserDataModel(
                user.identity().toString(),
                user.getName().toString(),
                user.getEmail()

        );
    }


    public User DM2Domain(UserDataModel dm) {
        if (dm == null)
            throw new IllegalArgumentException("UserDataModel cannot be null");

        Email email = new Email(dm.getEmail());
        UserId userId = new UserId(new Email(dm.getId()));
        Name name = new Name(dm.getName());


        User user = _userFactory.createUser(userId, name, null, email, null);

        return user;
    }
}
