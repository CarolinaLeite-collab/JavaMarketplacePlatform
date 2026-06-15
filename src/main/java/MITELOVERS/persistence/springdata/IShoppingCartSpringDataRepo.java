package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.ShoppingCartDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link ShoppingCartDataModel} entities.
 */

public interface IShoppingCartSpringDataRepo extends JpaRepository<ShoppingCartDataModel, String> {

    Optional<ShoppingCartDataModel> findByBuyerId(String buyerId);

}
