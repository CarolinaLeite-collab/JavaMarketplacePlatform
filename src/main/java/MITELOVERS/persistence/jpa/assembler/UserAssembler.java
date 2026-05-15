package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.user.UserFactory;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.datamodel.UserDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link User} domain objects
 * and {@link UserDataModel} persistence objects.
 * <p>
 * Handles transformation of domain value objects to persistence-friendly
 * formats and reconstruction of domain objects from stored data.
 */

@Component
@AllArgsConstructor

public class UserAssembler {

    private final UserFactory _userFactory;


    public UserDataModel toDataModel(User user) {

        return new UserDataModel(
                user.identity().toString(),
                user.getName().toString(),
                user.getEmail()

        );
    }


    public User toDomain(UserDataModel userDataModel) {

        Email email = new Email(userDataModel.getEmail());
        UserId userId = new UserId(new Email(userDataModel.getId()));
        Name name = new Name(userDataModel.getName());


        User user = _userFactory.createUser(userId, name, null, email, null);

        return user;
    }
}
