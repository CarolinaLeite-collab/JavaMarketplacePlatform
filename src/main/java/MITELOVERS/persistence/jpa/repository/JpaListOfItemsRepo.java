package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.persistence.jpa.assembler.ListOfItemsAssembler;
import MITELOVERS.persistence.jpa.datamodel.ListOfItemsDataModel;
import MITELOVERS.persistence.jpa.springdata.IListOfItemsSpringDataRepo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
@Profile("jpa")
public class JpaListOfItemsRepo implements IListOfItemsRepo {

    private final IListOfItemsSpringDataRepo _springDataRepo;
    private final ListOfItemsAssembler _assembler;

    public JpaListOfItemsRepo(IListOfItemsSpringDataRepo springDataRepo,
                              ListOfItemsAssembler assembler) {
        _springDataRepo = springDataRepo;
        _assembler = assembler;
    }

    @Override
    public ListOfItems save(ListOfItems entity) {
        ListOfItemsDataModel dm = _assembler.toDataModel(entity);
        ListOfItemsDataModel savedDm = _springDataRepo.save(dm);
        return _assembler.toDomain(savedDm);
    }

    @Override
    public Iterable<ListOfItems> findAll() {
        Iterable<ListOfItemsDataModel> dms = _springDataRepo.findAll();
        List<ListOfItems> list = new ArrayList<>();
        for (ListOfItemsDataModel dm : dms) {
            list.add(_assembler.toDomain(dm));
        }
        return list;
    }

    @Override
    public List<ListOfItemsId> findAllKeys() {
        Iterable<ListOfItemsDataModel> dms = _springDataRepo.findAll();
        List<ListOfItemsId> keys = new ArrayList<>();
        for (ListOfItemsDataModel dm : dms) {
            keys.add(new ListOfItemsId(dm.getListOfItemsId()));
        }
        return keys;
    }

    @Override
    public Optional<ListOfItems> ofIdentity(ListOfItemsId id) {
        ListOfItemsDataModel dm = _springDataRepo.findById(id.toString())
                .orElseThrow(() -> new IllegalArgumentException("ListOfItems not found!"));
        return Optional.of(_assembler.toDomain(dm));
    }

    @Override
    public boolean containsOfIdentity(ListOfItemsId id) {
        return _springDataRepo.existsById(id.toString());
    }

}