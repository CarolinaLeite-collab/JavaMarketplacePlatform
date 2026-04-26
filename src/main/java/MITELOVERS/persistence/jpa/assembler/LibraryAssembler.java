package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.datamodel.LibraryDataModel;

import java.util.List;

/**
 * Assembler responsible for converting {@link Library} domain objects
 * into {@link LibraryDataModel} instances.
 *
 * <p>
 * Assemblers ensure that presentation layers never depend on domain objects.
 * They extract only the data required for display or transport.
 * </p>
 */

public class LibraryAssembler {

    private LibraryAssembler() {
        // Prevent instantiation
    }

    public static LibraryDataModel toDTO(Library library) {

        if (library == null) {
            throw new IllegalArgumentException("Library cannot be null");
        }

        String libraryId = library.identity().toString();

        List<String> itemIds = library.getItemsIdInLibrary()
                .stream()
                .map(ItemId::toString)
                .toList();

        return new LibraryDataModel(libraryId, itemIds);
    }
}
