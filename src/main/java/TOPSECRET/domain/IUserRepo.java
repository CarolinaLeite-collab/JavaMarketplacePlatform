package TOPSECRET.domain;

import java.util.List;
import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.valueobject.UserID;

public interface IUserRepo extends IRepository<UserID, User> {

}
