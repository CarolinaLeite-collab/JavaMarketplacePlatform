package MITELOVERS.domain.listofitems;

import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ListOfItemsId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.domain.valueobject.SharedDuration;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
/**
 * Factory responsible for creating {@link ListOfItems} instances.
 */
@Component
public class ListOfItemsFactory {

    /**
     * Creates a new private {@link ListOfItems} with a generated identity.
     */
    public ListOfItems createListOfItems(UserId userId, Name name, GenreId genreId) {
        return new ListOfItems(userId, name, genreId);
    }

    /**
     * Reconstructs a {@link ListOfItems} from an existing identity.
     * Used by the assembler during reconstitution from persistence.
     */
    public ListOfItems createListOfItems(ListOfItemsId listOfItemsId, UserId userId,
                                         Name name, GenreId genreId,
                                         boolean isPrivate, LocalDateTime sharedUntil) {
        return new ListOfItems(listOfItemsId, userId, name, genreId, isPrivate, sharedUntil);
    }

    /**
     * Creates a new {@link ListOfItems} and immediately makes it public for the given duration.
     */
    public ListOfItems createPublicListOfItems(UserId userId, Name name, GenreId genreId,
                                               SharedDuration duration) {
        ListOfItems list = createListOfItems(userId, name, genreId);
        list.makePublic(duration);
        return list;
    }
}