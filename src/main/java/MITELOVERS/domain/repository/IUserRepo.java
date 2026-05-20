package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.UserId;

public interface IUserRepo extends IRepository<UserId, User> {


}
