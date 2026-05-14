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

    public boolean shareListPublicly(ListOfItemsId listOfItemsId, SharedDuration duration) {
        ListOfItems list = _iListOfItemsRepo.ofIdentity(listOfItemsId)
                .orElseThrow(() -> new IllegalStateException("List not found"));

        if (list.isPrivate()) {
            list.makePublic(duration);
            _iListOfItemsRepo.save(list);

            return true;
        }
        else {
            throw new IllegalStateException("List is already public");
        }
    }

    public List<ListOfItemsId> findListsByUserId(UserId userId) {
        if (userId == null)
            throw new IllegalArgumentException("UserId is mandatory");

        List<ListOfItems> userLists = _iListOfItemsRepo.findListOfItemsByUserId(userId);

        List<ListOfItemsId> listOfItemsIds = new ArrayList<>();

        for (ListOfItems listOfItems : userLists) {
            listOfItemsIds.add(listOfItems.identity());
        }
        return listOfItemsIds;
    }
}