package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.valueobject.LibraryId;
import org.springframework.stereotype.Repository;

public interface ILibraryRepo extends IRepository<LibraryId, Library> {

}
