package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.item.Item;
import MITELOVERS.domain.valueobject.GenreId;
import MITELOVERS.domain.valueobject.ItemId;
import java.util.Collection;
import java.util.List;

public interface IItemRepo extends IRepository<ItemId, Item> {

    List<Item> findByIdInOrderByDescriptionAsc(Collection<String> ids);

    List<ItemId> findByGenreId(GenreId genreId);
}
