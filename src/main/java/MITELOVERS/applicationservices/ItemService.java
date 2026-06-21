package MITELOVERS.applicationservices;

import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.item.ItemFactory;
import MITELOVERS.domain.repository.IEditionRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.valueobject.Condition;
import MITELOVERS.domain.valueobject.Description;
import MITELOVERS.domain.valueobject.EditionId;
import MITELOVERS.domain.valueobject.ItemId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Application service responsible for orchestrating item-related use cases,
 * including item registration and retrieval.
 */
@Service
public class ItemService {

    private final IItemRepo _iItemRepo;
    private final ItemFactory _itemFactory;
    private final IEditionRepo _iEditionRepo;

    public ItemService(IItemRepo iItemRepo,
                       ItemFactory itemFactory,
                       IEditionRepo iEditionRepo) {

        _iItemRepo    = Objects.requireNonNull(iItemRepo,    "ItemRepo is required");
        _itemFactory  = Objects.requireNonNull(itemFactory,  "ItemFactory is required");
        _iEditionRepo = Objects.requireNonNull(iEditionRepo, "EditionRepo is required");
    }


    @Transactional
    public Item registerItem(EditionId editionId,
                             Condition condition,
                             Description description) {

        _iEditionRepo.ofIdentity(editionId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Edition does not exist in the repository"));

        Item newItem = _itemFactory.createItem(editionId, condition, description);
        ItemId itemId = newItem.identity();

        if (_iItemRepo.containsOfIdentity(itemId)) {
            return _iItemRepo.ofIdentity(itemId)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Item with id '" + itemId + "' does not exist"));
        } else {
            return _iItemRepo.save(newItem);
        }
    }

    @Transactional
    public List<Item> getAllItems() {
        List<Item> result = new ArrayList<>();
        _iItemRepo.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public Item getItemById(String itemId) {
        return _iItemRepo.ofIdentity(new ItemId(itemId))
                .orElseThrow(() -> new NoSuchElementException(
                        "Item does not exist in the repository"));
    }

    @Transactional
    public Item markItemAsSold(String itemId) {
        Item item = getItemById(itemId);
        item.markAsSold();
        return _iItemRepo.save(item);
    }
}