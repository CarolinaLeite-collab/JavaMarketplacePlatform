package MITELOVERS.domain.listofitems;

import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating {@link ListOfItems} instances.
 */
@Component
public class ListOfItemsFactory {

    // Used by the controller — generates a new ID
    public ListOfItems createListOfItems(UserId userId, String name, GenreId genreId) {
        return new ListOfItems(userId, name, genreId);
    }

    // Used by the assembler — reconstructs from an existing ID
    public ListOfItems createListOfItems(ListOfItemsId listOfItemsId, UserId userId,
                                         String name, GenreId genreId) {
        return new ListOfItems(listOfItemsId, userId, name, genreId);
    }

    public ListOfItems createPublicListOfItems(UserId userId, String name, GenreId genreId) {
        ListOfItems list = createListOfItems(userId, name, genreId);
        list.makePublic();
        return list;
    }
}