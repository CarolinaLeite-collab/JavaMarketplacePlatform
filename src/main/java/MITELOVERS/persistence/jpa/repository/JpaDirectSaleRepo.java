package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.repository.IDirectSaleRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.assembler.DirectSaleAssembler;
import MITELOVERS.persistence.jpa.datamodel.DirectSaleDataModel;
import MITELOVERS.persistence.springdata.IDirectSaleSpringDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaDirectSaleRepo implements IDirectSaleRepo {

    @Autowired
    IDirectSaleSpringDataRepo _iDirectSaleSpringDataRepo;

    @Autowired
    DirectSaleAssembler _directSaleAssembler;

    @Override
    public DirectSale save(DirectSale entity) {

        DirectSaleDataModel dbEntity = _directSaleAssembler.toDataModel(entity);

        DirectSaleDataModel savedDbEntity = _iDirectSaleSpringDataRepo.save(dbEntity);

        return _directSaleAssembler.toDomain(savedDbEntity);
    }

    @Override
    public Iterable<DirectSaleId> findAllKeys() {

        Iterable<DirectSaleDataModel> directSaleDms = _iDirectSaleSpringDataRepo.findAll();

        List<DirectSaleId> directSaleIds = new ArrayList<>();

        for (DirectSaleDataModel directSaleDataModel : directSaleDms) {
            directSaleIds.add((new DirectSaleId(directSaleDataModel.getDirectSaleId())));
        }

        return directSaleIds;
    }

    @Override
    public Iterable<DirectSale> findAll() {

       Iterable<DirectSaleDataModel> directSaleDms = _iDirectSaleSpringDataRepo.findAll();

       List<DirectSale> directSales = new ArrayList<>();

       for (DirectSaleDataModel directSaleDataModel : directSaleDms){

           directSales.add(_directSaleAssembler.toDomain(directSaleDataModel));
       }

       return directSales;
    }

    @Override
    public Optional<DirectSale> ofIdentity(DirectSaleId id) {

        Optional<DirectSaleDataModel> dbDirectSale =
                _iDirectSaleSpringDataRepo.findById(id.toString());

        if ( dbDirectSale.isPresent() ) {

            DirectSale directSaleEntity = _directSaleAssembler.toDomain(dbDirectSale.get());

            return  Optional.of(directSaleEntity);
        }
        return Optional.empty();
    }

    @Override
    public boolean containsOfIdentity(DirectSaleId id) {

        return _iDirectSaleSpringDataRepo.existsById(id.toString());
    }

    @Override
    public List<ItemId> findDirectSaleItemsByAuthorIdSortedByDescription(AuthorId authorId){
        return new ArrayList<>();
    }

    @Override
    public List<DirectSale> findByItemsIdSortedByPublicationDateAsc(List<ItemId> itemIds) {

        List<String> idsAsString = itemIds.stream()
                .map(ItemId::toString)
                .toList();

        return _iDirectSaleSpringDataRepo
                .findByItemsIdOrderByCreationDateAsc(idsAsString)
                .stream()
                .map(_directSaleAssembler::toDomain)
                .filter(ds -> ds.getItemsId().stream().anyMatch(itemIds::contains))
                .toList();
    }

    @Override
    public List<DirectSale> findByItemsIdSortedByPublicationDateDesc(List<ItemId> itemIds) {

        List<String> idsAsString = itemIds.stream()
                .map(ItemId::toString)
                .toList();

        return _iDirectSaleSpringDataRepo
                .findByItemsIdOrderByCreationDateDesc(idsAsString)
                .stream()
                .map(_directSaleAssembler::toDomain)
                .filter(ds -> ds.getItemsId().stream().anyMatch(itemIds::contains))
                .toList();
    }

}
