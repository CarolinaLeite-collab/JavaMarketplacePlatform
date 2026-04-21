package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.city.City;
import MITELOVERS.domain.valueobject.CityId;


public interface ICityRepo extends IRepository<CityId, City> {
    City addCity(City city);

}
