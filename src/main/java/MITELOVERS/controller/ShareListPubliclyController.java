package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for sharing publicly a list of of a {@link UserId}.
 * <p>
 * This controller depends on {@link IListOfItemsRepo} to access persisted lists.
 * </p>
 */

public class ShareListPubliclyController {
    private final IListOfItemsRepo _iListOfItemsRepo;

    public ShareListPubliclyController(IListOfItemsRepo iListOfItemsRepo, UserId userId) {
        _iListOfItemsRepo = iListOfItemsRepo;
    }

    public List<ListOfItems> getListOfLists(UserId userId) {

        return findListsByUserId(userId);
    }

    // Method to be moved to future service layer and adapted accordingly
    public List<ListOfItems> findListsByUserId(UserId userId) {

        if (userId == null) {
            throw new IllegalArgumentException("UserId is mandatory");
        }

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
