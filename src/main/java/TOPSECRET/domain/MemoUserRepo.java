package TOPSECRET.domain;


import TOPSECRET.domain.user.User;
import TOPSECRET.domain.repository.IUserRepo;
import TOPSECRET.domain.valueobject.UserId;
import java.util.*;

/**
 * Repository responsible for managing {@link User} entities.
 */
public class MemoUserRepo implements IUserRepo {

    private final Map<UserId, User> _users = new HashMap<>();

    @Override
    public User save(User user) {
        if (containsOfIdentity(user.identity())) {
            throw new IllegalStateException("User already exists");
        }
        _users.put(user.identity(), user);
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        return List.copyOf(_users.values());
    }

    @Override
    public Optional<User> ofIdentity(UserId userId) {
        return Optional.ofNullable(_users.get(userId));
    }

    @Override
    public boolean containsOfIdentity(UserId userId) {
        return _users.containsKey(userId);
    }
}