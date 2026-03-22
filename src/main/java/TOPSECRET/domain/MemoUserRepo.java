package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link User} entities.
 */
public class MemoUserRepo implements IUserRepo{

    private final List<User> _users = new ArrayList<>();
    private final UserFactory _userFactory;

    public MemoUserRepo(UserFactory userFactory) {
        _userFactory = userFactory;
    }

    @Override
    public User registerNewUser(String name, String email) {
        Email newEmail = new Email(email);

        if (userExists(newEmail)) {
            throw new IllegalStateException("User already exists");
        }

        Name newName = new Name(name);
        User newUser = _userFactory.createUser(newName, newEmail);
        _users.add(newUser);
        return newUser;
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(_users);
    }

    private boolean userExists(Email email) {
        for (User user : _users) {
            if (user.hasEmail(email)) {
                return true;
            }
        }
        return false;
    }

}