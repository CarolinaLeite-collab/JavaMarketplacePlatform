package TOPSECRET.controller;

import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.IListOfPublicationsRepo;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Controller responsible for sharing publicly a list of of a {@link User}.
 * <p>
 * This controller depends on {@link IListOfPublicationsRepo} to access persisted lists.
 * </p>
 */

public class ShareListPubliclyController {
    private final IListOfPublicationsRepo _iListOfPublicationsRepo;

    public ShareListPubliclyController(IListOfPublicationsRepo iListOfPublicationsRepo) {
        _iListOfPublicationsRepo = iListOfPublicationsRepo;
    }

    public List<ListOfPublications> getListOfLists(User user) {
        return _iListOfPublicationsRepo.findListsByUser(user);
    }

    public boolean shareListPublicly(ListOfPublications selectedList) {
        if (selectedList == null) {
            return false;
        }
        selectedList.makePublic();
        return true;
    }
}
