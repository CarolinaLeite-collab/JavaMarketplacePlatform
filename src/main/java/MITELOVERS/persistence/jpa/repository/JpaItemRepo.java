package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.assembler.ItemAssembler;
import MITELOVERS.persistence.jpa.datamodel.ItemDataModel;
import MITELOVERS.persistence.springdata.IItemSpringDataRepo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaItemRepo implements IItemRepo {

    private final IItemSpringDataRepo _itemSpringDataRepo;

    private final ItemAssembler _itemAssembler;

    public JpaItemRepo(IItemSpringDataRepo springRepo, ItemAssembler assembler) {

        _itemSpringDataRepo = springRepo;
        _itemAssembler = assembler;

    }

    @Override
    public Item save(Item item) {

        ItemDataModel dmToSave = _itemAssembler.toDataModel(item);

        ItemDataModel savedDm = _itemSpringDataRepo.save(dmToSave);

        return _itemAssembler.toDomain(savedDm);

    }

    @Override
    public List<ItemId> findAllKeys() {

        List<ItemDataModel> itemDms = _itemSpringDataRepo.findAll();

        List<ItemId> itemIds = new ArrayList<>();

        for (ItemDataModel ItemDM : itemDms) {

            itemIds.add(new ItemId(ItemDM.getId()));

        }

        return itemIds;

    }

    @Override
    public Iterable<Item> findAll() {

        Iterable<ItemDataModel> itemDms = _itemSpringDataRepo.findAll();

        List<Item> items = new ArrayList<>();

        for (ItemDataModel ItemDM : itemDms) {

            items.add(_itemAssembler.toDomain(ItemDM));

        }

        return items;

    }

    @Override
    public Optional<Item> ofIdentity(ItemId id) {

        ItemDataModel savedItemDataModel = _itemSpringDataRepo.findById(id.getValue()).orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        return Optional.of(_itemAssembler.toDomain(savedItemDataModel));

    }

    @Override
    public boolean containsOfIdentity(ItemId id) {

        return _itemSpringDataRepo.existsById(id.toString());

    }
}
