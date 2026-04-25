package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.valueobject.CountryId;

/**
 * Repository interface for {@link Country} aggregates.
 * <p>
 * This abstraction is responsible only for persistence operations
 * exposed by {@link IRepository}.
 * </p>
 */

public interface ICountryRepo extends IRepository<CountryId, Country> {

}
