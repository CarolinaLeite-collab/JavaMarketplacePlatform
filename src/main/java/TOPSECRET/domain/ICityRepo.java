package TOPSECRET.domain;

import java.util.List;

public interface ICityRepo {

    City registerCity(String cityName, Country country);

    boolean existsCityInACountry(String cityName, Country country);

    List<City> getAllCities();

}
