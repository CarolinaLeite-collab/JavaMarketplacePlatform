package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.EditionDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEditionSpringDataRepo extends JpaRepository<EditionDataModel, String> {

}
