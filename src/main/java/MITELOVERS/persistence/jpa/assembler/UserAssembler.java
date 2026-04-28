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


    public UserDataModel toDataModel(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        return new UserDataModel(
                user.identity().toString(),
                user.getName().toString(),
                user.getEmail()

        );
    }


    public User toDomain(UserDataModel userDataModel) {
        if (userDataModel == null)
            throw new IllegalArgumentException("UserDataModel cannot be null");

        Email email = new Email(userDataModel.getEmail());
        UserId userId = new UserId(new Email(userDataModel.getId()));
        Name name = new Name(userDataModel.getName());


        User user = _userFactory.createUser(userId, name, null, email, null);

        return user;
    }
}
