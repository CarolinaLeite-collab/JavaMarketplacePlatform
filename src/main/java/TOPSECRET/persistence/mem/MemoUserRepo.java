package TOPSECRET.persistence.mem;


import TOPSECRET.domain.user.User;
import TOPSECRET.domain.repository.IUserRepo;
import TOPSECRET.domain.user.UserFactory;
import TOPSECRET.domain.valueobject.*;

import java.util.*;

/**
 * Repository responsible for managing {@link User} entities.
 */
public class MemoUserRepo implements IUserRepo {

    private final UserFactory _userFactory;
    private final Map<UserId, User> DATA = new HashMap<UserId, User>();


    public MemoUserRepo(UserFactory userFactory){
        _userFactory = userFactory;
    }

    @Override
    public User save(User user) {
        DATA.put(user.identity(), user);
        return user;
    }

    @Override
    public User addUser(Name name, Address address, Email email, Phone phone) {
        User newUser = _userFactory.createUser(name, address, email, phone);

        if (containsOfIdentity(newUser.identity())) {
            throw new IllegalStateException("User already exists");
        }
        return save(newUser);
    }

    @Override
    public Iterable<User> findAll() {
        return DATA.values();
    }

    public List<UserId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }


    @Override
    public Optional<User> ofIdentity(UserId userId) {
        if (!containsOfIdentity(userId)) {
            return Optional.empty();
        } else  {
            return Optional.of(DATA.get(userId));
        }
    }

    @Override
    public boolean containsOfIdentity(UserId userId) {
        return DATA.containsKey(userId);
    }
}