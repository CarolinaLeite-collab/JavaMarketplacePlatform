package TOPSECRET.domain.city;

import TOPSECRET.domain.country.Country;

public class CityFactory {

    public City createCity(String cityName, Country country)  {

        return new City(cityName, country);
    }
}
