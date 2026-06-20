package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.repository.ISaleRepo;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.persistence.jpa.assembler.SaleAssembler;
import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import MITELOVERS.persistence.springdata.ISaleSpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
@Profile("jpa")
public class JpaSaleRepo implements ISaleRepo {

    @Autowired
    ISaleSpringDataRepo _iSaleSpringDataRepo;

    @Autowired
    SaleAssembler _saleAssembler;

    @Override
    public Sale save(Sale entity) {
        SaleDataModel saleDataModel = _saleAssembler.toDataModel(entity);
        SaleDataModel savedSaleDataModel = _iSaleSpringDataRepo.save(saleDataModel);
        return _saleAssembler.toDomain(savedSaleDataModel);
    }

    @Override
    public Iterable<SaleId> findAllKeys() {
        return StreamSupport.stream(_iSaleSpringDataRepo.findAll().spliterator(), false)
                .map(SaleDataModel::getSaleId)
                .map(SaleId::new)
                .toList();
    }

    @Override
    public Iterable<Sale> findAll() {
        return StreamSupport.stream(_iSaleSpringDataRepo.findAll().spliterator(), false)
                .map(_saleAssembler::toDomain)
                .toList();
    }

    @Override
    public Optional<Sale> ofIdentity(SaleId id) {
        return _iSaleSpringDataRepo.findById(id.toString())
                .map(_saleAssembler::toDomain);
    }

    @Override
    public boolean containsOfIdentity(SaleId id) {
        return _iSaleSpringDataRepo.existsById(id.toString());
    }
}