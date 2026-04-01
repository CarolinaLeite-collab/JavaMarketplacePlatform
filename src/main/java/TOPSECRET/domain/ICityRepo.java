package TOPSECRET.domain;

import TOPSECRET.domain.country.Country;

import java.util.List;

public interface ICityRepo {

    City registerCity(String cityName, Country country);

    boolean existsCityInACountry(String cityName, Country country);

    List<City> getAllCities();
}
