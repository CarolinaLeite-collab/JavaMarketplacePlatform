package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.CityDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICitySpringDataRepo extends JpaRepository<CityDataModel, String> {
}
