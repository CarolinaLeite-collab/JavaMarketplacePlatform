package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.city.City;
import MITELOVERS.domain.repository.ICityRepo;
import MITELOVERS.domain.valueobject.CityId;
import MITELOVERS.domain.valueobject.CountryId;
import MITELOVERS.persistence.jpa.assembler.CityAssembler;
import MITELOVERS.persistence.jpa.datamodel.CityDataModel;
import MITELOVERS.persistence.springdata.ICitySpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaCityRepo implements ICityRepo {

    @Autowired
    private ICitySpringDataRepo springDataRepo;

    @Autowired
    private CityAssembler assembler;

    @Override
    public City save(City city) {
        CityDataModel dm = assembler.toDataModel(city);
        CityDataModel saved = springDataRepo.save(dm);
        return assembler.toDomain(saved);
    }

    @Override
    public Iterable<City> findAll() {
        List<CityDataModel> list = springDataRepo.findAll();
        List<City> listDomain = assembler.toDomainList(list);
        return listDomain;
    }

    @Override
    public List<CityId> findAllKeys() {
        List<CityDataModel> list = springDataRepo.findAll();
        List<CityId> ids = new ArrayList<>();

        for (CityDataModel dm : list) {
            ids.add(new CityId(dm.getName(),
                    new CountryId(dm.getCountryId())));
        }
        return ids;
    }

    @Override
    public Optional<City> ofIdentity(CityId cityId) {
        return springDataRepo.findById(cityId.toString())
                .map(assembler::toDomain);
    }

    @Override
    public boolean containsOfIdentity(CityId cityId) {
        return springDataRepo.existsById(cityId.toString());
    }
}