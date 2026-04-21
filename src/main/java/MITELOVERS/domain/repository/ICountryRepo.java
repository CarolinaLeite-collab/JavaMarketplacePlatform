package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.country.Country;
import MITELOVERS.domain.valueobject.CountryId;

/**
 * Repository interface for {@link Country} aggregates.
 * <p>
 * Extends {@link IRepository} with a domain-specific query by name.
 * </p>
 */

public interface ICountryRepo extends IRepository<CountryId, Country> {

    Country addCountry(String countryName);

}
