package TOPSECRET.domain.repository;

import TOPSECRET.ddd.IRepository;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.library.Library;
import TOPSECRET.domain.valueobject.LibraryId;
import TOPSECRET.domain.valueobject.UserId;

import java.util.List;

public interface ILibraryRepo extends IRepository<LibraryId, Library> {

    Library addLibrary(UserId userId);

    Library findLibraryByUserId(UserId userId);

    List<Item> getItemsInLibraryByUserId(UserId userId);
}
