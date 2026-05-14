package MITELOVERS.domain.library;

import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory responsible for creating instances of {@link Library}.
 *
 * <p>
 * This factory encapsulates the creation logic of {@link Library} aggregates,
 * ensuring that they are instantiated in a consistent and valid state.
 * </p>
 *
 * <p>
 * A {@link Library} is created based on a {@link UserId}, and validation rules
 * (such as non-null constraints) are enforced by the {@link Library} constructor.
 * </p>
 */

@Component
public class LibraryFactory {

    public Library createLibrary(UserId userId) {

        return new Library(userId);

    }

    // Reconstruction from persistence (assembler → domain)
    public Library createLibrary(LibraryId libraryId,
                                 List<ItemId> itemIds) {

        return new  Library(libraryId, itemIds);
    }
}
