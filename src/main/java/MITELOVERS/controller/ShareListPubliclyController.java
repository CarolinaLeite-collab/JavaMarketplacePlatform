package MITELOVERS.controller;

import MITELOVERS.domain.listofitems.ListOfItems;
import MITELOVERS.domain.repository.IListOfItemsRepo;
import MITELOVERS.domain.valueobject.UserId;

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
        return _iListOfItemsRepo.findListsByUserId(userId);
    }

}
