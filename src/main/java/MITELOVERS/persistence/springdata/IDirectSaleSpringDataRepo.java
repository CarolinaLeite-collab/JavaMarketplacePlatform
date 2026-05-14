package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDirectSaleSpringDataRepo extends JpaRepository<DirectSaleDataModel, String> {
}
