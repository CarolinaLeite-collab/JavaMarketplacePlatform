package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.library.LibraryFactory;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.persistence.jpa.datamodel.LibraryDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

@Component
@AllArgsConstructor
public class LibraryAssembler {

    private LibraryFactory _libraryFactory;

    public LibraryDataModel toDataModel(Library library) {

        if (library == null) {
            throw new IllegalArgumentException("Library cannot be null");
        }

        String libraryId = library.identity().toString();

        List<String> itemIds = library.getItemsIdInLibrary()
                .stream()
                .map(ItemId::toString)
                .toList();

        return new LibraryDataModel(
                libraryId,
                itemIds
        );
    }

    public Library toDomain(LibraryDataModel libraryDataModel) {

        if (libraryDataModel == null) {
            throw new IllegalArgumentException("LibraryDataModel cannot be null");
        }

        LibraryId libraryId = new LibraryId(new Email(libraryDataModel.getLibraryId()));

        List<ItemId> itemIds = libraryDataModel.getItemIds()
                .stream()
                .map(ItemId::new)
                .toList();

        return _libraryFactory.createLibrary(libraryId, itemIds);
    }

    public List<LibraryDataModel> listToDataModel(List<Library> libraries) {
        List<LibraryDataModel> list = new ArrayList<>();

        for (Library library : libraries) {
            list.add(toDataModel(library));
        }

        return list;
    }

    public List<Library> listToDomain(List<LibraryDataModel> libraryDMs) {
        List<Library> list = new ArrayList<>();

        for (LibraryDataModel libraryDataModel : libraryDMs) {
            list.add(toDomain(libraryDataModel));
        }

        return list;
    }
}

