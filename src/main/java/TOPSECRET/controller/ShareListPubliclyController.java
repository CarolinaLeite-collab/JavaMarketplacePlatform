package TOPSECRET.controller;

import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems.ListOfItems;
import TOPSECRET.domain.User;
import TOPSECRET.domain.valueobject.UserId;

import java.util.List;

/**
 * Controller responsible for sharing publicly a list of of a {@link User}.
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
