package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CountryId;

/**
 * Repository interface for {@link Country} aggregates.
 * <p>
 * Extends {@link IRepository} with a domain-specific query by name.
 * </p>
 */

public interface ICountryRepo extends IRepository<CountryId, Country> {

    Country addCountry(String countryName);

}
