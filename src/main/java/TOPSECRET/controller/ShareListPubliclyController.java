package TOPSECRET.controller;

import TOPSECRET.domain.IListOfItemsRepo;
import TOPSECRET.domain.ListOfItems;
import TOPSECRET.domain.user.User;



import java.util.List;

/**
 * Controller responsible for sharing publicly a list of of a {@link User}.
 * <p>
 * This controller depends on {@link IListOfItemsRepo} to access persisted lists.
 * </p>
 */

public class ShareListPubliclyController {
    private final IListOfItemsRepo _iListOfItemsRepo;

    public ShareListPubliclyController(IListOfItemsRepo iListOfItemsRepo, User user) {
        _iListOfItemsRepo = iListOfItemsRepo;
    }

    public List<ListOfItems> getListOfLists(User user) {
        return _iListOfItemsRepo.findListsByUser(user);
    }

}
