package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface IItemSpringDataRepo extends JpaRepository<ItemDataModel, String> {

    List<ItemDataModel> findByIdInOrderByDescriptionAsc(Collection<String> ids);


}