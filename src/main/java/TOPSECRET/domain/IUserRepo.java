package TOPSECRET.domain;

import java.util.List;
import TOPSECRET.domain.valueobject.UserID;

public interface IUserRepo {
    boolean save(User user);
    boolean containsOfIdentity(UserID userId);
    List<User> getAll();
}
