package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.*;

import java.util.List;

public interface IUserRepo extends IRepository<UserId, User> {
    User addUser(Name name, Address address, Email email, Phone phone);
    List<UserId> findAllKeys();

}
