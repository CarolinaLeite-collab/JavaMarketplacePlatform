package TOPSECRET.domain;

import java.util.List;

public interface IUserRepo {
    User registerNewUser (String name, String email);
    List<User> getAll();
}
