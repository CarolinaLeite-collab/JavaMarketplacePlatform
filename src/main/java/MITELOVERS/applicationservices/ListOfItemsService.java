package MITELOVERS.applicationservices;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.listofitems.ListOfItemsFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.domain.repository.IItemRepo;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service responsible for retrieving List of Items information
 * and converting domain objects into response DTOs.
 */

@Service
@AllArgsConstructor
public class ListOfItemsService {
    private IListOfItemsRepo _listOfItemsRepo;
    private ListOfItemsFactory _factory;
    private IGenreRepo _genreRepo;
    private IItemRepo _itemRepo;

    @Transactional(readOnly = true)
    public List<ListOfItems> getUserLists(UserId userId) {

        Iterable<ListOfItems> lists = _listOfItemsRepo.findListOfItemsByUserId(userId);

        List<ListOfItems> result = new ArrayList<>();

        for (ListOfItems list : lists) {

            result.add(list);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public ListOfItems getListById(ListOfItemsId listId) {

        ListOfItems list = _listOfItemsRepo.ofIdentity(listId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        return list;
    }

    @Transactional
    public ListOfItems save(UserId userId, Name name, GenreId genreId) {

        if(!_genreRepo.containsOfIdentity(genreId)) {
            throw new IllegalArgumentException("Genre doesn't exist");
        }

        if (getUserLists(userId).contains(name.toString())) {
            throw new IllegalArgumentException("List already exists");
        }

        ListOfItems result = _listOfItemsRepo.save(_factory.createListOfItems(userId, name, genreId));

        return result;
    }

    @Transactional
    public ListOfItems addItemToList(ListOfItemsId listOfItemsId, ItemId itemId) {

        if (itemId == null) {
            throw new IllegalArgumentException("ItemId is invalid");
        }

        ListOfItems list = _listOfItemsRepo.ofIdentity(listOfItemsId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        if (!_itemRepo.containsOfIdentity(itemId)) {
            throw new IllegalArgumentException("Item doesn't exist");
        }

        list.addItem(itemId);
        _listOfItemsRepo.save(list);

        return list;
    }

    @Transactional
    public ListOfItems makePublic(ListOfItemsId listOfItemsId, SharedDuration sharedDuration) {

        ListOfItems list = _listOfItemsRepo.ofIdentity(listOfItemsId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        if (list.isPrivate()) {

            list.makePublic(sharedDuration);
        }
        _listOfItemsRepo.save(list);

        return list;
    }

    @Transactional
    public ListOfItems makePrivate(ListOfItemsId listOfItemsId){

        ListOfItems list = _listOfItemsRepo.ofIdentity(listOfItemsId).orElseThrow(() -> new IllegalArgumentException("ListOfItems not found"));

        if (!list.isPrivate()) {

            list.makePrivate();
        }
        _listOfItemsRepo.save(list);

        return list;
    }

    @Transactional
    public List<ListOfItems> findByGenre(GenreId genreId) {

        Iterable<ListOfItems> lists = _listOfItemsRepo.findAll();

        List<ListOfItems> publicListsByGenre = new ArrayList<>();

        for(ListOfItems list : lists) {
            if(list.getGenreId().equals(genreId) && !list.isPrivate()) {
                publicListsByGenre.add(list);
            }
        }

        return publicListsByGenre;
    }

    @Transactional
    public void deleteList(ListOfItemsId listOfItemsId) {

        _listOfItemsRepo.deleteListOfItems(listOfItemsId);
    }

    @Transactional(readOnly = true)
    public List<ListOfItems> getPublicLists() {

        List<ListOfItems> result = new ArrayList<>();

        _listOfItemsRepo.findAll().forEach(result::add);

        return result.stream()
                .filter(list -> !list.isPrivate())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemId> getItemsInPublicList(ListOfItemsId listId) {
        ListOfItems list = _listOfItemsRepo.ofIdentity(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        if (list.isPrivate()) {
            throw new IllegalStateException("List is private");
        }

        return list.getItemIds();
    }

}
