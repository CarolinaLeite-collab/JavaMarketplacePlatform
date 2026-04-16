package TOPSECRET.domain.city;

import TOPSECRET.domain.country.Country;
import TOPSECRET.domain.valueobject.CountryId;

public class CityFactory {

    public City createCity(String cityName, CountryId countryId)  {

        return new City(cityName, countryId);
    }
}
