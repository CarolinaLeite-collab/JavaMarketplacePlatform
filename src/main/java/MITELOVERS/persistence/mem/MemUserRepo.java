package MITELOVERS.persistence.mem;


import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;

import java.util.*;

/**
 * Repository responsible for managing {@link User} entities.
 */
public class MemUserRepo implements IUserRepo {

    private final Map<UserId, User> DATA = new HashMap<UserId, User>();



    @Override
    public User save(User user) {
        DATA.put(user.identity(), user);
        return user;
    }


    @Override
    public Iterable<User> findAll() {
        return DATA.values();
    }

    @Override
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
