package TOPSECRET.domain;


import TOPSECRET.domain.User.User;
import TOPSECRET.domain.repository.IUserRepo;
import TOPSECRET.domain.valueobject.UserID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository responsible for managing {@link User} entities.
 */
public class MemoUserRepo implements IUserRepo {

    private final List<User> _users = new ArrayList<>();

    @Override
    public User save(User user) {
        if (containsOfIdentity(user.identity())) {
            throw new IllegalStateException("User already exists");
        }
        _users.add(user);
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        return List.copyOf(_users);
    }

    @Override
    public Optional<User> ofIdentity(UserID userId) {
        return _users.stream()
                .filter(u -> u.identity().equals(userId))
                .findFirst();
    }

    @Override
    public boolean containsOfIdentity(UserID userId) {
        return _users.stream()
                .anyMatch(u -> u.identity().equals(userId));
    }
}