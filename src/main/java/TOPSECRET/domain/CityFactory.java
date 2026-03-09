package TOPSECRET.domain;

public class CityFactory {

    public City createCity(String cityName, Country country)  {

        return new City(cityName, country);
    }
}
