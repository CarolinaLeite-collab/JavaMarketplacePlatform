package MITELOVERS.persistence.springdata;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import MITELOVERS.persistence.jpa.datamodel.ShoppingCartDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link Sale} entities.
 */

public interface ISaleSpringDataRepo extends JpaRepository<SaleDataModel,String> {

    List<SaleDataModel> findByUserId(String userId);

}
