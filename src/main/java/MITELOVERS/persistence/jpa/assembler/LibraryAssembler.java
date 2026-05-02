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

    public LibraryDataModel domain2dm(Library library) {

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

    public Library dm2domain(LibraryDataModel libraryDataModel) {

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

    public List<LibraryDataModel> domainList2dmList(List<Library> libraries) {
        List<LibraryDataModel> list = new ArrayList<>();

        for (Library library : libraries) {
            list.add(domain2dm(library));
        }

        return list;
    }

    public List<Library> dmList2DomainList(List<LibraryDataModel> libraryDMs) {
        List<Library> list = new ArrayList<>();

        for (LibraryDataModel libraryDataModel : libraryDMs) {
            list.add(dm2domain(libraryDataModel));
        }

        return list;
    }
}

