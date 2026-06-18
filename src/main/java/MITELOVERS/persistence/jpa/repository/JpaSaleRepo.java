package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.repository.ISaleRepo;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.persistence.springdata.ISaleSpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaSaleRepo implements ISaleRepo {

    @Autowired
    ISaleSpringDataRepo _iSaleSpringDataRepo;

    @Override
    public Sale save(Sale entity) {
        return null;
    }

    @Override
    public Iterable<SaleId> findAllKeys() {
        return null;
    }

    @Override
    public Iterable<Sale> findAll() {
        return null;
    }

    @Override
    public Optional<Sale> ofIdentity(SaleId id) {
        return Optional.empty();
    }

    @Override
    public boolean containsOfIdentity(SaleId id) {
        return false;
    }
}
