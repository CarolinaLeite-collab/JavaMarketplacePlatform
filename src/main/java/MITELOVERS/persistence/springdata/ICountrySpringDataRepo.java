package MITELOVERS.persistence.springdata;

import MITELOVERS.persistence.jpa.datamodel.CountryDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link CountryDataModel} persistence.
 * Provides standard CRUD operations with {@link String} as the ID type.
 */

public interface ICountrySpringDataRepo extends JpaRepository<CountryDataModel, String> {


}

