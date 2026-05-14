package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IItemSpringDataRepo extends JpaRepository<ItemDataModel, String> {

    List<ItemDataModel> findByIdInOrderByDescriptionAsc(Collection<String> ids);


}