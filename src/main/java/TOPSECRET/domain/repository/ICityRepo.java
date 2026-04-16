package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.city.City;
import TOPSECRET.domain.valueobject.CityId;



public interface ICityRepo extends IRepository<CityId, City> {
    City addCity(City city);
    

}