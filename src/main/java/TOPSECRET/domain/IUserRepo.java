package TOPSECRET.domain;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.valueobject.UserID;

public interface IUserRepo extends IRepository<UserID, User> {

}
