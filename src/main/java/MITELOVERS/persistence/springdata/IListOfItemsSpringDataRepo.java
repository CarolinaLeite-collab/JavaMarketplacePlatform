package MITELOVERS.persistence.springdata;

import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.jpa.datamodel.ListOfItemsDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link ListOfItemsDataModel}.
 */
public interface IListOfItemsSpringDataRepo extends JpaRepository<ListOfItemsDataModel, String> {

    List<ListOfItemsDataModel> findListOfItemsByUserId(UserId userId);
}