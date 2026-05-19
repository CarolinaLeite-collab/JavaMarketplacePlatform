package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.valueobject.EditionId;

public interface IEditionRepo extends IRepository<EditionId, Edition> {

}
