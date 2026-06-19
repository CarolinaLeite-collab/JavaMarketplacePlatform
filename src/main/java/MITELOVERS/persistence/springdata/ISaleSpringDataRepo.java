package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISaleSpringDataRepo extends JpaRepository<SaleDataModel,String> {
}
