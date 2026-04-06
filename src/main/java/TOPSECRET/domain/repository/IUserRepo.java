package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.UserId;

public interface IUserRepo extends IRepository<UserId, User> {

}
