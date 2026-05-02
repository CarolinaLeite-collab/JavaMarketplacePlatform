package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.AuthorDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorSpringDataRepo extends JpaRepository<AuthorDataModel, String> {

}
