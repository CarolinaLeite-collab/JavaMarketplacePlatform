package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.*;

public interface IUserRepo extends IRepository<UserId, User> {
    User addUser(Name name, Address address, Email email, Phone phone);

}
