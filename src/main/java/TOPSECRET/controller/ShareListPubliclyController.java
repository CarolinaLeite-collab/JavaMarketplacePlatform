package TOPSECRET.controller;

import TOPSECRET.domain.IListOfPublicationsRepo;
import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.User.User;

import java.util.List;

/**
 * Controller responsible for sharing publicly a list of of a {@link User}.
 * <p>
 * This controller depends on {@link IListOfPublicationsRepo} to access persisted lists.
 * </p>
 */

public class ShareListPubliclyController {
    private final IListOfPublicationsRepo _iListOfPublicationsRepo;

    public ShareListPubliclyController(IListOfPublicationsRepo iListOfPublicationsRepo, User user) {
        _iListOfPublicationsRepo = iListOfPublicationsRepo;
    }

    public List<ListOfPublications> getListOfLists(User user) {
        return _iListOfPublicationsRepo.findListsByUser(user);
    }

}
