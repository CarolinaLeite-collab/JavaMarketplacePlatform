package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.directsale.DirectSale;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.DirectSaleId;
import MITELOVERS.domain.valueobject.ItemId;

import java.util.List;


public interface IDirectSaleRepo extends IRepository<DirectSaleId, DirectSale> {

    List<ItemId> findDirectSaleItemsByAuthorIdSortedByDescription(AuthorId authorId);

    List<DirectSale> findByItemsIdSortedByPublicationDateAsc(List<ItemId> itemIds);

    List<DirectSale> findByItemsIdSortedByPublicationDateDesc(List<ItemId> itemIds);

    List<DirectSaleId> findExpired();

    void deleteDirectSale(DirectSaleId directSaleId);
}
