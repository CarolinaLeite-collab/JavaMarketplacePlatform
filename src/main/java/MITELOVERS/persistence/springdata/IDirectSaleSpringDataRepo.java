package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDirectSaleSpringDataRepo extends JpaRepository<DirectSaleDataModel, String> {

    List<DirectSaleDataModel> findByItemsIdOrderByCreationDateAsc(List<String> itemsId);

    List<DirectSaleDataModel> findByItemsIdOrderByCreationDateDesc(List<String> itemsId);

}
