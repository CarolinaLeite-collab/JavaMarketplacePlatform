package MITELOVERS.persistence.jpa.springdata;

import MITELOVERS.persistence.jpa.datamodel.ListOfItemsDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link ListOfItemsDataModel}.
 */
public interface IListOfItemsSpringDataRepo extends JpaRepository<ListOfItemsDataModel, String> {
}