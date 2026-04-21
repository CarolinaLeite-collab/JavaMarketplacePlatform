package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.List;

public interface ILibraryRepo extends IRepository<LibraryId, Library> {

    Library addLibrary(UserId userId);

    Library findLibraryByUserId(UserId userId);

    List<ItemId> getItemsInLibraryByUserId(UserId userId);

    boolean existsItemIdInAnyLibrary(ItemId itemId);
}
