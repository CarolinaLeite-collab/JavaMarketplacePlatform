package MITELOVERS.domain.city;

import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import org.springframework.stereotype.Component;


@Component
public class CityFactory {

    public City createCity(String cityName, CountryId countryId)  {
        return new City(cityName, countryId);
    }

    public City createCity(String cityName, CountryId countryId, CityId cityId)  {
        return new City(cityName, countryId, cityId);
    }
}
