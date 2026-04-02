package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.City.City;
import TOPSECRET.domain.valueobject.CityId;

public interface ICityRepo extends IRepository<CityId, City> {
}