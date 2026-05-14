package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.SharedDuration;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for sharing publicly a list of a {@link UserId}.
 */
public class ShareListPubliclyController {

    private final IListOfItemsRepo _iListOfItemsRepo;

    public ShareListPubliclyController(IListOfItemsRepo iListOfItemsRepo) {
        _iListOfItemsRepo = iListOfItemsRepo;
    }

    public List<ListOfItems> getListOfLists(UserId userId) {
        return findListsByUserId(userId);
    }

    public boolean shareListPublicly(ListOfItemsId listOfItemsId, SharedDuration duration) {
        ListOfItems list = _iListOfItemsRepo.ofIdentity(listOfItemsId)
                .orElseThrow(() -> new IllegalStateException("List not found"));

        list.makePublic(duration);
        _iListOfItemsRepo.save(list);
        return true;
    }

    public List<ListOfItems> findListsByUserId(UserId userId) {
        if (userId == null)
            throw new IllegalArgumentException("UserId is mandatory");

        Iterable<ListOfItems> all = _iListOfItemsRepo.findAll();
        List<ListOfItems> result = new ArrayList<>();
        for (ListOfItems list : all) {
            if (userId.equals(list.getUserId())) {
                result.add(list);
            }
        }
        return result;
    }
}