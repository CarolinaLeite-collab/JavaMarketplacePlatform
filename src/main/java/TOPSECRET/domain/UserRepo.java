package TOPSECRET.domain;

import TOPSECRET.ddd.ValueObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link User} entities.
 */
public class UserRepo {

    private final List<User> _users = new ArrayList<>();
    private final UserFactory _userFactory;

    public UserRepo(UserFactory userFactory) {
        _userFactory = userFactory;
    }

    public User registerNewUser(String name, String email) {

        if (userExists(email)) {
            throw new IllegalStateException("User already exists");
        }

        ValueObject.Name newUserName = new ValueObject.Name(name);
        Email newUserEmail = new Email(email);

        User newUser = _userFactory.createUser(newUserName, newUserEmail);
        _users.add(newUser);

        return newUser;
    }

    private boolean userExists(String email) {
        String emailFormat = email.trim().toLowerCase();

        for (User user : _users) {
            if (emailFormat.equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public List<User> getAll() {
        return List.copyOf(_users);
    }
}