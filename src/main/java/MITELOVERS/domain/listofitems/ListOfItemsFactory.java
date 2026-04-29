package MITELOVERS.domain.listofitems;

import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

@Component
public class ListOfItemsFactory {

    public ListOfItems createListOfItems(UserId userId, Name name, GenreId genreId) {
        return new ListOfItems(userId, name, genreId);
    }

    public ListOfItems createListOfItems(ListOfItemsId listOfItemsId, UserId userId,
                                         Name name, GenreId genreId) {
        return new ListOfItems(listOfItemsId, userId, name, genreId);
    }

    public ListOfItems createPublicListOfItems(UserId userId, Name name, GenreId genreId) {
        ListOfItems list = createListOfItems(userId, name, genreId);
        list.makePublic();
        return list;
    }

}