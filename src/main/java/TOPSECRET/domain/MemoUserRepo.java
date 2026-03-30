package TOPSECRET.domain;


import TOPSECRET.domain.valueobject.UserID;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link User} entities.
 */
// MemoUserRepo.java
public class MemoUserRepo implements IUserRepo {

    private final List<User> _users = new ArrayList<>();

    @Override
    public boolean save(User user) {
        if (containsOfIdentity(user.identity())) {
            return false;
        }
        _users.add(user);
        return true;
    }

    @Override
    public boolean containsOfIdentity(UserID userId) {
        return userExists(userId);
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(_users);
    }

    private boolean userExists(UserID userId) {
        for (User user : _users) {
            if (user.identity().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}