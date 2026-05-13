package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.UserDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserSpringDataRepo extends JpaRepository<UserDataModel, String> {
}
