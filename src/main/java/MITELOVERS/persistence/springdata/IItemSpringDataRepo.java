package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemSpringDataRepo extends JpaRepository<ItemDataModel, String> {
}