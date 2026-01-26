package TOPSECRET.controller;

import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.ListOfPublicationsRepo;
import TOPSECRET.domain.User;
import java.util.List;

class ShareListPubliclyController {
    private final ListOfPublicationsRepo _listOfPublicationsRepo;

    public ShareListPubliclyController(ListOfPublicationsRepo listOfPublicationsRepo) {
        _listOfPublicationsRepo = listOfPublicationsRepo;
    }

    public List<ListOfPublications> getListOfLists(User user) {
        return _listOfPublicationsRepo.findListsByUser(user);
    }

    public boolean shareListPublicly(ListOfPublications selectedList) {
        if (selectedList == null) {
            return false;
        }
        selectedList.switchVisibility();
        return true;
    }
}
