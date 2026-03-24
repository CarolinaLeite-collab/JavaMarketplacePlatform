package TOPSECRET.domain;

import java.util.List;

public interface ICityRepo {

    public City registerCity(String cityName, Country country);

    public boolean existsCityInACountry(String cityName, Country country);

    public List<City> getAllCities();

}
