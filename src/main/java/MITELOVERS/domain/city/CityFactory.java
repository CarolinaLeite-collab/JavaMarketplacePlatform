package MITELOVERS.domain.city;

import MITELOVERS.domain.valueobject.CountryId;



public class CityFactory {

    public City createCity(String cityName, CountryId countryId)  {

        return new City(cityName, countryId);
    }
}
